package cqrs.repository;

import domain.CatAddress;
import domain.CatOwnerContact;

import java.util.HashMap;
import java.util.Map;

public class CatReadRepository {

    private Map<String, CatOwnerContact> catContact;
    private Map<String, CatAddress> catAddress;

    public CatReadRepository() {
        this.catContact = new HashMap<>();
        this.catAddress = new HashMap<>();
    }

    public CatOwnerContact getCatContact(String catId) {
        return this.catContact.get(catId);
    }

    public void addCatOwnerContact(String catId, CatOwnerContact catOwnerContact) {
        this.catContact.put(catId, catOwnerContact);
    }

    public CatAddress getCatAddress(String catId) {
        return this.catAddress.get(catId);
    }

    public void addCatAddress(String catId, CatAddress catAddress) {
        this.catAddress.put(catId, catAddress);
    }
}
