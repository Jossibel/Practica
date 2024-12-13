package controller.tda.list;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public class LinkedList<E> {
    private Node<E> header;
    private Node<E> last;
    private Integer size;

    public LinkedList() {
        this.header = null;
        this.last = null;
        this.size = 0;
    }

    public Boolean isEmpty() {
        return (this.header == null || this.size == 0);
    }

    protected void addHeader(E dato) {
        Node<E> help;
        if (isEmpty()) {
            help = new Node<>(dato);
            this.header = help;
            this.last = help;
        } else {
            Node<E> healpHeader = this.header;
            help = new Node<>(dato, healpHeader);
            this.header = help;
        }
        this.size++;
    }

    private void addLast(E info) {
        Node<E> help;
        if (isEmpty()) {
            help = new Node<>(info);
            this.header = help;
            this.last = help;
        } else {
            help = new Node<>(info, null);
            last.setNext(help);
            last = help;
        }
        this.size++;
    }

    public void add(E info) {
        addLast(info);
    }

    private Node<E> getNode(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, List empty");
        } else if (index.intValue() < 0 || index.intValue() >= this.size) {
            throw new IndexOutOfBoundsException("Error, fuera de rango");
        } else if (index.intValue() == (this.size - 1)) {
            return last;
        } else {
            Node<E> search = header;
            int cont = 0;
            while (cont < index.intValue()) {
                cont++;
                search = search.getNext();
            }
            return search;
        }
    }

    public E getFirst() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, lista vacia");
        }
        return header.getInfo();
    }

    public E getLast() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, Lista Vacia");
        }
        return last.getInfo();
    }

    public E get(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, list empty");
        } else if (index.intValue() < 0 || index.intValue() >= this.size.intValue()) {
            throw new IndexOutOfBoundsException("Error, fuera de rango");
        } else if (index.intValue() == 0) {
            return header.getInfo();
        } else if (index.intValue() == (this.size - 1)) {
            return last.getInfo();
        } else {
            Node<E> search = header;
            int cont = 0;
            while (cont < index.intValue()) {
                cont++;
                search = search.getNext();
            }
            return search.getInfo();
        }
    }

    public void add(E info, Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty() || index.intValue() == 0) {
            addHeader(info);
        } else if (index.intValue() == this.size.intValue()) {
            addLast(info);
        } else {
            Node<E> search_preview = getNode(index - 1);
            Node<E> search = getNode(index);
            Node<E> help = new Node<>(info, search);
            search_preview.setNext(help);
            this.size++;
        }
    }

    public void update(E info, Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista está vacía");
        }
        
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Índice fuera de límites");
        }
        Node<E> help = getNode(index);
        help.setInfo(info);
    }

    public void reset() {
        this.header = null;
        this.last = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("List data");
        try {
            Node<E> help = header;
            while (help != null) {
                sb.append(help.getInfo()).append(" ->");
                help = help.getNext();
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    public Integer getSize() {
        return this.size;
    }

    public Node<E> getHeader() {
        return header;
    }

    public E[] toArray() {
        E[] matrix = null;
        if (!isEmpty()) {
            Class clazz = header.getInfo().getClass();
            matrix = (E[]) java.lang.reflect.Array.newInstance(clazz, size);
            Node<E> aux = header;
            for (int i = 0; i < this.size; i++) {
                matrix[i] = aux.getInfo();
                aux = aux.getNext();
            }
        }
        return matrix;
    }

    public LinkedList<E> toList(E[] matrix) {
        reset();
        for (int i = 0; i < matrix.length; i++) {
            this.add(matrix[i]);
        }
        return this;
    }

    public int getLength() {
        return this.size.intValue();
    }

    protected void removeLast() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, no puede eliminar datos de una lista vacia.");
        } else {
            Node<E> nodo_last = getNode((getLength() - 2));
            nodo_last.setNext(null);
            this.last = nodo_last;
            this.size--;
        }
    }

    public void removeFirst() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, no puede eliminar datos de una lista vacia.");
        } else {
            Node<E> help = this.header;
            Node<E> nextHeader = help.getNext();
            this.header = nextHeader;
            this.size--;
        }
    }

    public void remove(int index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("Lista vacia, no puede eliminar elementos");
        } else if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Índice fuera de límites: " + index);
        } else if (index == 0) {
            removeFirst();
        } else if (index == (this.size - 1)) {
            removeLast();
        } else {
            Node<E> nodoDeath = getNode(index);
            Node<E> previousNode = getNode(index - 1);
            previousNode.setNext(nodoDeath.getNext());
            this.size--;
        }
    }

    public E deleteFirst() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, no puede eliminar datos de una lista vacia.");
        } else {
            E element = header.getInfo();
            Node<E> aux = header.getNext();
            header = aux;
            if (size.intValue() == 1) {
                last = null;
            }
            size--;
            return element;
        }
    }

    public E deleteLast() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("Error, no puede eliminar datos de una lista vacia.");
        } else {
            E element = last.getInfo();
            Node<E> aux = getNode(size - 2);
            if (aux == null) {
                last = null;
                if (size == 2) {
                    last = header;
                } else {
                    header = null;
                }
            } else {
                last = null;
                last = aux;
                last.setNext(null);
            }
            size--;
            return element;
        }
    }

    public E delete(Integer post) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("Lista vacia, no puede eliminar elementos");
        } else if (post < 0 || post >= this.size) {
            throw new IndexOutOfBoundsException("Índice fuera de límites: " + post);
        } else if (post == 0) {
            return deleteFirst();
        } else if (post == (this.size - 1)) {
            return deleteLast();
        } else {
            Node<E> preview = getNode(post - 1);
            Node<E> actually = getNode(post);
            E element = actually.getInfo();
            Node<E> next = actually.getNext();
            preview.setNext(next);
            size--;
            return element;
        }
    }

    public boolean remove(E element) {
        if (isEmpty())
            return false;

        if (header.getInfo().equals(element)) {
            header = header.getNext();
            size--;
            return true;
        }

        Node<E> current = header;
        while (current.getNext() != null) {
            if (current.getNext().getInfo().equals(element)) {
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    // Aqui se listan ordenar y buscar///

    // Improved Comparison Methods
    private Comparator<E> getComparator(Integer type) {
        return (a, b) -> {
            if (a == null || b == null)
                return 0;

            if (a instanceof Comparable && b instanceof Comparable) {
                // Use natural ordering
                return type == 1 ? ((Comparable<E>) a).compareTo(b) * -1 : ((Comparable<E>) a).compareTo(b);
            }

            // Fallback for non-comparable objects
            return type == 1 ? a.toString().compareTo(b.toString()) * -1 : a.toString().compareTo(b.toString());
        };
    }

    // Improved Attribute-based Comparator
    private Comparator<E> getAttributeComparator(String attribute, Integer type) throws Exception {
        return (a, b) -> {
            try {
                Object valueA = getAttributeValue(a, attribute);
                Object valueB = getAttributeValue(b, attribute);

                if (valueA == null || valueB == null)
                    return 0;

                if (valueA instanceof Comparable && valueB instanceof Comparable) {
                    @SuppressWarnings("unchecked")
                    int comparison = ((Comparable) valueA).compareTo(valueB);
                    return type == 1 ? comparison * -1 : comparison;
                }

                // Fallback to string comparison
                return type == 1 ? valueA.toString().compareTo(valueB.toString()) * -1
                        : valueA.toString().compareTo(valueB.toString());
            } catch (Exception e) {
                throw new RuntimeException("Comparison error", e);
            }
        };
    }

    // Optimized Reflection Method
    private Object getAttributeValue(E obj, String attribute) throws Exception {
        String methodName = "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);

        // Try current class methods first
        try {
            Method method = obj.getClass().getMethod(methodName);
            return method.invoke(obj);
        } catch (NoSuchMethodException e) {
            // If not found, try superclass methods
            Method method = obj.getClass().getSuperclass().getMethod(methodName);
            return method.invoke(obj);
        }
    }

    // Optimized QuickSort with improved pivot selection
    public LinkedList<E> quicksort(int orderType) throws Exception {
        if (isEmpty())
            return this;

        E[] lista = toArray();
        quicksortImproved(lista, 0, lista.length - 1, getComparator(orderType));
        return toList(lista);
    }

    private void quicksortImproved(E[] arr, int low, int high, Comparator<E> comparator) {
        while (low < high) {
            // Use median-of-three for pivot selection
            int mid = low + (high - low) / 2;
            medianOfThree(arr, low, mid, high, comparator);

            int pivotIndex = partitionImproved(arr, low, high, comparator);

            // Use tail recursion optimization
            if (pivotIndex - low < high - pivotIndex) {
                quicksortImproved(arr, low, pivotIndex - 1, comparator);
                low = pivotIndex + 1;
            } else {
                quicksortImproved(arr, pivotIndex + 1, high, comparator);
                high = pivotIndex - 1;
            }
        }
    }

    private void medianOfThree(E[] arr, int low, int mid, int high, Comparator<E> comparator) {
        if (comparator.compare(arr[mid], arr[low]) < 0) {
            swap(arr, low, mid);
        }
        if (comparator.compare(arr[high], arr[low]) < 0) {
            swap(arr, low, high);
        }
        if (comparator.compare(arr[mid], arr[high]) < 0) {
            swap(arr, mid, high);
        }
    }

    private int partitionImproved(E[] arr, int low, int high, Comparator<E> comparator) {
        E pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(arr[j], pivot) <= 0) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(E[] arr, int i, int j) {
        E temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Attribute-based QuickSort
    public LinkedList<E> quicksort(String attribute, Integer type) throws Exception {
        if (isEmpty())
            return this;

        E[] lista = toArray();
        quicksortImproved(lista, 0, lista.length - 1, getAttributeComparator(attribute, type));
        return toList(lista);
    }

    // Optimized MergeSort
    public LinkedList<E> mergesort(int orderType) throws Exception {
        if (isEmpty())
            return this;

        E[] lista = toArray();
        mergesortImproved(lista, 0, lista.length - 1, getComparator(orderType));
        return toList(lista);
    }

    private void mergesortImproved(E[] arr, int left, int right, Comparator<E> comparator) {
        if (left >= right)
            return;

        int mid = left + (right - left) / 2;
        mergesortImproved(arr, left, mid, comparator);
        mergesortImproved(arr, mid + 1, right, comparator);

        merge(arr, left, mid, right, comparator);
    }

    private void merge(E[] arr, int left, int mid, int right, Comparator<E> comparator) {
        // Create a temporary array
        E[] temp = Arrays.copyOfRange(arr, left, right + 1);

        int i = 0, j = mid - left + 1, k = left;

        while (i <= mid - left && j <= right - left) {
            if (comparator.compare(temp[i], temp[j]) <= 0) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }

        // Copy remaining elements
        while (i <= mid - left) {
            arr[k++] = temp[i++];
        }
        while (j <= right - left) {
            arr[k++] = temp[j++];
        }
    }

    // Attribute-based MergeSort
    public LinkedList<E> mergesort(String attribute, Integer type) throws Exception {
        if (isEmpty())
            return this;

        E[] lista = toArray();
        mergesortImproved(lista, 0, lista.length - 1, getAttributeComparator(attribute, type));
        return toList(lista);
    }

    // Optimized ShellSort
    public LinkedList<E> shellSort(int orderType) throws Exception {
        if (isEmpty())
            return this;

        E[] lista = toArray();
        shellsortImproved(lista, getComparator(orderType));
        return toList(lista);
    }

    private void shellsortImproved(E[] arr, Comparator<E> comparator) {
        int n = arr.length;

        // Optimized gap sequence (using Knuth sequence)
        int gap = 1;
        while (gap < n / 3) {
            gap = 3 * gap + 1;
        }

        while (gap > 0) {
            for (int i = gap; i < n; i++) {
                E temp = arr[i];
                int j = i;

                while (j >= gap && comparator.compare(arr[j - gap], temp) > 0) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
            gap /= 3;
        }
    }

    // Attribute-based ShellSort
    public LinkedList<E> shellSort(String attribute, Integer type) throws Exception {
        if (isEmpty())
            return this;

        E[] lista = toArray();
        shellsortImproved(lista, getAttributeComparator(attribute, type));
        return toList(lista);
    }

    public void clear() {

        Object head = null;
        size = 0;

    }
}