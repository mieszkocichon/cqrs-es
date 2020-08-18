package cqrs.repository;

import domain.Cat;

import java.util.HashMap;
import java.util.Map;

public class CatWriteRepository {

    private Map<String, Cat> store;

    public CatWriteRepository() {
        this.store = new HashMap<String, Cat>();
    }

    public void addCat(String catId, Cat cat) {
        this.store.put(catId, cat);
    }

    public Cat getCat(String catId) {
        return this.store.get(catId);
    }
}
