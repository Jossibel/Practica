package controller.tda.list.Array;

import java.util.Arrays;

public class CustomArraylist<T> {
    private Object[] elements;
    private int size;

    public CustomArraylist() {
        elements = new Object[10]; 
        size = 0;
    }

    public void addLast(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    public T get(int index) {
        return (T) elements[index];
    }

    public void edit(int index, T element) {
        if (index >= 0 && index < size) {
            elements[index] = element;
        }
    }

    public void delete(int index) {
        if (index >= 0 && index < size) {
            for (int i = index; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
            elements[--size] = null; 
        }
    }

    public int size() {
        return size;
    }

   
    public T[] toArray() {
        return Arrays.copyOf(elements, size, (Class<T[]>) elements.getClass()); 
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, size * 2); 
    }
}


}
