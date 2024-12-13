package controller.Dao;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import controller.Dao.implement.AdapterDao;
import controller.tda.list.LinkedList;
import models.Familia;

public class FamiliaDao extends AdapterDao<Familia> {
    private Familia current_familia;
    private LinkedList<Familia> family_list;

    public FamiliaDao() {
        super(Familia.class);
        this.current_familia = new Familia();
    }

    public Familia getCurrentFamilia() {
        return this.current_familia;
    }

    public void setCurrentFamilia(Familia familia) {
        this.current_familia = familia;
    }

    public LinkedList<Familia> getFamilyList() {
        return (family_list == null) ? listAll() : family_list;
    }

    // Optimized CRUD methods with enhanced validation
    public Boolean save() throws Exception {
        validateFamiliaForSave();
        int new_id = getFamilyList().getSize() + 1;
        current_familia.setId(new_id);
        persist(current_familia);
        this.family_list = listAll();
        return true;
    }

    public Boolean update() throws Exception {
        validateFamiliaForUpdate();
        merge(getCurrentFamilia(), getCurrentFamilia().getId() - 1);
        this.family_list = listAll();
        return true;
    }

    public Boolean delete(int index) throws Exception {
        validateDeleteIndex(index);
        supreme(index);
        this.family_list = listAll();
        return true;
    }

    // Validation methods
    private void validateFamiliaForSave() {
        if (current_familia == null) {
            throw new IllegalArgumentException("Cannot save a null family");
        }
    }

    private void validateFamiliaForUpdate() {
        if (current_familia == null || current_familia.getId() == null) {
            throw new IllegalArgumentException("Cannot update a null or unidentified family");
        }
    }

    private void validateDeleteIndex(int index) {
        if (index < 0 || index >= getFamilyList().getSize()) {
            throw new IndexOutOfBoundsException("Invalid family index");
        }
    }

    //Aqui se listan ordenar y buscar///
    // Generic search method
    public LinkedList<Familia> search(Predicate<Familia> condition) {
        LinkedList<Familia> results = new LinkedList<>();
        Familia[] family_array = getFamilyList().toArray();

        for (Familia family : family_array) {
            if (condition.test(family)) {
                results.add(family);
            }
        }

        return results;
    }

    // Generic sorting method
    public LinkedList<Familia> sort(Comparator<Familia> comparator, boolean ascending) {
        if (getFamilyList().isEmpty()) {
            return getFamilyList();
        }

        Familia[] sorted_list = getFamilyList().toArray();
        Arrays.sort(sorted_list, ascending ? comparator : comparator.reversed());

        family_list.clear();
        for (Familia family : sorted_list) {
            family_list.add(family);
        }

        return family_list;
    }

    // Specific search methods
    public LinkedList<Familia> searchByDistrict(String district) {
        return search(f -> f.getCanton().equalsIgnoreCase(district));
    }

    public LinkedList<Familia> searchByPaternalLastName(String last_name) {
        return search(f -> f.getApellidoPaterno().equalsIgnoreCase(last_name));
    }

    public LinkedList<Familia> searchByMaternalLastName(String last_name) {
        return search(f -> f.getApellidoMaterno().equalsIgnoreCase(last_name));
    }

    public LinkedList<Familia> searchByMemberCount(int member_count) {
        return search(f -> f.getIntegrantes() == member_count);
    }

    public LinkedList<Familia> searchByGenerator(boolean has_generator) {
        return search(f -> f.getTieneGenerador() == has_generator);
    }

    // Sorting methods with predefined comparators
    public LinkedList<Familia> sortByPaternalLastName(boolean ascending) {
        return sort(Comparator.comparing(Familia::getApellidoPaterno, String.CASE_INSENSITIVE_ORDER), ascending);
    }

    public LinkedList<Familia> sortByMaternalLastName(boolean ascending) {
        return sort(Comparator.comparing(Familia::getApellidoMaterno, String.CASE_INSENSITIVE_ORDER), ascending);
    }

    public LinkedList<Familia> sortByDistrict(boolean ascending) {
        return sort(Comparator.comparing(Familia::getCanton, String.CASE_INSENSITIVE_ORDER), ascending);
    }

    public LinkedList<Familia> sortByMemberCount(boolean ascending) {
        return sort(Comparator.comparing(Familia::getIntegrantes), ascending);
    }

    public LinkedList<Familia> sortByGenerator(boolean ascending) {
        return sort(Comparator.comparing(Familia::getTieneGenerador), ascending);
    }

    // Counting methods
    public int countFamilies(Predicate<Familia> condition) {
        return (int) Arrays.stream(getFamilyList().toArray())
                .filter(condition)
                .count();
    }

    public int countFamiliesWithGenerator() {
        return countFamilies(Familia::getTieneGenerador);
    }
}