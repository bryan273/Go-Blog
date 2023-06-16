package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.exception.CommentDoesNotExistException;
import c6.goblogbackend.blogs.exception.PostDoesNotExistException;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.repository.CommentRepository;
import c6.goblogbackend.blogs.repository.PostRepository;
import c6.goblogbackend.blogs.service.comment.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentProxyImpl extends ProxyServiceBase implements CommentProxy {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final PostRepository postRepository;

    @Override
    public List<Comment> handleReadAll(String jwt, int postId) {
        authenticateJWT(jwt);

        return commentService.getAll(postId);
    }



    @Override
    public Comment handleCreate(String jwt, CreateCommentRequest request) {
        authenticateJWT(jwt);
        checkPostExistance(request.getPostId());

        return commentService.create(request);
    }

    @Override
    public void handleDelete(String jwt, int commentId) {
        authenticateJWT(jwt);

        if (isCommentIsEmpty(commentId)) {
            throw new CommentDoesNotExistException(commentId);
        }

        commentService.delete(commentId);
    }

    boolean isCommentIsEmpty(Integer id) {
        return commentRepository.findById(id).isEmpty();
    }

    void checkPostExistance(int postId) throws PostDoesNotExistException{
        var post = postRepository.findById(postId);
        if (!post.isPresent()) throw new PostDoesNotExistException(postId);
    }


}
