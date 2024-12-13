package controller.Dao;

import models.Generador;
import controller.Dao.implement.AdapterDao;
import controller.tda.list.LinkedList;

public class GeneradorDao extends AdapterDao<Generador> {
    private Generador generador = new Generador(); 
    private LinkedList listAll;
    
    public GeneradorDao(){
        super(Generador.class);
    }
    public Generador getGenerador(){ //Obtiene la generador
        if (generador == null) {
            generador = new Generador(); //En caso de que la generador sea nula, crea una nueva instancia de Generador
        }
        return this.generador; //Devuelve la generador
    }

    public void setGenerador(Generador generador){ //Establece la generador con un objeto Generador
        this.generador = generador; //Asigna el objeto Generador a la variable generador
    }

    public LinkedList getlistAll(){  //Obtiene la lista de objetos
        if (listAll == null) { //Si la lista es nula 
            this.listAll = listAll(); //Invoca el método listAll() para obtener la lista de objetos
        }
        return listAll; //Devuelve la lista de objetos de la variable listAll
    }
    public Boolean save() throws Exception{ //Guarda la variable generador en la lista de objetos
        Integer id = getlistAll().getSize()+1; //Obtiene el tamaño de la lista y le suma 1 para asignar un nuevo id
        generador.setId(id); //Asigna el id a generador
        this.persist(this.generador); //Guarda la generador en la lista de objetos LinkedList y en el archivo JSON
        this.listAll = listAll(); //Actualiza la lista de objetos
        return true; //Retorna verdadero si se guardó correctamente
    }

    public Boolean update() throws Exception{ //Actualiza el nodo Generador en la lista de objetos
        this.merge(getGenerador(), getGenerador().getId() -1);  //Envia la generador a actualizar con su index 
        this.listAll = listAll();  //Actualiza la lista de objetos
        return true; 
    }

    public Boolean delete(int index) throws Exception { //Elimina un objeto Familia por su índice
        this.supreme(index);
        this.listAll = listAll(); // Actualiza la lista de objetos
        return true; // Retorna verdadero si se eliminó correctamente
    }
    

}

