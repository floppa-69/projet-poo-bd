package pharmacie.service;

import pharmacie.dao.UserDAO;
import pharmacie.exception.AuthentificationException;
import pharmacie.model.User;
import pharmacie.util.SecurityUtil;

import java.sql.SQLException;

public class AuthService {

    private UserDAO userDAO = new UserDAO();
    private static User currentUser;

    public User login(String username, String password) throws AuthentificationException, SQLException {
        User user = userDAO.findByUsername(username);

        if (user == null) {
            throw new AuthentificationException("Utilisateur non trouvé.");
        }

        String hashedPassword = SecurityUtil.hashPassword(password);
        if (!user.getPassword().equals(hashedPassword)) {
            throw new AuthentificationException("Mot de passe incorrect.");
        }

        currentUser = user;
        return user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
