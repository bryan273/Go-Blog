package c6.goblogbackend.blogs.service.comment;


import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.model.Notification;

import java.util.List;

public interface CommentService {
    Comment create(CreateCommentRequest request);
    void delete(Integer id);
    List<Comment> getAll(int postId);

    Notification addNotification(CreateCommentRequest request);

}


