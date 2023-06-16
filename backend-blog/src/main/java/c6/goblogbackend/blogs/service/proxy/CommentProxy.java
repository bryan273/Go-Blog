package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Comment;

import java.util.List;

public interface CommentProxy {
    Comment handleCreate(String jwt, CreateCommentRequest request);
    List<Comment> handleReadAll(String jwt, int postId);
    void handleDelete(String jwt, int postId);
}
