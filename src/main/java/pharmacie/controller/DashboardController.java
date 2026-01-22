package pharmacie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import pharmacie.app.Main;
import pharmacie.model.User;
import pharmacie.model.Role;
import pharmacie.service.AuthService;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label userLabel;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab salesTab;
    @FXML
    private Tab reportsTab;
    @FXML
    private Tab usersTab;
    @FXML
    private Tab perfTab;

    // Controllers injected automatically by FXML Loader
    @FXML
    private SalesController salesController;
    @FXML
    private ProductController productsController;
    @FXML
    private ClientController clientsController;
    @FXML
    private SupplierController suppliersController;
    @FXML
    private OrderController ordersController;
    @FXML
    private ReportsController reportsController;
    @FXML
    private UserController usersController;
    @FXML
    private SupplierPerformanceController perfController;

    @FXML
    public void initialize() {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser != null) {
            userLabel.setText("Utilisateur: " + currentUser.getUsername() +
                    " [" + currentUser.getRole() + "]");

            // Hide tabs for non-admin users
            if (currentUser.getRole() != Role.ADMIN) {
                mainTabPane.getTabs().remove(reportsTab);
                mainTabPane.getTabs().remove(usersTab);
                mainTabPane.getTabs().remove(perfTab);
            }
        }

        // Auto-refresh data when switching to Sales tab
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == salesTab && salesController != null) {
                salesController.handleRefreshData();
            }
        });
    }

    @FXML
    private void handleGlobalRefresh() {
        if (salesController != null)
            salesController.handleRefreshData();
        if (productsController != null)
            productsController.refresh();
        if (clientsController != null)
            clientsController.refresh();
        if (suppliersController != null)
            suppliersController.refresh();
        if (ordersController != null)
            ordersController.refresh();
        if (reportsController != null)
            reportsController.refresh();
        if (usersController != null)
            usersController.refresh();
        if (perfController != null)
            perfController.refresh();

        System.out.println("Global refresh executed.");
    }

    @FXML
    private void handleLogout() throws IOException {
        new AuthService().logout();
        Main.setRoot("login");
    }
}
