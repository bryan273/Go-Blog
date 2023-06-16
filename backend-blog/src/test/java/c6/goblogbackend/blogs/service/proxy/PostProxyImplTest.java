package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.*;
import c6.goblogbackend.blogs.exception.PostCreationException;
import c6.goblogbackend.blogs.exception.PostDoesNotExistException;
import c6.goblogbackend.blogs.model.Post;
import c6.goblogbackend.blogs.model.User;
import c6.goblogbackend.blogs.repository.PostRepository;
import c6.goblogbackend.blogs.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import c6.goblogbackend.blogs.dto.CreatePostRequest;
import c6.goblogbackend.blogs.dto.SearchPostRequest;
import c6.goblogbackend.blogs.dto.UpdatePostRequest;
import org.springframework.web.client.RestTemplate;

class PostProxyImplTest {

    private PostProxyImpl postProxy;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostService postService;

    @Mock
    private ResponseEntity<AuthenticationResponse> mockResponseEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postProxy = new PostProxyImpl(postRepository, postService);
        postProxy.setRestTemplate(mock(RestTemplate.class));
        postProxy.setAUTH_SERVICE_BASE_URL("http://example.com");
        postProxy.setAPI_KEY("api-key");
    }

    @Test
    void handleCreate_ValidTokenAndRequest_ReturnsCreatedPost() {
        // Arrange
        String jwt = "valid-jwt";
        CreatePostRequest request = new CreatePostRequest();
        request.setCreatorId(1);
        request.setTitle("Test Post");
        request.setContent("This is a test post.");

        User user = User.builder().id(request.getCreatorId()).build();

        Post expectedPost = new Post();
        expectedPost.setCreator(user);
        expectedPost.setTitle(request.getTitle());
        expectedPost.setContent(request.getContent());
        expectedPost.setLikes(0);
        expectedPost.setTimeCreated(null);

        CompletableFuture<Post> future = new CompletableFuture<>();
        future.complete(expectedPost);

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("bryan")
                .build();

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(postService.create(request)).thenReturn(future);

        // Act
        Post createdPost = postProxy.handleCreate(jwt, request);
        createdPost.setTimeCreated(null);
        // Assert
        assertNotNull(createdPost);
        assertEquals(expectedPost, createdPost);
        verify(postService, times(1)).create(request);
    }

    @Test
    void handleCreate_ValidTokenAndRequest_ThrowsPostCreationExceptionOnInterruptedException() {
        // Arrange
        String jwt = "valid-jwt";
        CreatePostRequest request = new CreatePostRequest();
        request.setCreatorId(1);
        request.setTitle("Test Post");
        request.setContent("This is a test post.");

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("bryan")
                .build();

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        CompletableFuture<Post> future = new CompletableFuture<>();
        future.completeExceptionally(new InterruptedException());

        when(postService.create(request)).thenReturn(future);

        // Act & Assert
        assertThrows(PostCreationException.class, () -> postProxy.handleCreate(jwt, request));
        verify(postService, times(1)).create(request);
    }

    @Test
    void handleSearch_ValidTokenAndRequest_ReturnsMatchingPosts() {
        // Arrange
        String jwt = "valid-jwt";
        SearchPostRequest request = new SearchPostRequest();
        request.setQueryTitle("test");

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        List<Post> expectedPosts = new ArrayList<>();
        Post post1 = new Post();
        post1.setPostId(1L);
        post1.setTitle("Test Post 1");
        post1.setContent("This is test post 1.");
        expectedPosts.add(post1);

        Post post2 = new Post();
        post2.setPostId(2L);
        post2.setTitle("Test Post 2");
        post2.setContent("This is test post 2.");
        expectedPosts.add(post2);

        when(postService.findByTitleIgnoreCase(request)).thenReturn(expectedPosts);

        // Act
        List<Post> matchingPosts = postProxy.handleSearch(jwt, request);

        // Assert
        assertNotNull(matchingPosts);
        assertEquals(expectedPosts.size(), matchingPosts.size());
        assertEquals(expectedPosts.get(0).getPostId(), matchingPosts.get(0).getPostId());
        assertEquals(expectedPosts.get(0).getTitle(), matchingPosts.get(0).getTitle());
        assertEquals(expectedPosts.get(0).getContent(), matchingPosts.get(0).getContent());
        assertEquals(expectedPosts.get(1).getPostId(), matchingPosts.get(1).getPostId());
        assertEquals(expectedPosts.get(1).getTitle(), matchingPosts.get(1).getTitle());
        assertEquals(expectedPosts.get(1).getContent(), matchingPosts.get(1).getContent());
        verify(postService, times(1)).findByTitleIgnoreCase(request);
    }

    @Test
    void handleUpdate_ValidTokenAndRequestAndExistingPost_ReturnsUpdatedPost() {
        // Arrange
        String jwt = "valid-jwt";
        UpdatePostRequest request = new UpdatePostRequest();
        request.setPostId(1);
        request.setTitle("Updated Post");
        request.setContent("This post has been updated.");

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("bryan")
                .build();

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        Post existingPost = new Post();
        existingPost.setPostId(1L);
        existingPost.setTitle("Old Post");
        existingPost.setContent("This is an old post.");

        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(existingPost));

        Post expectedPost = new Post();
        expectedPost.setPostId((long) request.getPostId());
        expectedPost.setTitle(request.getTitle());
        expectedPost.setContent(request.getContent());

        CompletableFuture<Post> future = new CompletableFuture<>();
        future.complete(expectedPost);

        when(postService.update(request)).thenReturn(future);

        // Act
        Post updatedPost = postProxy.handleUpdate(jwt, request);

        // Assert
        assertNotNull(updatedPost);
        assertEquals(expectedPost.getPostId(), updatedPost.getPostId());
        assertEquals(expectedPost.getTitle(), updatedPost.getTitle());
        assertEquals(expectedPost.getContent(), updatedPost.getContent());
        verify(postService, times(1)).update(request);
    }

    @Test
    void handleUpdate_ValidTokenAndRequestAndNonExistingPost_ThrowsPostDoesNotExistException() {
        // Arrange
        String jwt = "valid-jwt";
        UpdatePostRequest request = new UpdatePostRequest();
        request.setPostId(1);
        request.setTitle("Updated Post");
        request.setContent("This post has been updated.");

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("bryan")
                .build();

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(postRepository.findById(request.getPostId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PostDoesNotExistException.class, () -> postProxy.handleUpdate(jwt, request));
        verify(postService, never()).update(request);
    }

    @Test
    void handleUpdate_ValidTokenAndRequest_ThrowsPostCreationExceptionOnInterruptedException() {
        // Arrange
        String jwt = "valid-jwt";
        UpdatePostRequest request = new UpdatePostRequest();
        request.setPostId(1);
        request.setTitle("Updated Post");
        request.setContent("This post has been updated.");

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("bryan")
                .build();

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        Post existingPost = new Post();
        existingPost.setPostId(1L);
        existingPost.setTitle("Old Post");
        existingPost.setContent("This is an old post.");

        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(existingPost));

        CompletableFuture<Post> future = new CompletableFuture<>();
        future.completeExceptionally(new InterruptedException());

        when(postService.update(request)).thenReturn(future);

        // Act & Assert
        assertThrows(PostCreationException.class, () -> postProxy.handleUpdate(jwt, request));
        verify(postService, times(1)).update(request);
    }

    @Test
    void handleDelete_ValidTokenAndExistingPost_CallsPostServiceDelete() {
        // Arrange
        String jwt = "valid-jwt";
        int postId = 1;

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));

        // Act
        postProxy.handleDelete(jwt, postId);

        // Assert
        verify(postService, times(1)).delete(postId);
    }

    @Test
    void handleDelete_ValidTokenAndNonExistingPost_ThrowsPostDoesNotExistException() {
        // Arrange
        String jwt = "valid-jwt";
        int postId = 1;

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PostDoesNotExistException.class, () -> postProxy.handleDelete(jwt, postId));
        verify(postService, never()).delete(anyInt());
    }

    @Test
    void handleReadAll_ValidToken_ReturnsAllPosts() {
        // Arrange
        String jwt = "valid-jwt";

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        List<Post> expectedPosts = new ArrayList<>();
        Post post1 = new Post();
        post1.setPostId(1L);
        post1.setTitle("Post 1");
        post1.setContent("This is post 1.");
        expectedPosts.add(post1);

        Post post2 = new Post();
        post2.setPostId(2L);
        post2.setTitle("Post 2");
        post2.setContent("This is post 2.");
        expectedPosts.add(post2);

        when(postService.findAll()).thenReturn(expectedPosts);

        // Act
        List<Post> allPosts = postProxy.handleReadAll(jwt);

        // Assert
        assertNotNull(allPosts);
        assertEquals(expectedPosts.size(), allPosts.size());
        assertEquals(expectedPosts.get(0).getPostId(), allPosts.get(0).getPostId());
        assertEquals(expectedPosts.get(0).getTitle(), allPosts.get(0).getTitle());
        assertEquals(expectedPosts.get(0).getContent(), allPosts.get(0).getContent());
        assertEquals(expectedPosts.get(1).getPostId(), allPosts.get(1).getPostId());
        assertEquals(expectedPosts.get(1).getTitle(), allPosts.get(1).getTitle());
        assertEquals(expectedPosts.get(1).getContent(), allPosts.get(1).getContent());
        verify(postService, times(1)).findAll();
    }

    @Test
    void handleRead_ValidTokenAndExistingPost_ReturnsPost() {
        // Arrange
        String jwt = "valid-jwt";
        int postId = 1;

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("bryan")
                .build();

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        Post expectedPost = new Post();
        expectedPost.setPostId((long) postId);
        expectedPost.setTitle("Test Post");
        expectedPost.setContent("This is a test post.");

        GetPostResponse resp = new GetPostResponse();
        resp.setPost(expectedPost);


        when(postService.findById(postId)).thenReturn(resp);
        when(postRepository.findById(postId)).thenReturn(Optional.of(resp.getPost()));
        // Act
        GetPostResponse response = postProxy.handleRead(jwt, postId);

        // Assert
        assertNotNull(response);
        assertEquals(expectedPost, response.getPost());
        verify(postService, times(1)).findById(postId);
    }

    @Test
    void handleRead_ValidTokenAndNonExistingPost_ThrowsPostDoesNotExistException() {
        // Arrange
        String jwt = "valid-jwt";
        int postId = 1;

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(postProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(postService.findById(postId)).thenReturn(null);

        // Act & Assert
        assertThrows(PostDoesNotExistException.class, () -> postProxy.handleRead(jwt, postId));
        verify(postService, never()).findById(anyInt());
    }

    @Test
    void isPostDoesNotExist_PostDoesNotExist_ReturnsTrue() {
        // Arrange
        int postId = 1;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act
        boolean result = postProxy.isPostDoesNotExist(postId);

        // Assert
        assertTrue(result);
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void isPostDoesNotExist_PostExists_ReturnsFalse() {
        // Arrange
        int postId = 1;

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));

        // Act
        boolean result = postProxy.isPostDoesNotExist(postId);

        // Assert
        assertFalse(result);
        verify(postRepository, times(1)).findById(postId);
    }
}

