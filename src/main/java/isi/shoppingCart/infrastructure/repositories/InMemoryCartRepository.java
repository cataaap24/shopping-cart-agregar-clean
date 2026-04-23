package isi.shoppingCart.infrastructure.repositories;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.ProductRepository;

public class InMemoryCartRepository implements CartRepository {
    private Cart cart;

    public InMemoryCartRepository(ProductRepository productRepository) {
        cart = new Cart();

        Product product1 = productRepository.findById(1);
        Product product2 = productRepository.findById(2);
        Product product3 = productRepository.findById(3);

        if (product1 != null) {
            cart.addProduct(product1);
        }

        if (product2 != null) {
            cart.addProduct(product2);
            cart.addProduct(product2);
        }

        if (product3 != null) {
            cart.addProduct(product3);
        }
    }

    public Cart getCart() {
        return cart;
    }

    public void save(Cart cart) {
        this.cart = cart;
    }
}
