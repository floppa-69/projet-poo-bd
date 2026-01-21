package pharmacie.exception;

/**
 * Exception levée lorsqu'une donnée fournie est invalide ou manquante.
 */
public class DonneeInvalideException extends Exception {
    public DonneeInvalideException(String message) {
        super(message);
    }
}
