package pharmacie.dao;

import pharmacie.model.Supplier;
import pharmacie.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public void save(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO suppliers (name, contact, email, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContact());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getAddress());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    supplier.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Supplier supplier) throws SQLException {
        String sql = "UPDATE suppliers SET name = ?, contact = ?, email = ?, address = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContact());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getAddress());
            stmt.setInt(5, supplier.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Supplier findById(int id) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSupplier(rs);
                }
            }
        }
        return null;
    }

    public List<Supplier> findAll() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                suppliers.add(mapResultSetToSupplier(rs));
            }
        }
        return suppliers;
    }

    private Supplier mapResultSetToSupplier(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getInt("id"));
        supplier.setName(rs.getString("name"));
        supplier.setContact(rs.getString("contact"));
        supplier.setEmail(rs.getString("email"));
        supplier.setAddress(rs.getString("address"));
        return supplier;
    }
}
