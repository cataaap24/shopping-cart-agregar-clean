package isi.shoppingCart.entities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Purchase {
    private int id;
    private List<CartItem> items;
    private double total;
    private int clientId;

    public Purchase(int id, int clientId, List<CartItem> items, double total) {
        this.id = id;
        this.clientId = clientId;
        this.items = new ArrayList<CartItem>(items);
        this.total = total;
    }
    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotal() {
        return total;
    }
}
