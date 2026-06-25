package pchy.pchy.models;

public class ResultadoCaso {

    private int idResultado;
    private int idEntrega;
    private int idCaso;
    private boolean correcto;
    private String salidaObtenida;
    private float tiempoEjecucion;
    private String entrada;
    private String salidaEsperada;

    public ResultadoCaso() {
    }

    public int getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(int idResultado) {
        this.idResultado = idResultado;
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public int getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }

    public boolean isCorrecto() {
        return correcto;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }

    public String getSalidaObtenida() {
        return salidaObtenida;
    }

    public void setSalidaObtenida(String salidaObtenida) {
        this.salidaObtenida = salidaObtenida;
    }

    public float getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(float tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSalidaEsperada() {
        return salidaEsperada;
    }

    public void setSalidaEsperada(String salidaEsperada) {
        this.salidaEsperada = salidaEsperada;
    }
}