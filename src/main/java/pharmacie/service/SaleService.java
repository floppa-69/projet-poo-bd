package pharmacie.service;

import pharmacie.dao.ProductDAO;
import pharmacie.dao.SaleDAO;
import pharmacie.exception.StockInsuffisantException;
import pharmacie.model.Product;
import pharmacie.model.Sale;
import pharmacie.model.SaleItem;

import java.sql.SQLException;
import java.util.List;

public class SaleService {

    private SaleDAO saleDAO = new SaleDAO();
    private ProductDAO productDAO = new ProductDAO();

    public void registerSale(Sale sale) throws StockInsuffisantException, SQLException {
        // Validate stock for all items
        // Aggregate quantities by product ID
        java.util.Map<Integer, Integer> productQuantities = new java.util.HashMap<>();
        for (SaleItem item : sale.getItems()) {
            productQuantities.put(item.getProductId(),
                    productQuantities.getOrDefault(item.getProductId(), 0) + item.getQuantity());
        }

        // Check stock for aggregated quantities
        for (java.util.Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            Product product = productDAO.findById(entry.getKey());
            if (product == null) {
                throw new SQLException("Produit introuvable : ID " + entry.getKey());
            }
            if (product.getStockQuantity() < entry.getValue()) {
                throw new StockInsuffisantException("Stock insuffisant pour le produit : " + product.getName() +
                        " (Demandé: " + entry.getValue() + ", Disponible: " + product.getStockQuantity() + ")");
            }
        }

        for (SaleItem item : sale.getItems()) {
            Product product = productDAO.findById(item.getProductId());
            // Set unit price from current product price (ensure consistency)
            item.setUnitPrice(product.getPrice());
        }

        // Calculate total amount
        double total = 0;
        for (SaleItem item : sale.getItems()) {
            total += item.getQuantity() * item.getUnitPrice();
        }
        sale.setTotalAmount(total);

        // Save sale (DAO handles transaction and stock update)
        saleDAO.save(sale);
    }

    public List<Sale> getAllSales() {
        try {
            return saleDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
