package pchy.pchy.models;

public class Niveles {

    private int idNivel;
    private int idCompetencia;
    private int numeroNivel;
    private String titulo;
    private int tiempoLimite;
    private int posicion;
    private boolean activo;
    private int problemasParaDesbloquear;

    public Niveles() {
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public int getNumeroNivel() {
        return numeroNivel;
    }

    public void setNumeroNivel(int numeroNivel) {
        this.numeroNivel = numeroNivel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getTiempoLimite() {
        return tiempoLimite;
    }

    public void setTiempoLimite(int tiempoLimite) {
        this.tiempoLimite = tiempoLimite;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getProblemasParaDesbloquear() {
        return problemasParaDesbloquear;
    }

    public void setProblemasParaDesbloquear(int p) {
        this.problemasParaDesbloquear = p;
    }
}