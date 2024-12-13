package models;
import java.time.LocalDateTime;

public class Transacciones {
    private String tipoTransaccion;
    private String fecha;
    private Integer id;

    public Transacciones(String tipoTransaccion, Integer id) {
        this.tipoTransaccion = tipoTransaccion;
        this.fecha = LocalDateTime.now().toString();
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "tipoTransaccion='" + tipoTransaccion + '\'' +
                ", fecha=" + fecha +
                ", id=" + id + 
                "\n}";
    }
}
