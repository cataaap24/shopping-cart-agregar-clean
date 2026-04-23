package isi.shoppingCart.infrastructure.repositories;

import isi.shoppingCart.usecases.ports.IdGenerator;

public class InMemoryIdGenerator implements IdGenerator {
    private int nextId;

    public InMemoryIdGenerator() {
        nextId = 1;
    }

    public int nextId() {
        int id = nextId;
        nextId = nextId + 1;
        return id;
    }
}