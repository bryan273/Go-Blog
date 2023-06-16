package c6.goblogbackend.blogs.controller;

import c6.goblogbackend.blogs.dto.CreatePostRequest;
import c6.goblogbackend.blogs.dto.GetPostResponse;
import c6.goblogbackend.blogs.dto.SearchPostRequest;
import c6.goblogbackend.blogs.dto.UpdatePostRequest;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.model.Post;
import c6.goblogbackend.blogs.service.proxy.PostProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostControllerTest {

    private PostController postController;

    @Mock
    private PostProxy postProxyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postController = new PostController(postProxyService);
    }

    @Test
    void testGetAllPost() {
        // Arrange
        String jwtToken = "dummyToken";
        List<Post> expectedPosts = new ArrayList<>();
        expectedPosts.add(new Post(1L, null, "Post 1", "Content 1", 0, null));
        expectedPosts.add(new Post(2L, null, "Post 2", "Content 2", 0, null));
        when(postProxyService.handleReadAll(jwtToken)).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<Post>> response = postController.getAllPost(jwtToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPosts, response.getBody());
        verify(postProxyService, times(1)).handleReadAll(jwtToken);
    }

    @Test
    void testGetPostById() {
        // Arrange
        String jwtToken = "dummyToken";
        int postId = 1;
        UpdatePostRequest request = UpdatePostRequest.builder().postId(1).build();
        Post expectedPost = new Post(1L, null, "Post 1", "Content 1", 0, null);
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment(1, null, 1, null, "Comment 1"));
        expectedComments.add(new Comment(2, null, 1, null, "Comment 2"));
        GetPostResponse expectedResponse = new GetPostResponse(expectedPost, expectedComments);
        when(postProxyService.handleRead(jwtToken, request.getPostId())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<GetPostResponse> response = postController.getPostById(jwtToken, postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(postProxyService, times(1)).handleRead(jwtToken, postId);
    }

    @Test
    void testAddNewPost() {
        // Arrange
        String jwtToken = "dummyToken";
        CreatePostRequest request = CreatePostRequest.builder()
                .title("New Post")
                .content("New Content")
                .build();
        Post expectedPost = new Post(1L, null, "New Post", "New Content", 0, null);

        when(postProxyService.handleCreate(jwtToken, request)).thenReturn(expectedPost);

        // Act
        ResponseEntity<Post> response = postController.addNewPost(jwtToken, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPost, response.getBody());
        verify(postProxyService, times(1)).handleCreate(jwtToken, request);
    }

    @Test
    void testUpdatePost() {
        // Arrange
        String jwtToken = "dummyToken";
        UpdatePostRequest request = UpdatePostRequest.builder()
                .postId(1)
                .title("Updated Post")
                .content("Updated Content")
                .build();
        Post expectedPost = new Post(1L, null, "Updated Post", "Updated Content", 0, null);
        when(postProxyService.handleUpdate(jwtToken, request)).thenReturn(expectedPost);

        // Act
        ResponseEntity<Post> response = postController.updatePost(jwtToken, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPost, response.getBody());
        verify(postProxyService, times(1)).handleUpdate(jwtToken, request);
    }

    @Test
    void testDeletePost() {
        // Arrange
        int postId = 1;
        String jwtToken = "dummyToken";

        // Act
        ResponseEntity<String> response = postController.deletePost(postId, jwtToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Post with id 1", response.getBody());
        verify(postProxyService, times(1)).handleDelete(jwtToken, postId);
    }

    @Test
    void testSearchPost() {
        // Arrange
        String jwtToken = "dummyToken";
        SearchPostRequest request = new SearchPostRequest("keyword");
        List<Post> expectedPosts = new ArrayList<>();
        expectedPosts.add(new Post(1L, null, "Post 1", "Content 1", 0, null));
        expectedPosts.add(new Post(2L, null, "Post 2", "Content 2", 0, null));
        when(postProxyService.handleSearch(jwtToken, request)).thenReturn(expectedPosts);

        // Act
        ResponseEntity<List<Post>> response = postController.searchPost(jwtToken, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPosts, response.getBody());
        verify(postProxyService, times(1)).handleSearch(jwtToken, request);
    }
}
