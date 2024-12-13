package models;

public class Familia {
    private Integer id;
    private String canton;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int integrantes;
    private boolean tieneGenerador;

    public Familia() {
    }
    
    
    public Familia(Integer id, String canton, String apellidoPaterno, String apellidoMaterno , int integrantes, boolean tieneGenerador) {
        this.id = id;
        this.canton = canton;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.integrantes = integrantes;
        this.tieneGenerador = tieneGenerador;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public int getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(int integrantes) {
        this.integrantes = integrantes;
    }

    public String getCanton() {
        return canton;
    }
    
    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public boolean getTieneGenerador() {
        return tieneGenerador;
    }

    public void setTieneGenerador(boolean tieneGenerador) {
        this.tieneGenerador = tieneGenerador;
    }

}
