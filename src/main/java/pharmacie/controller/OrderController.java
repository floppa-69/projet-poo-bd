package pharmacie.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        // Init Create Section
        supplierComboBox.setItems(FXCollections.observableArrayList(supplierService.getAllSuppliers()));
        productComboBox.setItems(FXCollections.observableArrayList(productService.getAllProducts()));

        newItemNameColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));
        newItemQtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        newItemTable.setItems(newItems);

        // Init List Section
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderSupplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadOrders();
    }

    private void loadOrders() {
        ordersTable.setItems(FXCollections.observableArrayList(stockService.getAllOrders()));
    }

    @FXML
    private void handleAddItem() {
        Product p = productComboBox.getValue();
        if (p == null)
            return;

        try {
            int qty = Integer.parseInt(qtyField.getText());
            if (qty <= 0)
                throw new NumberFormatException();

            SupplierOrderItem item = new SupplierOrderItem(p.getId(), qty);
            item.setProductName(p.getName());
            newItems.add(item);

            qtyField.clear();
        } catch (NumberFormatException e) {
            createStatusLabel.setText("Quantité invalide");
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
