package pharmacie.service;

import pharmacie.dao.ClientDAO;
import pharmacie.exception.DonneeInvalideException;
import pharmacie.model.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {

    private ClientDAO clientDAO = new ClientDAO();

    public List<Client> getAllClients() {
        try {
            return clientDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void addClient(Client client) throws DonneeInvalideException, SQLException {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new DonneeInvalideException("Le nom du client est obligatoire.");
        }
        clientDAO.save(client);
    }

    public void deleteClient(int id) throws SQLException {
        clientDAO.delete(id);
    }

    // Add other methods as needed
}
