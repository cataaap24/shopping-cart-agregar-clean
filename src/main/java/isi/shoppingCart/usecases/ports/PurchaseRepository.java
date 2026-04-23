package isi.shoppingCart.usecases.ports;
import isi.shoppingCart.entities.Purchase;
public interface PurchaseRepository {
    void save(Purchase purchase);
}
