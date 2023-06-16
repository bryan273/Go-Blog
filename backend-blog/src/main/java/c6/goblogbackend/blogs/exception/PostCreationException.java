package c6.goblogbackend.blogs.exception;

public class PostCreationException extends RuntimeException {
    public PostCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
