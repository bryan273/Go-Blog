package c6.goblogbackend.blogs.controller;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.service.proxy.CommentProxy;
import c6.goblogbackend.blogs.service.proxy.NotificationProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    private CommentController commentController;

    @Mock
    private CommentProxy commentProxyService;

    @Mock
    private NotificationProxy notificationProxyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        commentController = new CommentController(commentProxyService);
    }

    @Test
    void testGetCommentById() {
        // Arrange
        int postId = 1;
        String jwtToken = "dummyToken";
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment(1, null, postId, null, "Comment 1"));
        expectedComments.add(new Comment(2, null, postId, null, "Comment 2"));
        when(commentProxyService.handleReadAll(jwtToken, postId)).thenReturn(expectedComments);

        // Act
        ResponseEntity<List<Comment>> response = commentController.getCommentById(postId, jwtToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedComments, response.getBody());
        verify(commentProxyService, times(1)).handleReadAll(jwtToken, postId);
    }

    @Test
    void testAddComment() {
        // Arrange
        String jwtToken = "dummyToken";
        CreateCommentRequest request = CreateCommentRequest.builder()
                .content("New Comment")
                .creator("John Doe")
                .creatorId(1)
                .postId(1)
                .timeCreated(new Date())
                .build();
        Comment expectedComment = new Comment(1, null, 1, null, "New Comment");
        when(commentProxyService.handleCreate(jwtToken, request)).thenReturn(expectedComment);

        // Act
        ResponseEntity<Comment> response = commentController.addComment(jwtToken, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedComment, response.getBody());
        verify(commentProxyService, times(1)).handleCreate(jwtToken, request);
    }

    @Test
    void testDeleteComment() {
        // Arrange
        int commentId = 1;
        String jwtToken = "dummyToken";

        // Act
        ResponseEntity<String> response = commentController.deleteComment(commentId, jwtToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Comment with id 1", response.getBody());
        verify(commentProxyService, times(1)).handleDelete(jwtToken, commentId);
    }
}
