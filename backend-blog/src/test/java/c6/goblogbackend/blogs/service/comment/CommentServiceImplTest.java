package c6.goblogbackend.blogs.service.comment;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.model.Notification;
import c6.goblogbackend.blogs.model.Post;
import c6.goblogbackend.blogs.model.User;
import c6.goblogbackend.blogs.repository.CommentRepository;
import c6.goblogbackend.blogs.repository.PostRepository;
import c6.goblogbackend.blogs.repository.UserRepository;
import c6.goblogbackend.blogs.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentServiceImpl(commentRepository, userRepository, notificationService, postRepository);
    }

    @Test
    void testGetAll() {
        int postId = 1;
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        comments.add(new Comment());

        when(commentRepository.findByPostId(postId)).thenReturn(comments);

        List<Comment> result = commentService.getAll(postId);
        assertEquals(comments.size(), result.size());
        verify(commentRepository, times(1)).findByPostId(postId);
    }

    @Test
    void testCreate() {
        int creatorId = 1;
        int postId = 1;
        String content = "Test comment";

        CreateCommentRequest request = new CreateCommentRequest();
        request.setCreatorId(creatorId);
        request.setPostId(postId);
        request.setContent(content);

        User user = User.builder().id(1).build();

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTimeCreated(new Date());
        comment.setPostId(postId);
        comment.setCreator(user);

        Post post = Post.builder().postId((long) postId).creator(user).build();
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(User.builder().id(1).build()));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(post));

        Comment result = commentService.create(request);

        assertEquals(content, result.getContent());
        assertEquals(postId, result.getPostId());
        assertEquals(creatorId, result.getCreator().getId());
        verify(userRepository, times(1)).findById(creatorId);
        verify(commentRepository, times(1)).save(result);
    }

    @Test
    void testCreate_InvalidCreatorId() {
        int creatorId = 1;
        int postId = 1;
        String content = "Test comment";

        CreateCommentRequest request = new CreateCommentRequest();
        request.setCreatorId(creatorId);
        request.setPostId(postId);
        request.setContent(content);

        when(userRepository.findById(creatorId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> commentService.create(request));
        verify(userRepository, times(1)).findById(creatorId);
        verify(commentRepository, never()).save(any());
    }

    @Test
    void testDelete() {
        Integer commentId = 1;

        commentService.delete(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void testAddNotification_PostExistsAndCreatorIdNotEqualToSubscriberId_ReturnsNotification() {
        int creatorId = 1;
        int postId = 1;
        int subscriberId = 2;
        String content = "Test comment";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .creatorId(creatorId)
                .postId(postId)
                .subscriberId(subscriberId)
                .content(content)
                .build();
                
        User subscriber = User.builder().id(subscriberId).build();
        Post post = Post.builder().postId((long) postId).creator(subscriber).build();
        Notification notification = new Notification(); // Replace with the actual Notification object

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(notificationService.create(any(CreateCommentRequest.class))).thenReturn(notification);

        Notification result = commentService.addNotification(request);

        assertNotNull(result);
        verify(postRepository, times(1)).findById(postId);
        verify(notificationService, times(1)).create(any(CreateCommentRequest.class));
    }

    @Test
    void testAddNotification_PostExistsAndCreatorIdEqualToSubscriberId_ReturnsNull() {
        int creatorId = 1;
        int postId = 1;
        int subscriberId = 1; // Same as creatorId
        String content = "Test comment";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .creatorId(creatorId)
                .postId(postId)
                .subscriberId(subscriberId)
                .content(content)
                .build();

        User creator = User.builder().id(creatorId).build();
        Post post = Post.builder().postId((long) postId).creator(creator).build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Notification result = commentService.addNotification(request);

        assertNull(result);
        verify(postRepository, times(1)).findById(postId);
        verify(notificationService, never()).create(any(CreateCommentRequest.class));
    }

    @Test
    void testAddNotification_PostDoesNotExist_ReturnsNull() {
        int creatorId = 1;
        int postId = 1;
        int subscriberId = 2;
        String content = "Test comment";

        CreateCommentRequest request = CreateCommentRequest.builder()
                .creatorId(creatorId)
                .postId(postId)
                .subscriberId(subscriberId)
                .content(content)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Notification result = commentService.addNotification(request);

        assertNull(result);
        verify(postRepository, times(1)).findById(postId);
        verify(notificationService, never()).create(any(CreateCommentRequest.class));
    }

}
