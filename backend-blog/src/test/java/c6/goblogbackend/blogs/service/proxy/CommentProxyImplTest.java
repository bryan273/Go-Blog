package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.AuthenticationResponse;
import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.exception.CommentDoesNotExistException;
import c6.goblogbackend.blogs.exception.PostDoesNotExistException;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.model.Post;
import c6.goblogbackend.blogs.repository.CommentRepository;
import c6.goblogbackend.blogs.repository.PostRepository;
import c6.goblogbackend.blogs.service.comment.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentProxyImplTest {

    private CommentProxyImpl commentProxy;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ResponseEntity<AuthenticationResponse> mockResponseEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentProxy = new CommentProxyImpl(commentRepository, commentService, postRepository);
        commentProxy.setRestTemplate(mock(RestTemplate.class));
        commentProxy.setAUTH_SERVICE_BASE_URL("http://example.com");
        commentProxy.setAPI_KEY("api-key");
    }

    @Test
    void handleReadAll_ValidTokenAndExistingPost_ReturnsComments() {
        // Arrange
        String jwt = "valid-jwt";
        int postId = 1;

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build();       
        when(commentProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        List<Comment> expectedComments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setCommentId(1);
        comment1.setContent("This is comment 1.");
        expectedComments.add(comment1);

        Comment comment2 = new Comment();
        comment2.setCommentId(2);
        comment2.setContent("This is comment 2.");
        expectedComments.add(comment2);

        when(commentService.getAll(postId)).thenReturn(expectedComments);

        // Act
        List<Comment> comments = commentProxy.handleReadAll(jwt, postId);

        // Assert
        assertNotNull(comments);
        assertEquals(expectedComments.size(), comments.size());
        assertEquals(expectedComments.get(0).getCommentId(), comments.get(0).getCommentId());
        assertEquals(expectedComments.get(0).getContent(), comments.get(0).getContent());
        assertEquals(expectedComments.get(1).getCommentId(), comments.get(1).getCommentId());
        assertEquals(expectedComments.get(1).getContent(), comments.get(1).getContent());
        verify(commentService, times(1)).getAll(postId);
    }

    @Test
    void handleCreate_ValidTokenAndRequestAndExistingPost_ReturnsCreatedComment() {
        // Arrange
        String jwt = "valid-jwt";
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(1);
        request.setContent("This is a comment.");

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(commentProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(new Post()));

        Comment expectedComment = new Comment();
        expectedComment.setCommentId(1);
        expectedComment.setContent(request.getContent());

        when(commentService.create(request)).thenReturn(expectedComment);

        // Act
        Comment createdComment = commentProxy.handleCreate(jwt, request);

        // Assert
        assertNotNull(createdComment);
        assertEquals(expectedComment.getCommentId(), createdComment.getCommentId());
        assertEquals(expectedComment.getContent(), createdComment.getContent());
        verify(commentService, times(1)).create(request);
    }

    @Test
    void handleCreate_ValidTokenAndRequestAndNonExistingPost_ThrowsPostDoesNotExistException() {
        // Arrange
        String jwt = "valid-jwt";
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(1);
        request.setContent("This is a comment.");

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(commentProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(postRepository.findById(request.getPostId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PostDoesNotExistException.class, () -> commentProxy.handleCreate(jwt, request));
        verify(commentService, never()).create(request);
    }

    @Test
    void handleDelete_ValidTokenAndExistingComment_CallsCommentServiceDelete() {
        // Arrange
        String jwt = "valid-jwt";
        int commentId = 1;

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(commentProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));

        // Act
        commentProxy.handleDelete(jwt, commentId);

        // Assert
        verify(commentService, times(1)).delete(commentId);
    }

    @Test
    void handleDelete_ValidTokenAndNonExistingComment_ThrowsCommentDoesNotExistException() {
        // Arrange
        String jwt = "valid-jwt";
        int commentId = 1;

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(commentProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentDoesNotExistException.class, () -> commentProxy.handleDelete(jwt, commentId));
        verify(commentService, never()).delete(anyInt());
    }

    @Test
    void isCommentIsEmpty_CommentDoesNotExist_ReturnsTrue() {
        // Arrange
        int commentId = 1;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act
        boolean result = commentProxy.isCommentIsEmpty(commentId);

        // Assert
        assertTrue(result);
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void isCommentIsEmpty_CommentExists_ReturnsFalse() {
        // Arrange
        int commentId = 1;

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));

        // Act
        boolean result = commentProxy.isCommentIsEmpty(commentId);

        // Assert
        assertFalse(result);
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void checkPostExistance_PostExists_DoesNotThrowException() {
        // Arrange
        int postId = 1;

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));

        // Act & Assert
        assertDoesNotThrow(() -> commentProxy.checkPostExistance(postId));
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void checkPostExistance_PostDoesNotExist_ThrowsPostDoesNotExistException() {
        // Arrange
        int postId = 1;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PostDoesNotExistException.class, () -> commentProxy.checkPostExistance(postId));
        verify(postRepository, times(1)).findById(postId);
    }
}
