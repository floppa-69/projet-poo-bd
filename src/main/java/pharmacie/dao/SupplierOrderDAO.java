package pharmacie.dao;

import pharmacie.model.SupplierOrder;
import pharmacie.model.SupplierOrderItem;
import pharmacie.model.SupplierPerformance;
import pharmacie.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierOrderDAO {

    public void save(SupplierOrder order) throws SQLException {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement itemStmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String orderSql = "INSERT INTO supplier_orders (supplier_id, status) VALUES (?, ?)";
            orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, order.getSupplierId());
            orderStmt.setString(2, "PENDING"); // Default status
            orderStmt.executeUpdate();

            int orderId = 0;
            try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                    order.setId(orderId);
                }
            }

            String itemSql = "INSERT INTO supplier_order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
            itemStmt = conn.prepareStatement(itemSql);

            for (SupplierOrderItem item : order.getItems()) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.addBatch();
            }

            itemStmt.executeBatch();
            conn.commit();

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
            if (orderStmt != null)
                orderStmt.close();
            if (itemStmt != null)
                itemStmt.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void updateStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE supplier_orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }

    // Method to receive order and update stock in a transaction
    public void receiveOrder(int orderId) throws SQLException {
        Connection conn = null;
        PreparedStatement getItemsStmt = null;
        PreparedStatement updateStockStmt = null;
        PreparedStatement updateStatusStmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Get items to update stock
            String getItemsSql = "SELECT product_id, quantity FROM supplier_order_items WHERE order_id = ?";
            getItemsStmt = conn.prepareStatement(getItemsSql);
            getItemsStmt.setInt(1, orderId);

            String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE id = ?";
            updateStockStmt = conn.prepareStatement(updateStockSql);

            try (ResultSet rs = getItemsStmt.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");

                    updateStockStmt.setInt(1, quantity);
                    updateStockStmt.setInt(2, productId);
                    updateStockStmt.addBatch();
                }
            }
            updateStockStmt.executeBatch();

            // 2. Update status
            String updateStatusSql = "UPDATE supplier_orders SET status = 'RECEIVED' WHERE id = ?";
            updateStatusStmt = conn.prepareStatement(updateStatusSql);
            updateStatusStmt.setInt(1, orderId);
            updateStatusStmt.executeUpdate();

            conn.commit();

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
            if (getItemsStmt != null)
                getItemsStmt.close();
            if (updateStockStmt != null)
                updateStockStmt.close();
            if (updateStatusStmt != null)
                updateStatusStmt.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<SupplierOrder> findAll() throws SQLException {
        List<SupplierOrder> orders = new ArrayList<>();
        String sql = "SELECT o.*, s.name as supplier_name " +
                "FROM supplier_orders o " +
                "LEFT JOIN suppliers s ON o.supplier_id = s.id " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SupplierOrder order = new SupplierOrder();
                order.setId(rs.getInt("id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setSupplierId(rs.getInt("supplier_id"));
                order.setStatus(rs.getString("status"));
                order.setSupplierName(rs.getString("supplier_name"));
                orders.add(order);
            }
        }
        return orders;
    }

    public List<SupplierPerformance> getSupplierPerformance() throws SQLException {
        List<SupplierPerformance> performanceList = new ArrayList<>();
        String sql = "SELECT s.name, SUM(p.price * soi.quantity) as total_spent " +
                "FROM suppliers s " +
                "JOIN supplier_orders o ON s.id = o.supplier_id " +
                "JOIN supplier_order_items soi ON o.id = soi.order_id " +
                "JOIN products p ON soi.product_id = p.id " +
                "WHERE o.status = 'RECEIVED' " +
                "GROUP BY s.id, s.name " +
                "ORDER BY total_spent DESC";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                performanceList.add(new SupplierPerformance(
                        rs.getString("name"),
                        rs.getDouble("total_spent")));
            }
        }
        return performanceList;
    }
}
