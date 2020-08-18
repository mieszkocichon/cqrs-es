package cqrs.commands;

public class CreateCatCommand {

    private String catId;
    private String name;

    private CreateCatCommand() {
    }

    public CreateCatCommand(String catId, String name) {
        this.catId = catId;
        this.name = name;
    }

    public String getCatId() {
        return catId;
    }

    public String getName() {
        return name;
    }
}
