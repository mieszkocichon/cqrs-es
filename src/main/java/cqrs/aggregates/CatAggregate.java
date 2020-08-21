package cqrs.aggregates;

import cqrs.commands.CreateCatCommand;
import cqrs.commands.UpdateCatCommand;
import domain.Cat;
import cqrs.repository.CatWriteRepository;

public class CatAggregate {

    private CatWriteRepository catWriteRepository;

    public static CatAggregate empty() {
        return new CatAggregate();
    }

    private static CatAggregate create(CatWriteRepository catWriteRepository) {
        return new CatAggregate(catWriteRepository);
    }

    private CatAggregate() {
        this.catWriteRepository = CatWriteRepository.empty();
    }

    private CatAggregate(CatWriteRepository catWriteRepository) {
        this.catWriteRepository = catWriteRepository;
    }

    public Cat handleCreateCatCommand(CreateCatCommand createCatCommand) {
        Cat cat = Cat.create(createCatCommand.getCatId(), createCatCommand.getName());

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
