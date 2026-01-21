package pharmacie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pharmacie.app.Main;
import pharmacie.exception.AuthentificationException;
import pharmacie.service.AuthService;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            authService.login(username, password);
            Main.setRoot("dashboard");
        } catch (AuthentificationException e) {
            errorLabel.setText(e.getMessage());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("System error: " + e.getMessage());
        }
    }
}
