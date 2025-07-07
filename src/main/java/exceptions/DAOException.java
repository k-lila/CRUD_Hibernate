package exceptions;

public class DAOException extends Exception {
    private static final long serialVersionUID = 7054379063290825137L;
    public DAOException(String message, Exception exception) {
        super(message, exception);
    }
}
