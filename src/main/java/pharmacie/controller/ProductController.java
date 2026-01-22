package pharmacie.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmacie.model.Product;
import pharmacie.service.ProductService;

public class ProductController {

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField minStockField;
    @FXML
    private Label statusLabel;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> stockColumn;

    private ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        refresh();

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    public void refresh() {
        productTable.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
    }

    private void populateForm(Product p) {
        nameField.setText(p.getName());
        descriptionField.setText(p.getDescription());
        priceField.setText(String.valueOf(p.getPrice()));
        stockField.setText(String.valueOf(p.getStockQuantity()));
        minStockField.setText(String.valueOf(p.getMinStockLevel()));
    }

    @FXML
    private void handleSave() {
        try {
            Product p = new Product();
            // If selecting existing, update ID (simple logic: if name matches? No, logic
            // updates based on ID but form doesn't show ID easily editable.
            // For simplicity: Clear selection = New. Select = Update.
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                p.setId(selected.getId());
            }

            p.setName(nameField.getText());
            p.setDescription(descriptionField.getText());
            p.setPrice(Double.parseDouble(priceField.getText()));
            p.setStockQuantity(Integer.parseInt(stockField.getText()));
            p.setMinStockLevel(Integer.parseInt(minStockField.getText()));

            if (p.getId() > 0) {
                productService.updateProduct(p);
                statusLabel.setText("Produit mis à jour !");
            } else {
                productService.addProduct(p);
                statusLabel.setText("Produit ajouté !");
            }
            refresh();
            handleClear();
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                productService.deleteProduct(selected.getId());
                refresh();
                handleClear();
                statusLabel.setText("Produit supprimé !");
            } catch (Exception e) {
                statusLabel.setText("Erreur suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        stockField.clear();
        minStockField.clear();
        productTable.getSelectionModel().clearSelection();
    }
}
