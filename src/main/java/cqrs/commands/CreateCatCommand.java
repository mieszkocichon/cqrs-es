package cqrs.commands;

public class CreateCatCommand {

    private String catId;
    private String name;

    public static CreateCatCommand create(String catId, String name) {
        return new CreateCatCommand(catId, name);
    }

    private CreateCatCommand() {
    }

    private CreateCatCommand(String catId, String name) {
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
