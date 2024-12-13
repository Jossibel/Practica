package controller.Dao.servicies;
import controller.Dao.GeneradorDao;
import controller.tda.list.LinkedList;
import models.Generador;

public class GeneradorServicies {
    private GeneradorDao obj;
    public GeneradorServicies(){ //Constructor de la clase
        obj = new GeneradorDao(); //Instancia un objeto de la clase GeneradorDao
    }
    public Boolean update() throws Exception{ //Guarda la variable generador en la lista de objetos
        return obj.update(); //Invoca el método save() de la clase GeneradorDao
    }

    public Boolean save() throws Exception{ //Guarda la variable generador en la lista de objetos
        return obj.save(); //Invoca el método save() de la clase GeneradorDao
    }

    public LinkedList<Generador> listAll(){ //Obtiene la lista de objetos LinkedList<T>
        return obj.getlistAll(); //Invoca el método getlistAll() de la clase GeneradorDao

    }

    public Generador getGenerador(){ //Obtiene la generador
        return obj.getGenerador(); //Invoca el método getGenerador() de la clase GeneradorDao
    }

    public void setGenerador( Generador generador){ //Recibe un objeto Generador
        obj.setGenerador(generador); //Invoca el método setGenerador() de la clase GeneradorDao y envía el objeto Generador
    }

    public Generador get(Integer id) throws Exception{ //Obtiene un objeto Generador por su id
        return obj.get(id); //Invoca el método get() de la clase GeneradorDao y envía el id
    }

    public Boolean delete(int index) throws Exception{ //Elimina un objeto Generador por su índice
        return obj.delete(index); //Invoca el método delete() de la clase GeneradorDao y envía el índice
    }
    
    
}
