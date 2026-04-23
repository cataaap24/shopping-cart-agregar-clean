package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.ProductRepository;

public class AgregarProductoAlCarritoUseCase {
    private ProductRepository productRepository;
    private CartRepository cartRepository;

    public AgregarProductoAlCarritoUseCase(ProductRepository productRepository,
                                           CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public String execute(int productId) {
        Product product = productRepository.findById(productId);

        if (product == null) {
            return "Producto no encontrado.";
        }

        Cart cart = cartRepository.getCart();
        int currentQuantity = getQuantityInCart(cart, productId);

        if (!product.hasEnoughStock(currentQuantity + 1)) {
            return "Stock insuficiente para \"" + product.getName() + "\".";
        }

        cart.addProduct(product);
        cartRepository.save(cart);

        return "ok";
    }

    private int getQuantityInCart(Cart cart, int productId) {
        int i;

        for (i = 0; i < cart.getItems().size(); i++) {
            CartItem item = cart.getItems().get(i);

            if (item.getProduct().getId() == productId) {
                return item.getQuantity();
            }
        }

        return 0;
    }
}
