package isi.shoppingCart.usecases.ports;

import isi.shoppingCart.entities.Client;

public interface ClientRepository {
    Client findByEmail(String email);
    Client findById(int id);
}
