package cqrs.aggregates;

import cqrs.commands.CreateCatCommand;
import cqrs.commands.UpdateCatCommand;
import domain.Cat;
import cqrs.repository.CatWriteRepository;

public class CatAggregate {

    private CatWriteRepository catWriteRepository;

    public CatAggregate() {
        this.catWriteRepository = new CatWriteRepository();
    }

    public CatAggregate(CatWriteRepository catWriteRepository) {
        this.catWriteRepository = catWriteRepository;
    }

    public Cat handleCreateCatCommand(CreateCatCommand createCatCommand) {
        Cat cat = new Cat(createCatCommand.getCatId(), createCatCommand.getName());

        this.catWriteRepository.addCat(cat.getCatId(), cat);

        return cat;
    }

    public Cat handleUpdateCatCommand(UpdateCatCommand updateCatCommand) {
        Cat cat = catWriteRepository.getCat(updateCatCommand.getCatId());
        cat.setAddresses(updateCatCommand.getAddresses());
        cat.setContacts(updateCatCommand.getContacts());

        catWriteRepository.addCat(cat.getCatId(), cat);

        return cat;
    }
}
