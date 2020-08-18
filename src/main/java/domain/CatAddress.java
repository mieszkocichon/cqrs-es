package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CatAddress {
    private Map<String, Set<Address>> addressByRegion = new HashMap<>();

    public void setAddressByRegion(Map<String, Set<Address>> addressByRegion) {
        this.addressByRegion = addressByRegion;
    }

    public Map<String, Set<Address>> getAddressByType() {
        return this.addressByRegion;
    }
}
