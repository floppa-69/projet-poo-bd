package pharmacie.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmacie.model.Product;
import pharmacie.model.Supplier;
import pharmacie.model.SupplierOrder;
import pharmacie.model.SupplierOrderItem;
import pharmacie.service.ProductService;
import pharmacie.service.StockService;
import pharmacie.service.SupplierService;

public class OrderController {

    @FXML
    private ComboBox<Supplier> supplierComboBox;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private TextField qtyField;
    @FXML
    private TableView<SupplierOrderItem> newItemTable;
    @FXML
    private TableColumn<SupplierOrderItem, String> newItemNameColumn;
    @FXML
    private TableColumn<SupplierOrderItem, Integer> newItemQtyColumn;
    @FXML
    private CheckBox newProductCheckBox;
    @FXML
    private VBox newProductBox;
    @FXML
    private TextField newProductNameField;
    @FXML
    private TextField newProductPriceField;
    @FXML
    private Label createStatusLabel;

    @FXML
    private TableView<SupplierOrder> ordersTable;
    @FXML
    private TableColumn<SupplierOrder, Integer> orderIdColumn;
    @FXML
    private TableColumn<SupplierOrder, String> orderDateColumn;
    @FXML
    private TableColumn<SupplierOrder, String> orderSupplierColumn;
    @FXML
    private TableColumn<SupplierOrder, String> orderStatusColumn;
    @FXML
    private Label receiveStatusLabel;

    private SupplierService supplierService = new SupplierService();
    private ProductService productService = new ProductService();
    private StockService stockService = new StockService();

    private ObservableList<SupplierOrderItem> newItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Toggle visibility for new product fields
        newProductBox.visibleProperty().bind(newProductCheckBox.selectedProperty());
        newProductBox.managedProperty().bind(newProductCheckBox.selectedProperty());
        productComboBox.disableProperty().bind(newProductCheckBox.selectedProperty());

        // Init Create Section
        newItemNameColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        newItemQtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        newItemTable.setItems(newItems);

        // Init List Section
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderSupplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        refresh();
    }

    public void refresh() {
        supplierComboBox.setItems(FXCollections.observableArrayList(supplierService.getAllSuppliers()));
        productComboBox.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        loadOrders();
    }

    private void loadOrders() {
        ordersTable.setItems(FXCollections.observableArrayList(stockService.getAllOrders()));
    }

    @FXML
    private void handleAddItem() {
        try {
            Product p;
            if (newProductCheckBox.isSelected()) {
                // Create new product immediately
                String name = newProductNameField.getText();
                double price = Double.parseDouble(newProductPriceField.getText());
                if (name == null || name.trim().isEmpty())
                    throw new Exception("Nom de produit requis");

                p = new Product(name, "Créé via commande fournisseur", price, 0, 5);
                productService.addProduct(p);

                newProductNameField.clear();
                newProductPriceField.clear();
                newProductCheckBox.setSelected(false);
                productComboBox.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
            } else {
                p = productComboBox.getValue();
                if (p == null)
                    return;
            }

            int qty = Integer.parseInt(qtyField.getText());
            if (qty <= 0)
                throw new NumberFormatException();

            SupplierOrderItem item = new SupplierOrderItem(p.getId(), qty);
            item.setProductName(p.getName());
            newItems.add(item);

            qtyField.clear();
            createStatusLabel.setText("");
        } catch (NumberFormatException e) {
            createStatusLabel.setText("Quantité ou Prix invalide");
        } catch (Exception e) {
            createStatusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateOrder() {
        Supplier s = supplierComboBox.getValue();
        if (s == null) {
            createStatusLabel.setText("Choisir un fournisseur");
            return;
        }
        if (newItems.isEmpty()) {
            createStatusLabel.setText("Ajouter des produits");
            return;
        }

        try {
            SupplierOrder order = new SupplierOrder();
            order.setSupplierId(s.getId());
            order.setItems(newItems);

            stockService.createOrder(order);

            createStatusLabel.setText("Commande créée !");
            newItems.clear();
            loadOrders();
        } catch (Exception e) {
            createStatusLabel.setText("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReceiveOrder() {
        SupplierOrder selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if ("RECEIVED".equals(selected.getStatus())) {
                receiveStatusLabel.setText("Commande déjà reçue.");
                return;
            }
            try {
                stockService.receiveOrder(selected.getId());
                receiveStatusLabel.setText("Stock mis à jour !");
                loadOrders();
            } catch (Exception e) {
                receiveStatusLabel.setText("Erreur: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
