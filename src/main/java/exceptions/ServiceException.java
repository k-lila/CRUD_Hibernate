package exceptions;

public class ServiceException extends Exception {
	private static final long serialVersionUID = 7054379063290825137L;
    public ServiceException(String msg, Exception e) {
        super(msg, e);
    }
}
