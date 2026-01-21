package pharmacie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pharmacie.app.Main;
import pharmacie.service.AuthService;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label userLabel;

    @FXML
    public void initialize() {
        if (AuthService.getCurrentUser() != null) {
            userLabel.setText("Utilisateur: " + AuthService.getCurrentUser().getUsername() +
                    " [" + AuthService.getCurrentUser().getRole() + "]");
        }
    }

    @FXML
    private void handleLogout() throws IOException {
        new AuthService().logout();
        Main.setRoot("login");
    }
}
