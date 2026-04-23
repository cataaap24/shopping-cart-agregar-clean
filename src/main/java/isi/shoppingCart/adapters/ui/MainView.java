package isi.shoppingCart.adapters.ui;

import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.infrastructure.repositories.InMemoryCartRepository;
import isi.shoppingCart.infrastructure.repositories.InMemoryPurchaseRepository;
import isi.shoppingCart.infrastructure.repositories.InMemoryIdGenerator;
import isi.shoppingCart.infrastructure.repositories.InMemoryProductRepository;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.IdGenerator;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;
import isi.shoppingCart.usecases.services.AgregarProductoAlCarritoUseCase;
import isi.shoppingCart.usecases.services.ShoppingCartApp;
import isi.shoppingCart.usecases.services.ConfirmPurchaseUseCase;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainView {
    private ShoppingCartApp shoppingCartApp;
    private int clientId = 1;  // cliente fijo para simular sin login
    private VBox catalogBox;
    private VBox cartBox;
    private Label totalLabel;

    public MainView() {
        ProductRepository productRepository = new InMemoryProductRepository();
        PurchaseRepository purchaseRepository = new InMemoryPurchaseRepository();
        IdGenerator idGenerator = new InMemoryIdGenerator();
        CartRepository cartRepository = new InMemoryCartRepository(productRepository);
        AgregarProductoAlCarritoUseCase agregarProductoAlCarritoUseCase =
                new AgregarProductoAlCarritoUseCase(productRepository, cartRepository);
        ConfirmPurchaseUseCase confirmPurchaseUseCase = new ConfirmPurchaseUseCase(cartRepository, productRepository, purchaseRepository, idGenerator);
        shoppingCartApp = new ShoppingCartApp(
                productRepository,
                cartRepository,
                agregarProductoAlCarritoUseCase,
                confirmPurchaseUseCase
        );

        catalogBox = new VBox(10);
        cartBox = new VBox(10);
        totalLabel = new Label("Total: $ 0.0");
    }

    public Scene createScene() {
        VBox catalogPanel = createCatalogPanel();
        VBox cartPanel = createCartPanel();

        HBox content = new HBox(20);
        content.setPadding(new Insets(15));
        content.getChildren().addAll(catalogPanel, cartPanel);

        HBox.setHgrow(catalogPanel, Priority.ALWAYS);
        HBox.setHgrow(cartPanel, Priority.ALWAYS);

        refreshCatalog();
        refreshCart();

        BorderPane root = new BorderPane();
        root.setCenter(content);

        return new Scene(root, 800, 450);
    }

    private VBox createCatalogPanel() {
        Label title = new Label("Catalogo");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox panel = new VBox(10);
        panel.getChildren().addAll(title, catalogBox);
        panel.setPrefWidth(380);
        panel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");
        return panel;
    }

    private VBox createCartPanel() {
        Label title = new Label("Carrito");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button confirmButton = new Button("Confirmar compra");
        confirmButton.setOnAction(event -> onConfirmPurchase());

        VBox panel = new VBox(10);
        panel.getChildren().addAll(title, cartBox, totalLabel, confirmButton);
        panel.setPrefWidth(380);
        panel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");
        return panel;
    }

    private void refreshCatalog() {
        catalogBox.getChildren().clear();

        List<Product> products = shoppingCartApp.getCatalogProducts();
        int i;

        for (i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            HBox row = new HBox(10);

            Label nameLabel = new Label(product.getName());
            Label priceLabel = new Label("$ " + product.getPrice());
            Label stockLabel = new Label("Stock: " + product.getStock());
            Button addButton = new Button("Agregar");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            addButton.setOnAction(event -> {
                String result = shoppingCartApp.addProductToCart(product.getId());

                if (!result.equals("ok")) {
                    showMessage(result, Alert.AlertType.WARNING);
                }

                refreshCatalog();
                refreshCart();
            });

            row.getChildren().addAll(nameLabel, priceLabel, stockLabel, spacer, addButton);
            row.setStyle("-fx-padding: 5; -fx-border-color: #DDDDDD;");

            catalogBox.getChildren().add(row);
        }
    }

    private void refreshCart() {
        cartBox.getChildren().clear();

        List<CartItem> items = shoppingCartApp.getCartItems();
        int i;

        for (i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            HBox row = new HBox(10);

            Label nameLabel = new Label(item.getProduct().getName());
            Label quantityLabel = new Label("Cantidad: " + item.getQuantity());
            Label subtotalLabel = new Label("Subtotal: $ " + item.getSubtotal());

            row.getChildren().addAll(nameLabel, quantityLabel, subtotalLabel);
            row.setStyle("-fx-padding: 5; -fx-border-color: #DDDDDD;");

            cartBox.getChildren().add(row);
        }

        totalLabel.setText("Total: $ " + shoppingCartApp.getCartTotal());
    }

    private void onConfirmPurchase() {
        String result = shoppingCartApp.confirmPurchase(clientId);

        Alert.AlertType type;
        if (result.startsWith("Compra #")) {
            type = Alert.AlertType.INFORMATION;
        } else {
            type = Alert.AlertType.WARNING;
        }

        showMessage(result, type);
        refreshCatalog();
        refreshCart();
    }

    private void showMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Shopping Cart");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
