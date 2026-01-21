package pharmacie.service;

import pharmacie.dao.SupplierDAO;
import pharmacie.exception.DonneeInvalideException;
import pharmacie.model.Supplier;

import java.sql.SQLException;
import java.util.List;

public class SupplierService {

    private SupplierDAO supplierDAO = new SupplierDAO();

    public List<Supplier> getAllSuppliers() {
        try {
            return supplierDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void addSupplier(Supplier supplier) throws DonneeInvalideException, SQLException {
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom du fournisseur est obligatoire.");
        }
        supplierDAO.save(supplier);
    }

    public void updateSupplier(Supplier supplier) throws DonneeInvalideException, SQLException {
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom du fournisseur est obligatoire.");
        }
        supplierDAO.update(supplier);
    }

    public void deleteSupplier(int id) throws SQLException {
        supplierDAO.delete(id);
    }
}
