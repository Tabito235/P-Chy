package pchy.pchy.models;

import java.sql.Timestamp;

public class Clase {

    private int idClase;
    private String nombre;
    private String descripcion;
    private String codigo;
    private int idProfesorCreador;
    private boolean activa;
    private Timestamp fechaCreacion;

    // Esto solo ayuda a la vista, suma y esas cosas, no lo cuento en la BD, pero
    // sirve chido (creo ajsjasajja... ay nunca comento y ahora que si nomas pongo
    // esto)
    private int totalAlumnos;
    private int totalCompetencias;

    private String nombreProfesor;

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public Clase() {
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdProfesorCreador() {
        return idProfesorCreador;
    }

    public void setIdProfesorCreador(int idProfesorCreador) {
        this.idProfesorCreador = idProfesorCreador;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getTotalAlumnos() {
        return totalAlumnos;
    }

    public void setTotalAlumnos(int totalAlumnos) {
        this.totalAlumnos = totalAlumnos;
    }

    public int getTotalCompetencias() {
        return totalCompetencias;
    }

    public void setTotalCompetencias(int totalCompetencias) {
        this.totalCompetencias = totalCompetencias;
    }
}