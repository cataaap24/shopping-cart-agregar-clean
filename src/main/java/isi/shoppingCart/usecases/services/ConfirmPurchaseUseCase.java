package isi.shoppingCart.usecases.services;
import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.IdGenerator;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;


public class ConfirmPurchaseUseCase {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private PurchaseRepository purchaseRepository;
    private IdGenerator idGenerator;

    public ConfirmPurchaseUseCase(CartRepository cartRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, IdGenerator idGenerator) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.idGenerator = idGenerator;
    }

    public String execute(int clienteId) {
        Cart cart = cartRepository.getCart();

        if (cart.isEmpty()) {
            return "No se puede confirmar la compra. El carrito esta vacio.";
        }

        int i;
    // Verificar stock para cada producto en el carrito
        for (i = 0; i < cart.getItems().size(); i++) {
            CartItem item = cart.getItems().get(i);
            Product product = productRepository.findById(item.getProduct().getId());

            if (product == null ||!product.hasEnoughStock(item.getQuantity())) {
                return "Stock insuficiente para " + item.getProduct().getName() + " (disponible: " + product.getStock() + ", solicitado: " + item.getQuantity() + ").";
            }
        }

        double total = cart.getTotal();
        int id = idGenerator.nextId();
        Purchase purchase = new Purchase(id, clienteId,  cart.getItems(), total);
        purchaseRepository.save(purchase);

        // Reducir stock del inventario
        for (i = 0; i < purchase.getItems().size(); i++) {
            CartItem item = purchase.getItems().get(i);
            Product product = productRepository.findById(item.getProduct().getId());
            product.decreaseStock(item.getQuantity());
            productRepository.save(product); // Guardar cambios en el producto después de actualizar el stock, esto es cuando se mude a BD
        }

        cart.clear();
        cartRepository.save(cart);

        return "Compra #" + id + " confirmada con exito. Total: $" + total;
    }
}
