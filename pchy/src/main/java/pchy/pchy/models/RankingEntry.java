package pchy.pchy.models;

public class RankingEntry {

    private int posicion;
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String fotoPerfil;
    private int puntaje;
    private String medalla; // BRONCE, PLATA, ORO, DIAMANTE, null

    public RankingEntry() {}

    // Getters y setters
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }

    public String getMedalla() { return medalla; }
    public void setMedalla(String medalla) { this.medalla = medalla; }
}