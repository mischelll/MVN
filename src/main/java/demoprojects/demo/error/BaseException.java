package demoprojects.demo.error;

public abstract class BaseException extends RuntimeException {
    private int statusCode;

    public BaseException(int statusCode) {
        this.statusCode = statusCode;
    }

    public BaseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
