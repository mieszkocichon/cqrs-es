package cqrs.repository;

import domain.Cat;

import java.util.HashMap;
import java.util.Map;

public class CatWriteRepository {

    private Map<String, Cat> store;

    public static CatWriteRepository empty() {
        return new CatWriteRepository();
    }

    private CatWriteRepository() {
        this.store = new HashMap<>();
    }

    public void addCat(String catId, Cat cat) {
        this.store.put(catId, cat);
    }

    public Cat getCat(String catId) {
        return this.store.get(catId);
    }
}
