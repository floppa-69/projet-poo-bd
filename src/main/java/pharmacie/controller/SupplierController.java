package pharmacie.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmacie.model.Supplier;
import pharmacie.service.SupplierService;

public class SupplierController {

    @FXML
    private TextField nameField, contactField, emailField, addressField;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, Integer> idColumn;
    @FXML
    private TableColumn<Supplier, String> nameColumn, contactColumn, emailColumn;

    private SupplierService supplierService = new SupplierService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadSuppliers();
    }

    private void loadSuppliers() {
        supplierTable.setItems(FXCollections.observableArrayList(supplierService.getAllSuppliers()));
    }

    @FXML
    private void handleSave() {
        try {
            Supplier s = new Supplier();
            s.setName(nameField.getText());
            s.setContact(contactField.getText());
            s.setEmail(emailField.getText());
            s.setAddress(addressField.getText());
            supplierService.addSupplier(s);
            statusLabel.setText("Fournisseur ajouté !");
            loadSuppliers();
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }
}
