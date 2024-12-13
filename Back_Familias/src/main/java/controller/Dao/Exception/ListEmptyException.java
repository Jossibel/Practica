package controller.Dao.Exception;

public class ListEmptyException extends Exception {
    public ListEmptyException(){

    }
    public ListEmptyException(String msg){
        super(msg);
    }
}
