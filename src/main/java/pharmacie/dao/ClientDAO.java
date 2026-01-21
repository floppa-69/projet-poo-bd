package pharmacie.dao;

import pharmacie.model.Client;
import pharmacie.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void save(Client client) throws SQLException {
        String sql = "INSERT INTO clients (name, phone, email, loyalty_points) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.setInt(4, client.getLoyaltyPoints());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Client client) throws SQLException {
        String sql = "UPDATE clients SET name = ?, phone = ?, email = ?, loyalty_points = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.setInt(4, client.getLoyaltyPoints());
            stmt.setInt(5, client.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Client findById(int id) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        }
        return null;
    }

    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setName(rs.getString("name"));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        client.setLoyaltyPoints(rs.getInt("loyalty_points"));
        return client;
    }
}
