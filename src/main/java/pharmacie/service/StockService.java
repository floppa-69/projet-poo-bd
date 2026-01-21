package pharmacie.service;

import pharmacie.dao.SupplierOrderDAO;
import pharmacie.model.SupplierOrder;

import java.sql.SQLException;
import java.util.List;

public class StockService {

    private SupplierOrderDAO orderDAO = new SupplierOrderDAO();

    public void createOrder(SupplierOrder order) throws SQLException {
        orderDAO.save(order);
    }

    public void receiveOrder(int orderId) throws SQLException {
        orderDAO.receiveOrder(orderId);
    }

    public List<SupplierOrder> getAllOrders() {
        try {
            return orderDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
