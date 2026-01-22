package pharmacie.dao;

import pharmacie.model.Sale;
import pharmacie.model.SaleItem;
import pharmacie.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {

    public void save(Sale sale) throws SQLException {
        Connection conn = null;
        PreparedStatement saleStmt = null;
        PreparedStatement itemStmt = null;
        PreparedStatement updateStockStmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // 1. Insert Sale
            String saleSql = "INSERT INTO sales (total_amount, client_id, user_id) VALUES (?, ?, ?)";
            saleStmt = conn.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS);
            saleStmt.setDouble(1, sale.getTotalAmount());
            if (sale.getClientId() > 0)
                saleStmt.setInt(2, sale.getClientId());
            else
                saleStmt.setNull(2, Types.INTEGER);
            if (sale.getUserId() > 0)
                saleStmt.setInt(3, sale.getUserId());
            else
                saleStmt.setNull(3, Types.INTEGER);
            saleStmt.executeUpdate();

            int saleId = 0;
            try (ResultSet generatedKeys = saleStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    saleId = generatedKeys.getInt(1);
                    sale.setId(saleId);
                }
            }

            // 2. Insert Items and Update Stock
            String itemSql = "INSERT INTO sale_items (sale_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";

            itemStmt = conn.prepareStatement(itemSql);
            updateStockStmt = conn.prepareStatement(updateStockSql);

            for (SaleItem item : sale.getItems()) {
                // Insert Item
                itemStmt.setInt(1, saleId);
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getUnitPrice());
                itemStmt.addBatch();

                // Update Stock
                updateStockStmt.setInt(1, item.getQuantity());
                updateStockStmt.setInt(2, item.getProductId());
                updateStockStmt.addBatch();
            }

            itemStmt.executeBatch();
            updateStockStmt.executeBatch();

            conn.commit(); // Commit Transaction

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (saleStmt != null)
                saleStmt.close();
            if (itemStmt != null)
                itemStmt.close();
            if (updateStockStmt != null)
                updateStockStmt.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Sale> findAll() throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT s.*, c.name as client_name, u.username as user_name " +
                "FROM sales s " +
                "LEFT JOIN clients c ON s.client_id = c.id " +
                "LEFT JOIN users u ON s.user_id = u.id " +
                "ORDER BY s.sale_date DESC";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getInt("id"));
                sale.setSaleDate(rs.getTimestamp("sale_date"));
                sale.setTotalAmount(rs.getDouble("total_amount"));
                sale.setClientId(rs.getInt("client_id"));
                sale.setUserId(rs.getInt("user_id"));
                sale.setClientName(rs.getString("client_name"));
                sale.setUserName(rs.getString("user_name"));
                sales.add(sale);
            }
        }
        return sales;
    }
}
