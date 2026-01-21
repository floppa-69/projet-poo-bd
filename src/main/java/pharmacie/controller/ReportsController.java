package pharmacie.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmacie.model.Product;
import pharmacie.model.Sale;
import pharmacie.service.ProductService;
import pharmacie.service.SaleService;

import java.util.List;
import java.util.stream.Collectors;

public class ReportsController {

    @FXML
    private Label totalSalesLabel;
    @FXML
    private TableView<Sale> salesTable;
    @FXML
    private TableColumn<Sale, Integer> saleIdColumn;
    @FXML
    private TableColumn<Sale, String> saleDateColumn;
    @FXML
    private TableColumn<Sale, String> saleClientColumn;
    @FXML
    private TableColumn<Sale, String> saleUserColumn;
    @FXML
    private TableColumn<Sale, Double> saleAmountColumn;

    @FXML
    private TableView<Product> lowStockTable;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> currentStockColumn;
    @FXML
    private TableColumn<Product, Integer> minStockColumn;
    @FXML
    private TableColumn<Product, String> supplierColumn;

    private SaleService saleService = new SaleService();
    private ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        // Init Sales Table
        saleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        saleDateColumn.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        saleClientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        saleUserColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        saleAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        // Init Stock Table
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentStockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        minStockColumn.setCellValueFactory(new PropertyValueFactory<>("minStockLevel"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        refresh();
    }

    @FXML
    private void refresh() {
        // Load Sales
        List<Sale> sales = saleService.getAllSales();
        salesTable.setItems(FXCollections.observableArrayList(sales));

        double total = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        totalSalesLabel.setText(String.format("Total Ventes: %.3f DT", total));

        // Load Low Stock
        List<Product> allProducts = productService.getAllProducts();
        List<Product> lowStock = allProducts.stream()
                .filter(p -> p.getStockQuantity() <= p.getMinStockLevel())
                .collect(Collectors.toList());
        lowStockTable.setItems(FXCollections.observableArrayList(lowStock));
    }
}
