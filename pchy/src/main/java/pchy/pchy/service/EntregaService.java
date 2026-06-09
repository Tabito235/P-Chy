package pchy.pchy.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pchy.pchy.models.CasoPrueba;
import pchy.pchy.models.Entrega;
import pchy.pchy.models.ResultadoCaso;
import pchy.pchy.repository.CasoPruebaRepository;
import pchy.pchy.repository.EntregaRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class EntregaService {

    @Autowired private EntregaRepository  entregaRepository;
    @Autowired private CasoPruebaRepository casoPruebaRepository;
    @Autowired private JudgeService       judgeService;
    @Autowired private Cloudinary         cloudinary;

    public Entrega enviar(int idUsuario, int idNivel, int idProblema,
                          String lenguaje,
                          MultipartFile archivoCodigo,
                          MultipartFile archivoCaptura) throws Exception {

        // 1. Leer código fuente
        String codigo = new String(
            archivoCodigo.getBytes(), StandardCharsets.UTF_8
        );

        // 2. Subir captura a Cloudinary
        String urlCaptura = null;
        if (archivoCaptura != null && !archivoCaptura.isEmpty()) {
            Map resultado = cloudinary.uploader().upload(
                archivoCaptura.getBytes(),
                ObjectUtils.asMap(
                    "folder", "pchy/capturas",
                    "resource_type", "image"
                )
            );
            urlCaptura = (String) resultado.get("secure_url");
        }

        // 3. Subir código a Cloudinary (como raw)
        Map resCode = cloudinary.uploader().upload(
            archivoCodigo.getBytes(),
            ObjectUtils.asMap(
                "folder", "pchy/codigos",
                "resource_type", "raw",
                "public_id", "codigo_" + idUsuario + "_" + idProblema + "_" +
                             System.currentTimeMillis()
            )
        );
        String urlCodigo = (String) resCode.get("secure_url");

        // 4. Crear entrega en BD
        Entrega entrega = new Entrega();
        entrega.setIdUsuario(idUsuario);
        entrega.setIdNivel(idNivel);
        entrega.setIdProblema(idProblema);
        entrega.setArchivoCodigo(urlCodigo);
        entrega.setArchivoCaptura(urlCaptura);

        int idEntrega = entregaRepository.crear(entrega);
        entrega.setIdEntrega(idEntrega);

        // 5. Obtener casos de prueba
        List<CasoPrueba> casos = casoPruebaRepository.listarPorProblema(idProblema);

        if (casos.isEmpty()) {
            // Sin casos: marcar como REVISION para que el profesor revise
            entregaRepository.actualizarResultadoJudge(
                idEntrega, "SIN_CASOS", 0,
                "No hay casos de prueba definidos. Requiere revisión manual."
            );
            entrega.setResultadoJudge("SIN_CASOS");
            entrega.setPorcentajeCasos(0);
            return entrega;
        }

        // 6. Enviar a Judge0
        JudgeService.JudgeResult result =
            judgeService.evaluar(codigo, lenguaje, casos, idEntrega);

        // 7. Guardar resultado por caso
        for (ResultadoCaso rc : result.resultados()) {
            entregaRepository.guardarResultadoCaso(rc);
        }

        // 8. Actualizar entrega con resultado
        entregaRepository.actualizarResultadoJudge(
            idEntrega,
            result.veredicto(),
            result.porcentaje(),
            result.mensajeError()
        );

        entrega.setResultadoJudge(result.veredicto());
        entrega.setPorcentajeCasos(result.porcentaje());
        entrega.setMensajeError(result.mensajeError());

        return entrega;
    }

    public Entrega obtenerUltima(int idUsuario, int idProblema) {
        return entregaRepository.obtenerUltima(idUsuario, idProblema);
    }

    public List<Entrega> historial(int idUsuario, int idProblema) {
        return entregaRepository.listarPorProblema(idUsuario, idProblema);
    }

    public List<ResultadoCaso> resultadosCasos(int idEntrega) {
        return entregaRepository.listarResultadosCasos(idEntrega);
    }

    public boolean estaAprobado(int idUsuario, int idProblema) {
        return entregaRepository.estaAprobado(idUsuario, idProblema);
    }

    public int contarAprobadosEnNivel(int idUsuario, int idNivel) {
        return entregaRepository.contarAprobadosEnNivel(idUsuario, idNivel);
    }
}