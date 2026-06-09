package pchy.pchy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pchy.pchy.models.CasoPrueba;
import pchy.pchy.models.ResultadoCaso;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JudgeService {

    @Value("${judge0.api.url}")
    private String apiUrl;

    @Value("${judge0.api.key}")
    private String apiKey;

    @Value("${judge0.api.host}")
    private String apiHost;

    @Value("${judge0.lang.python}")
    private int langPython;

    @Value("${judge0.lang.cpp}")
    private int langCpp;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // Resultado de ejecutar contra un caso
    public record EjecucionResult(
        boolean correcto,
        String salidaObtenida,
        float tiempoEjecucion,
        String error
    ) {}

    // Resultado total de la entrega
    public record JudgeResult(
        String veredicto,        // ACEPTADO, ERROR_COMPILACION, ERROR_EJECUCION, TIEMPO_LIMITE
        int porcentaje,          // % de casos pasados
        String mensajeError,
        List<ResultadoCaso> resultados
    ) {}

    public JudgeResult evaluar(String codigo, String lenguaje,
                                List<CasoPrueba> casos,
                                int idEntrega) throws Exception {

        int languageId = lenguaje.equalsIgnoreCase("Python") ? langPython : langCpp;

        List<ResultadoCaso> resultados = new ArrayList<>();
        int casosCorrectos = 0;
        String primerError = null;
        String veredicto = "ACEPTADO";

        for (CasoPrueba caso : casos) {

            EjecucionResult res = ejecutar(codigo, languageId,
                caso.getEntrada(), caso.getSalidaEsperada());

            ResultadoCaso rc = new ResultadoCaso();
            rc.setIdEntrega(idEntrega);
            rc.setIdCaso(caso.getIdCaso());
            rc.setCorrecto(res.correcto());
            rc.setSalidaObtenida(res.salidaObtenida());
            rc.setTiempoEjecucion(res.tiempoEjecucion());
            rc.setEntrada(caso.getEntrada());
            rc.setSalidaEsperada(caso.getSalidaEsperada());

            resultados.add(rc);

            if (res.correcto()) {
                casosCorrectos++;
            } else {
                if (primerError == null) {
                    primerError = res.error();
                    // Determinar tipo de error
                    if (res.error() != null) {
                        if (res.error().contains("Time Limit"))
                            veredicto = "TIEMPO_LIMITE";
                        else if (res.error().contains("Compilation"))
                            veredicto = "ERROR_COMPILACION";
                        else
                            veredicto = "ERROR_EJECUCION";
                    } else {
                        veredicto = "INCORRECTO";
                    }
                }
            }
        }

        int porcentaje = casos.isEmpty() ? 0 :
            (casosCorrectos * 100) / casos.size();

        if (casosCorrectos == casos.size()) veredicto = "ACEPTADO";

        return new JudgeResult(veredicto, porcentaje, primerError, resultados);
    }

    private EjecucionResult ejecutar(String codigo, int languageId,
                                      String stdin, String expectedOutput)
            throws Exception {

        // 1. Enviar submission
        String body = mapper.writeValueAsString(new java.util.HashMap<>() {{
            put("source_code", codigo);
            put("language_id", languageId);
            put("stdin", stdin);
            put("expected_output", expectedOutput);
        }});

        HttpRequest submitRequest = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl + "/submissions?base64_encoded=false&wait=true"))
            .header("Content-Type", "application/json")
            .header("X-RapidAPI-Key", apiKey)
            .header("X-RapidAPI-Host", apiHost)
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = httpClient.send(
            submitRequest, HttpResponse.BodyHandlers.ofString()
        );

        JsonNode json = mapper.readTree(response.body());

        // 2. Parsear resultado
        int statusId = json.path("status").path("id").asInt();
        String stdout = json.path("stdout").asText("").trim();
        float tiempo = (float) json.path("time").asDouble(0);
        String stderr = json.path("stderr").asText(null);
        String compileOutput = json.path("compile_output").asText(null);

        // Status IDs de Judge0:
        // 3 = Accepted, 4 = Wrong Answer, 5 = Time Limit,
        // 6 = Compilation Error, 11-14 = Runtime errors
        boolean correcto = (statusId == 3);

        String error = null;
        if (statusId == 5) error = "Time Limit Exceeded";
        else if (statusId == 6) error = "Compilation Error: " + compileOutput;
        else if (statusId >= 7) error = "Runtime Error: " + stderr;
        else if (statusId == 4) error = null; // Wrong Answer, no es error

        return new EjecucionResult(correcto, stdout, tiempo, error);
    }
}