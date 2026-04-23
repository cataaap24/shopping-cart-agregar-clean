package isi.shoppingCart.infrastructure.repositories;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.usecases.ports.PurchaseRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryPurchaseRepository implements PurchaseRepository {
    private List<Purchase> purchases;

    public InMemoryPurchaseRepository() {
        purchases = new ArrayList<>();
    }

    public void save(Purchase purchase) {
        purchases.add(purchase);
    }

    public List<Purchase> findAll() {
        return Collections.unmodifiableList(purchases);
    }

}
