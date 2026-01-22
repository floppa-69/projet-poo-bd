package pharmacie.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmacie.model.SupplierPerformance;
import pharmacie.service.StockService;

public class SupplierPerformanceController {

    @FXML
    private TableView<SupplierPerformance> performanceTable;
    @FXML
    private TableColumn<SupplierPerformance, String> supplierNameColumn;
    @FXML
    private TableColumn<SupplierPerformance, Double> totalSpentColumn;

    private StockService stockService = new StockService();

    @FXML
    public void initialize() {
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        totalSpentColumn.setCellValueFactory(new PropertyValueFactory<>("totalSpent"));

        refresh();
    }

    public void refresh() {
        performanceTable.setItems(FXCollections.observableArrayList(stockService.getPerformanceData()));
    }
}
