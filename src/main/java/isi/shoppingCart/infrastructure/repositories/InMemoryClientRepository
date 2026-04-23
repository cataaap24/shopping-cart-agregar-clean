package isi.shoppingCart.infrastructure.repositories;

import isi.shoppingCart.entities.Client;
import isi.shoppingCart.usecases.ports.ClientRepository;
import java.util.ArrayList;
import java.util.List;

public class InMemoryClientRepository implements ClientRepository {
    private List<Client> clients;

    public InMemoryClientRepository() {
        clients = new ArrayList<Client>();
        clients.add(new Client(1, "Ana García", "ana@email.com", "1234"));
        clients.add(new Client(2, "Luis Pérez", "luis@email.com", "abcd"));
    }

    public Client findByEmail(String email) {
        for (Client client : clients) {
            if (client.getEmail().equals(email)) {
                return client;
            }
        }
        return null;
    }
    public Client findById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

}
