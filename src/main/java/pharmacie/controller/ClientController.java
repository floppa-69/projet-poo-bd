package pharmacie.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmacie.model.Client;
import pharmacie.service.ClientService;

public class ClientController {

    @FXML
    private TextField nameField, phoneField, emailField;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, Integer> idColumn;
    @FXML
    private TableColumn<Client, String> nameColumn, phoneColumn, emailColumn;

    private ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadClients();
    }

    private void loadClients() {
        clientTable.setItems(FXCollections.observableArrayList(clientService.getAllClients()));
    }

    @FXML
    private void handleSave() {
        try {
            Client c = new Client();
            c.setName(nameField.getText());
            c.setPhone(phoneField.getText());
            c.setEmail(emailField.getText());
            clientService.addClient(c);
            statusLabel.setText("Client ajouté !");
            loadClients();
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }
}
