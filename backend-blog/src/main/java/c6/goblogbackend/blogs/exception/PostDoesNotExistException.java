package c6.goblogbackend.blogs.exception;

public class PostDoesNotExistException extends RuntimeException {
    public PostDoesNotExistException(Integer id) {
        super("Post with id " + id + " does not exist");
    }
}