package pharmacie.service;

import pharmacie.dao.UserDAO;
import pharmacie.model.Role;
import pharmacie.model.User;
import pharmacie.util.SecurityUtil;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void addUser(User user) throws SQLException {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(SecurityUtil.hashPassword(user.getPassword()));
        }
        userDAO.save(user);
    }

    public void deleteUser(int id) throws SQLException {
        userDAO.delete(id);
    }

    public void changeRole(int id, Role role) throws SQLException {
        userDAO.updateRole(id, role);
    }
}
