package c6.goblogbackend.blogs.exception;

public class CommentDoesNotExistException extends RuntimeException{
    public CommentDoesNotExistException(Integer id) {
        super("Comment with id " + id + " is empty");
    }
}
