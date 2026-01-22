package pharmacie.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmacie.model.Role;
import pharmacie.model.User;
import pharmacie.service.UserService;

public class UserController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<Role> roleComboBox;
    @FXML
    private Label statusLabel;

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, Role> roleColumn;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList(Role.values()));
        roleComboBox.setValue(Role.EMPLOYEE);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        refresh();
    }

    public void refresh() {
        userTable.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
    }

    @FXML
    private void handleSave() {
        try {
            User u = new User();
            u.setUsername(usernameField.getText());
            u.setPassword(passwordField.getText());
            u.setRole(roleComboBox.getValue());

            userService.addUser(u);
            statusLabel.setText("Utilisateur ajouté !");
            refresh();
            usernameField.clear();
            passwordField.clear();
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        try {
            userService.deleteUser(selected.getId());
            statusLabel.setText("Utilisateur supprimé !");
            refresh();
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    private void handlePromote() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        try {
            Role newRole = (selected.getRole() == Role.ADMIN) ? Role.EMPLOYEE : Role.ADMIN;
            userService.changeRole(selected.getId(), newRole);
            statusLabel.setText("Rôle mis à jour !");
            refresh();
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
        }
    }
}
