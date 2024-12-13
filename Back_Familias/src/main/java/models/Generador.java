package models;

public class Generador {
    private Integer id;
    private float costo;
    private float consumoXHora;
    private float energiaGenerada;
    private String uso;

    public Generador() {
    }
    
    
    public Generador(Integer id, int costo, int consumoXHora, int energiaGenerada) {
        this.id = id;
        this.costo = costo;
        this.consumoXHora = consumoXHora;
        this.energiaGenerada = energiaGenerada;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public float getConsumoXHora() {
        return consumoXHora;
    }

    public void setConsumoXHora(float consumoXHora) {
        this.consumoXHora = consumoXHora;
    }

    public float getEnergiaGenerada() {
        return energiaGenerada;
    }

    public void setEnergiaGenerada(float energiaGenerada) {
        this.energiaGenerada = energiaGenerada;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

}
