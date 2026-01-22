package pharmacie.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmacie.model.Client;
import pharmacie.model.Product;
import pharmacie.model.Sale;
import pharmacie.model.SaleItem;
import pharmacie.service.AuthService;
import pharmacie.service.ClientService;
import pharmacie.service.ProductService;
import pharmacie.service.SaleService;

public class SalesController {

    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private TextField quantityField;

    @FXML
    private TableView<SaleItem> cartTable;
    @FXML
    private TableColumn<SaleItem, String> productNameColumn;
    @FXML
    private TableColumn<SaleItem, Double> priceColumn;
    @FXML
    private TableColumn<SaleItem, Integer> quantityColumn;
    @FXML
    private TableColumn<SaleItem, Double> totalColumn;

    @FXML
    private Label totalLabel;

    private ClientService clientService = new ClientService();
    private ProductService productService = new ProductService();
    private SaleService saleService = new SaleService();

    private ObservableList<SaleItem> cartItems = FXCollections.observableArrayList();
    private double currentTotal = 0.0;

    @FXML
    public void initialize() {
        // Load data
        clientComboBox.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
        productComboBox.setItems(FXCollections.observableArrayList(productService.getAllProducts()));

        // Setup Table
        productNameColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        cartTable.setItems(cartItems);
    }

    @FXML
    public void handleRefreshData() {
        productComboBox.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        clientComboBox.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
    }

    @FXML
    private void handleAddToCart() {
        Product selectedProduct = productComboBox.getValue();
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.WARNING, "Sélectionnez un produit.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(quantityField.getText());
            if (qty <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Quantité invalide.");
            return;
        }

        // Check stock locally (approximate)
        if (selectedProduct.getStockQuantity() < qty) {
            showAlert(Alert.AlertType.WARNING, "Stock insuffisant (Dispo: " + selectedProduct.getStockQuantity() + ")");
            return;
        }

        // Add to cart
        SaleItem item = new SaleItem();
        item.setProductId(selectedProduct.getId());
        item.setProductName(selectedProduct.getName());
        item.setQuantity(qty);
        item.setUnitPrice(selectedProduct.getPrice());

        cartItems.add(item);
        updateTotal();
    }

    @FXML
    private void handleCheckout() {
        if (cartItems.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Le panier est vide.");
            return;
        }

        Sale sale = new Sale();
        if (clientComboBox.getValue() != null) {
            sale.setClientId(clientComboBox.getValue().getId());
        }
        if (AuthService.getCurrentUser() != null) {
            sale.setUserId(AuthService.getCurrentUser().getId());
        }
        sale.setItems(cartItems);

        try {
            saleService.registerSale(sale);
            showAlert(Alert.AlertType.INFORMATION, "Vente enregistrée avec succès !");

            // Notification de stock faible
            java.util.List<pharmacie.model.Product> lowStock = productService.getLowStockProducts();
            if (!lowStock.isEmpty()) {
                StringBuilder sb = new StringBuilder("ALERTE STOCK FAIBLE :\n");
                for (pharmacie.model.Product p : lowStock) {
                    sb.append("- ").append(p.getName()).append(" (Stock: ").append(p.getStockQuantity()).append(")\n");
                }
                showAlert(Alert.AlertType.WARNING, sb.toString());
            }

            handleClear();
            productComboBox.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la vente: " + e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        cartItems.clear();
        updateTotal();
        quantityField.setText("1");
        productComboBox.getSelectionModel().clearSelection();
        clientComboBox.getSelectionModel().clearSelection();
    }

    private void updateTotal() {
        currentTotal = cartItems.stream().mapToDouble(SaleItem::getTotalPrice).sum();
        totalLabel.setText(String.format("%.3f DT", currentTotal));
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
