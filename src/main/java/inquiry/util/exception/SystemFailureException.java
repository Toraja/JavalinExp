package inquiry.util.exception;

public class SystemFailureException extends Exception {
    private static final long serialVersionUID = 1L;

    public SystemFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
