package pchy.pchy.models;

import java.sql.Timestamp;

public class Entrega {

    private int idEntrega;
    private int idUsuario;
    private int idNivel;
    private int idProblema;
    private String archivoCodigo;
    private String archivoCaptura;
    private int puntaje;
    private String estado;
    private String resultadoJudge;
    private int porcentajeCasos;
    private String mensajeError;
    private Timestamp fechaEntrega;

    public Entrega() {
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public int getIdProblema() {
        return idProblema;
    }

    public void setIdProblema(int idProblema) {
        this.idProblema = idProblema;
    }

    public String getArchivoCodigo() {
        return archivoCodigo;
    }

    public void setArchivoCodigo(String archivoCodigo) {
        this.archivoCodigo = archivoCodigo;
    }

    public String getArchivoCaptura() {
        return archivoCaptura;
    }

    public void setArchivoCaptura(String archivoCaptura) {
        this.archivoCaptura = archivoCaptura;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResultadoJudge() {
        return resultadoJudge;
    }

    public void setResultadoJudge(String resultadoJudge) {
        this.resultadoJudge = resultadoJudge;
    }

    public int getPorcentajeCasos() {
        return porcentajeCasos;
    }

    public void setPorcentajeCasos(int porcentajeCasos) {
        this.porcentajeCasos = porcentajeCasos;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public Timestamp getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}