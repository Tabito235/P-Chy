package pchy.pchy.models;

import java.sql.Timestamp;

public class UsuarioClase {

    private int idUsuario;
    private int idClase;
    private Timestamp fechaUnion;
    private String estado;

    public UsuarioClase() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public Timestamp getFechaUnion() {
        return fechaUnion;
    }

    public void setFechaUnion(Timestamp fechaUnion) {
        this.fechaUnion = fechaUnion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
