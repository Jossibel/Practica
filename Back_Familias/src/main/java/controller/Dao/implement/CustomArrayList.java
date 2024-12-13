package controller.Dao.implement;

public class CustomArrayList<T> {
    
    private Object[] elements; 
    private int size; 

   
    public CustomArrayList() {
        elements = new Object[10];
        size = 0;
    }

    
    public void addLast(T element) {
        ensureCapacity(); 
        elements[size++] = element;
    }

    
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        return (T) elements[index];
    }

   
    public void edit(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        elements[index] = element;
    }

  
    public void delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;}

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] array = (T[]) new Object[size]; // Crear un nuevo array del tamaño correcto
        for (int i = 0; i < size; i++) {
            array[i] = (T) elements[i];
        }
        return array;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[size * 2]; // Duplicar el tamaño del array
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i]; // Copiar elementos al nuevo array
            }
            elements = newElements;
        }
    }


}
