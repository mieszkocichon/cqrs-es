package cqrs.queries;

public class AddressByRegionQuery {

    private String catId;
    private String state;

    private AddressByRegionQuery() {
    }

    public AddressByRegionQuery(String catId, String state) {
        this.catId = catId;
        this.state = state;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
