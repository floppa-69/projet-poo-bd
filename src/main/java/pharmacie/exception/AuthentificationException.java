package pharmacie.exception;

/**
 * Exception levée lors d'un échec d'authentification.
 */
public class AuthentificationException extends Exception {
    public AuthentificationException(String message) {
        super(message);
    }
}
