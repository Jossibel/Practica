package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.tda.list.queue.Queque;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class HistorialTransacciones {
    private Queque<Transacciones> historial;
    private static final String filePath = "historial_transacciones.json";
    private Gson gson;

    public HistorialTransacciones(int capacidad) {
        this.historial = new Queque<>(capacidad);
        this.gson = new Gson();
        cargarHistorial(); // Cargamos historial desde JSON al iniciar
    }

    public void agregarTransaccion(Transacciones transaccion) {
        try {
            historial.queque(transaccion);
            guardarHistorial(); // Guardamos en JSON después de cada transacción
        } catch (Exception e) {
            System.out.println("Error al agregar transacción: " + e.getMessage());
        }
    }

    private void guardarHistorial() {
        try (FileWriter w = new FileWriter(filePath)) {
            Transacciones[] listaTransacciones = historial.toArray(); // Usar el método toArray de Queque
            System.out.println("Lista de transacciones:");
            for (Transacciones transaccion : listaTransacciones) {
                System.out.println(transaccion);
            }

            gson.toJson(listaTransacciones, w);
        } catch (IOException e) {
            System.out.println("Error al guardar historial en JSON: " + e.getMessage());
        }
    }

    private void cargarHistorial() {    // Cargar historial desde JSON
        try (FileReader reader = new FileReader(filePath)) {
            Type tipoListaTransacciones = new TypeToken<Transacciones[]>() {}.getType();
            Transacciones[] listaTransacciones = gson.fromJson(reader, tipoListaTransacciones);
            if (listaTransacciones != null) {
                for (Transacciones transaccion : listaTransacciones) {
                    historial.queque(transaccion); // Agregar cada transacción a la cola
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar el historial desde JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al cargar el historial en la cola: " + e.getMessage());
        }
    }

    public void mostrarHistorial() {
        historial.print();
    }
}
