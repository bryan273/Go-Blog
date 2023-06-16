package c6.goblogbackend.blogs.dto;

import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.model.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DtoTest {

    @Test
    void testCreateCommentRequest() {
        CreateCommentRequest createCommentRequest = CreateCommentRequest.builder()
                .content("Test comment")
                .creator("John")
                .creatorId(1)
                .postId(2)
                .timeCreated(new Date())
                .build();

        Assertions.assertEquals("Test comment", createCommentRequest.getContent());
        Assertions.assertEquals("John", createCommentRequest.getCreator());
        Assertions.assertEquals(1, createCommentRequest.getCreatorId());
        Assertions.assertEquals(2, createCommentRequest.getPostId());
        Assertions.assertNotNull(createCommentRequest.getTimeCreated());
    }

    @Test
    void testCreatePostRequest() {
        CreatePostRequest createPostRequest = CreatePostRequest.builder()
                .creatorId(1)
                .creator("John")
                .title("Test post")
                .content("This is a test post")
                .likes(0)
                .timeCreated(new Date())
                .build();

        Assertions.assertEquals(1, createPostRequest.getCreatorId());
        Assertions.assertEquals("John", createPostRequest.getCreator());
        Assertions.assertEquals("Test post", createPostRequest.getTitle());
        Assertions.assertEquals("This is a test post", createPostRequest.getContent());
        Assertions.assertEquals(0, createPostRequest.getLikes());
        Assertions.assertNotNull(createPostRequest.getTimeCreated());
    }

    @Test
    void testGetCommentRequest() {
        GetCommentRequest getCommentRequest = GetCommentRequest.builder()
                .postId(2)
                .build();

        Assertions.assertEquals(2, getCommentRequest.getPostId());
    }

    @Test
    void testGetPostResponse() {
        Post post = new Post();
        List<Comment> comments = new ArrayList<>();
        GetPostResponse getPostResponse = GetPostResponse.builder()
                .post(post)
                .comments(comments)
                .build();

        Assertions.assertEquals(post, getPostResponse.getPost());
        Assertions.assertEquals(comments, getPostResponse.getComments());
    }

    @Test
    void testPostDTO() {
        Post post = new Post();
        List<Comment> comments = new ArrayList<>();
        PostDTO postDTO = PostDTO.builder()
                .post(post)
                .comments(comments)
                .build();

        Assertions.assertEquals(post, postDTO.getPost());
        Assertions.assertEquals(comments, postDTO.getComments());
    }

    @Test
    void testSearchPostRequest() {
        SearchPostRequest searchPostRequest = SearchPostRequest.builder()
                .queryTitle("Test")
                .build();

        Assertions.assertEquals("Test", searchPostRequest.getQueryTitle());
    }

    @Test
    void testUpdatePostRequest() {
        UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                .postId(2)
                .creatorId(1)
                .creator("John")
                .title("Test post")
                .content("This is an updated post")
                .likes(0)
                .timeCreated(new Date())
                .build();

        Assertions.assertEquals(2, updatePostRequest.getPostId());
        Assertions.assertEquals(1, updatePostRequest.getCreatorId());
        Assertions.assertEquals("John", updatePostRequest.getCreator());
        Assertions.assertEquals("Test post", updatePostRequest.getTitle());
        Assertions.assertEquals("This is an updated post", updatePostRequest.getContent());
        Assertions.assertEquals(0, updatePostRequest.getLikes());
        Assertions.assertNotNull(updatePostRequest.getTimeCreated());
    }

    @Test
    void testUserDTO() {
        UserDTO userDTO = UserDTO.builder()
                .username("John")
                .id(1)
                .build();

        Assertions.assertEquals("John", userDTO.getUsername());
        Assertions.assertEquals(1, userDTO.getId());
    }

    @Test
    void testPostDTODataAnnotation() {
        PostDTO postDTO = new PostDTO();
        postDTO.setPost(new Post());
        postDTO.setComments(new ArrayList<>());

        Assertions.assertNotNull(postDTO.getPost());
        Assertions.assertNotNull(postDTO.getComments());
    }

    @Test
    void testPostDTODefaultConstructor() {
        PostDTO postDTO = new PostDTO();

        Assertions.assertNotNull(postDTO);
    }

    @Test
    void testCreateCommentRequestDataAnnotation() {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();

        Assertions.assertNotNull(createCommentRequest);
    }

    @Test
    void testCreateCommentRequestNoArgsConstructor() {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();

        Assertions.assertNotNull(createCommentRequest);
    }

    @Test
    void testCreatePostRequestDataAnnotation() {
        CreatePostRequest createPostRequest = new CreatePostRequest();

        Assertions.assertNotNull(createPostRequest);
    }

    @Test
    void testCreatePostRequestNoArgsConstructor() {
        CreatePostRequest createPostRequest = new CreatePostRequest();

        Assertions.assertNotNull(createPostRequest);
    }

    @Test
    void testGetCommentRequestDataAnnotation() {
        GetCommentRequest getCommentRequest = new GetCommentRequest();

        Assertions.assertNotNull(getCommentRequest);
    }

    @Test
    void testGetCommentRequestNoArgsConstructor() {
        GetCommentRequest getCommentRequest = new GetCommentRequest();

        Assertions.assertNotNull(getCommentRequest);
    }

    @Test
    void testGetPostResponseDataAnnotation() {
        GetPostResponse getPostResponse = new GetPostResponse();

        Assertions.assertNotNull(getPostResponse);
    }

    @Test
    void testGetPostResponseNoArgsConstructor() {
        GetPostResponse getPostResponse = new GetPostResponse();

        Assertions.assertNotNull(getPostResponse);
    }

    @Test
    void testSearchPostRequestDataAnnotation() {
        SearchPostRequest searchPostRequest = new SearchPostRequest();

        Assertions.assertNotNull(searchPostRequest);
    }

    @Test
    void testSearchPostRequestNoArgsConstructor() {
        SearchPostRequest searchPostRequest = new SearchPostRequest();

        Assertions.assertNotNull(searchPostRequest);
    }

    @Test
    void testUpdatePostRequestDataAnnotation() {
        UpdatePostRequest updatePostRequest = new UpdatePostRequest();

        Assertions.assertNotNull(updatePostRequest);
    }

    @Test
    void testUpdatePostRequestNoArgsConstructor() {
        UpdatePostRequest updatePostRequest = new UpdatePostRequest();

        Assertions.assertNotNull(updatePostRequest);
    }

    @Test
    void testUserDTODataAnnotation() {
        UserDTO userDTO = new UserDTO();

        Assertions.assertNotNull(userDTO);
    }

    @Test
    void testUserTONoArgsConstructor() {
        UserDTO userDTO = new UserDTO();

        Assertions.assertNotNull(userDTO);
    }

    @Test
    void testAuthenticationResponse() {
        // Arrange
        String content = "Lorem ipsum";
        String username = "john_doe";
        int userId = 123;
        boolean isAuthenticated = true;

        // Act
        AuthenticationResponse response = new AuthenticationResponse(content, username, userId, isAuthenticated);

        // Assert
        Assertions.assertEquals(content, response.getContent());
        Assertions.assertEquals(username, response.getUsername());
        Assertions.assertEquals(userId, response.getUserId());
        Assertions.assertTrue(response.isAuthenticated());

        // Additional assertion to test the default constructor
        AuthenticationResponse defaultResponse = new AuthenticationResponse();
        Assertions.assertNull(defaultResponse.getContent());
        Assertions.assertNull(defaultResponse.getUsername());
        Assertions.assertEquals(0, defaultResponse.getUserId());
        Assertions.assertFalse(defaultResponse.isAuthenticated());
    }
}
