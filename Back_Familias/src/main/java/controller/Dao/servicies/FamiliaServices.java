package controller.Dao.servicies;

import java.util.function.Predicate;

import controller.Dao.FamiliaDao;
import controller.tda.list.LinkedList;
import models.Familia;

public class FamiliaServices {
    private final FamiliaDao family_dao;

    public FamiliaServices() {
        this.family_dao = new FamiliaDao();
    }

    // CRUD methods with enhanced readability
    public Boolean updateFamily() throws Exception {
        return family_dao.update();
    }

    public Boolean saveFamily() throws Exception {
        return family_dao.save();
    }

    public LinkedList<Familia> getAllFamilies() {
        return family_dao.getFamilyList();
    }

    public Familia getCurrentFamily() {
        return family_dao.getCurrentFamilia();
    }

    public void setCurrentFamily(Familia family) {
        family_dao.setCurrentFamilia(family);
    }

    public Familia getFamilyById(Integer id) throws Exception {
        return family_dao.get(id);
    }

    public Boolean deleteFamily(int index) throws Exception {
        return family_dao.delete(index);
    }

    //Aqui se listan ordenar y buscar///
    // Generic search method
    public LinkedList<Familia> searchFamilies(Predicate<Familia> condition) {
        return family_dao.search(condition);
    }

    // Specific search methods with snake_case
    public LinkedList<Familia> searchByDistrict(String district) {
        return family_dao.searchByDistrict(district);
    }

    public LinkedList<Familia> searchByPaternalLastName(String paternal_last_name) {
        return family_dao.searchByPaternalLastName(paternal_last_name);
    }

    public LinkedList<Familia> searchByMaternalLastName(String maternal_last_name) {
        return family_dao.searchByMaternalLastName(maternal_last_name);
    }

    public LinkedList<Familia> searchByMemberCount(int member_count) {
        return family_dao.searchByMemberCount(member_count);
    }

    public LinkedList<Familia> searchByGenerator(boolean has_generator) {
        return family_dao.searchByGenerator(has_generator);
    }

    // Sorting methods with snake_case
    public LinkedList<Familia> sortByPaternalLastName(boolean ascending) {
        return family_dao.sortByPaternalLastName(ascending);
    }

    public LinkedList<Familia> sortByMaternalLastName(boolean ascending) {
        return family_dao.sortByMaternalLastName(ascending);
    }

    public LinkedList<Familia> sortByDistrict(boolean ascending) {
        return family_dao.sortByDistrict(ascending);
    }

    public LinkedList<Familia> sortByMemberCount(boolean ascending) {
        return family_dao.sortByMemberCount(ascending);
    }

    public LinkedList<Familia> sortByGenerator(boolean ascending) {
        return family_dao.sortByGenerator(ascending);
    }

    // Counting methods
    public int countFamiliesWithGenerator() {
        return family_dao.countFamiliesWithGenerator();
    }

    // Búsqueda Lineal
    public LinkedList<Familia> searchLinear(String field, String value) {
        LinkedList<Familia> results = new LinkedList<>();
        Familia[] families = getAllFamilies().toArray();

        for (Familia familia : families) {
            if (matchesField(familia, field, value)) {
                results.add(familia);
            }
        }
        return results;
    }

    // Búsqueda Binaria (requiere que la lista esté ordenada por el campo de
    // búsqueda)
    public LinkedList<Familia> searchBinary(String field, String value) {
        LinkedList<Familia> results = new LinkedList<>();
        Familia[] families = getAllFamilies().toArray();

        // Primero ordenamos por el campo especificado
        sortWithQuickSort(field, true);

        int left = 0;
        int right = families.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            String fieldValue = getFieldValue(families[mid], field);

            if (fieldValue.equalsIgnoreCase(value)) {
                results.add(families[mid]);
                // Buscar elementos adyacentes que también coincidan
                int i = mid - 1;
                while (i >= 0 && getFieldValue(families[i], field).equalsIgnoreCase(value)) {
                    results.add(families[i--]);
                }
                i = mid + 1;
                while (i < families.length && getFieldValue(families[i], field).equalsIgnoreCase(value)) {
                    results.add(families[i++]);
                }
                break;
            }

            if (fieldValue.compareToIgnoreCase(value) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return results;
    }

    // Implementaciones de algoritmos de ordenamiento
    public LinkedList<Familia> sortWithShellSort(String field, boolean ascending) {
        Familia[] arr = getAllFamilies().toArray();
        int n = arr.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Familia temp = arr[i];
                int j;
                for (j = i; j >= gap && compare(arr[j - gap], temp, field, ascending) > 0; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }

        return arrayToLinkedList(arr);
    }

    public LinkedList<Familia> sortWithMergeSort(String field, boolean ascending) {
        Familia[] arr = getAllFamilies().toArray();
        mergeSortHelper(arr, 0, arr.length - 1, field, ascending);
        return arrayToLinkedList(arr);
    }

    public LinkedList<Familia> sortWithQuickSort(String field, boolean ascending) {
        Familia[] arr = getAllFamilies().toArray();
        quickSortHelper(arr, 0, arr.length - 1, field, ascending);
        return arrayToLinkedList(arr);
    }

    // Métodos auxiliares
    private void mergeSortHelper(Familia[] arr, int left, int right, String field, boolean ascending) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortHelper(arr, left, mid, field, ascending);
            mergeSortHelper(arr, mid + 1, right, field, ascending);
            merge(arr, left, mid, right, field, ascending);
        }
    }

    private void merge(Familia[] arr, int left, int mid, int right, String field, boolean ascending) {
        // Implementación del merge
        // ... (implementar la lógica de fusión del mergesort)
    }

    private void quickSortHelper(Familia[] arr, int low, int high, String field, boolean ascending) {
        if (low < high) {
            int pi = partition(arr, low, high, field, ascending);
            quickSortHelper(arr, low, pi - 1, field, ascending);
            quickSortHelper(arr, pi + 1, high, field, ascending);
        }
    }

    private int partition(Familia[] arr, int low, int high, String field, boolean ascending) {
        // Implementación del particionamiento
        // ... (implementar la lógica de particionamiento del quicksort)
        return 0;
    }

    private int compare(Familia f1, Familia f2, String field, boolean ascending) {
        int result = compareField(f1, f2, field);
        return ascending ? result : -result;
    }

    private int compareField(Familia f1, Familia f2, String field) {
        switch (field.toLowerCase()) {
            case "canton":
                return f1.getCanton().compareToIgnoreCase(f2.getCanton());
            case "apellidopaterno":
                return f1.getApellidoPaterno().compareToIgnoreCase(f2.getApellidoPaterno());
            case "apellidomaterno":
                return f1.getApellidoMaterno().compareToIgnoreCase(f2.getApellidoMaterno());
            case "integrantes":
                return Integer.compare(f1.getIntegrantes(), f2.getIntegrantes());
            case "generador":
                return Boolean.compare(f1.getTieneGenerador(), f2.getTieneGenerador());
            default:
                throw new IllegalArgumentException("Campo no válido: " + field);
        }
    }

    private String getFieldValue(Familia familia, String field) {
        switch (field.toLowerCase()) {
            case "canton":
                return familia.getCanton();
            case "apellidopaterno":
                return familia.getApellidoPaterno();
            case "apellidomaterno":
                return familia.getApellidoMaterno();
            case "integrantes":
                return String.valueOf(familia.getIntegrantes());
            case "generador":
                return String.valueOf(familia.getTieneGenerador());
            default:
                throw new IllegalArgumentException("Campo no válido: " + field);
        }
    }

    private boolean matchesField(Familia familia, String field, String value) {
        return getFieldValue(familia, field).equalsIgnoreCase(value);
    }

    private LinkedList<Familia> arrayToLinkedList(Familia[] arr) {
        LinkedList<Familia> list = new LinkedList<>();
        for (Familia f : arr) {
            list.add(f);
        }
        return list;
    }
}