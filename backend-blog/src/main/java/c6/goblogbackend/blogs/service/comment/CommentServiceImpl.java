package c6.goblogbackend.blogs.service.comment;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.model.Notification;
import c6.goblogbackend.blogs.repository.CommentRepository;
import c6.goblogbackend.blogs.repository.PostRepository;
import c6.goblogbackend.blogs.repository.UserRepository;
import c6.goblogbackend.blogs.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Autowired
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    @Autowired
    private final NotificationService notificationService;
    private final PostRepository postRepository;

    @Override
    public List<Comment> getAll(int postId) { return commentRepository.findByPostId(postId); }

    @Override
    public Comment create(CreateCommentRequest request) {

        var user = userRepository.findById(request.getCreatorId()).orElseThrow();


        Comment comment = Comment.builder()
                .content(request.getContent())
                .timeCreated(new Date())
                .postId(request.getPostId())
                .creator(user)
                .build();
        commentRepository.save(comment);
        addNotification(request);
        

        return comment;
    }

    @Override
    public void delete(Integer id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Notification addNotification(CreateCommentRequest request){
        var post = postRepository.findById(request.getPostId()).orElse(null);

        if (post != null && post.getCreator().getId() != request.getCreatorId()){

            CreateCommentRequest notif = CreateCommentRequest.builder()
                                                            .creatorId(request.getCreatorId())
                                                            .content(request.getContent())
                                                            .postId(request.getPostId())
                                                            .subscriberId(post.getCreator().getId())
                                                            .build();
            return notificationService.create(notif);
        }else {
            return null;
        }
    }
}


