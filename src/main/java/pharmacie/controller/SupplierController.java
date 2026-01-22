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

        refresh();
    }

    public void refresh() {
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
            refresh();
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Sélectionnez un fournisseur !");
            return;
        }
        try {
            supplierService.deleteSupplier(selected.getId());
            statusLabel.setText("Fournisseur supprimé !");
            refresh();
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }
}
