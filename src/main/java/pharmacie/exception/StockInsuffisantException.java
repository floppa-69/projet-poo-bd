package pharmacie.exception;

/**
 * Exception levée lorsque le stock d'un produit est insuffisant pour une vente.
 */
public class StockInsuffisantException extends Exception {
    public StockInsuffisantException(String message) {
        super(message);
    }
}
