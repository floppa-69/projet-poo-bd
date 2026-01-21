package pharmacie.service;

import pharmacie.dao.ProductDAO;
import pharmacie.exception.DonneeInvalideException;
import pharmacie.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {

    private ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        try {
            return productDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void addProduct(Product product) throws DonneeInvalideException, SQLException {
        validateProduct(product);
        productDAO.save(product);
    }

    public void updateProduct(Product product) throws DonneeInvalideException, SQLException {
        validateProduct(product);
        productDAO.update(product);
    }

    public void deleteProduct(int id) throws SQLException {
        productDAO.delete(id);
    }

    private void validateProduct(Product product) throws DonneeInvalideException {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom du produit est obligatoire.");
        }
        if (product.getPrice() <= 0) {
            throw new DonneeInvalideException("Le prix doit être positif.");
        }
        if (product.getMinStockLevel() < 0) {
            throw new DonneeInvalideException("Le stock minimum ne peut pas être négatif.");
        }
    }
}
