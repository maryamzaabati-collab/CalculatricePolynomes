/**
 * Erreur de saisie ou de calcul.
 * Même schéma qu'en cours : une classe qui étend Exception.
 */
public class CalculException extends Exception {

    public CalculException(String message) {
        super(message);
    }
}
