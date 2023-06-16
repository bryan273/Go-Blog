package c6.goblogbackend.blogs.service.post;

import c6.goblogbackend.blogs.dto.CreatePostRequest;
import c6.goblogbackend.blogs.dto.GetPostResponse;
import c6.goblogbackend.blogs.dto.SearchPostRequest;
import c6.goblogbackend.blogs.dto.UpdatePostRequest;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.model.Post;
import c6.goblogbackend.blogs.model.User;
import c6.goblogbackend.blogs.repository.CommentRepository;
import c6.goblogbackend.blogs.repository.PostRepository;
import c6.goblogbackend.blogs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostServiceImpl(postRepository, commentRepository, userRepository);
    }

    @Test
    void testFindAll() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());
        Sort sort = Sort.by(Sort.Direction.DESC, "timeCreated");

        when(postRepository.findAll(sort)).thenReturn(posts);

        List<Post> result = postService.findAll();
        assertEquals(posts.size(), result.size());
        verify(postRepository, times(1)).findAll(sort);
    }

    @Test
    void testFindByTitleIgnoreCase() {
        String queryTitle = "test";
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());

        SearchPostRequest request = new SearchPostRequest();
        request.setQueryTitle(queryTitle);

        when(postRepository.findByTitleContainingIgnoreCase(queryTitle)).thenReturn(posts);

        List<Post> result = postService.findByTitleIgnoreCase(request);

        assertEquals(posts.size(), result.size());
        verify(postRepository, times(1)).findByTitleContainingIgnoreCase(queryTitle);
    }

    @Test
    void testFindById() {
        Integer postId = 1;
        Optional<Post> post = Optional.of(new Post());
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        comments.add(new Comment());

        when(postRepository.findById(postId)).thenReturn(post);
        when(commentRepository.findByPostId(postId)).thenReturn(comments);

        GetPostResponse result = postService.findById(postId);

        assertEquals(post.get(), result.getPost());
        assertEquals(comments.size(), result.getComments().size());
        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findByPostId(postId);
    }

    @Test
    void testCreate() throws ExecutionException, InterruptedException {
        int creatorId = 1;
        String title = "Test Title";
        String content = "Test Content";

        CreatePostRequest request = new CreatePostRequest();
        request.setCreatorId(creatorId);
        request.setTitle(title);
        request.setContent(content);

        User user = User.builder().id(1).build();

        Post post = new Post();
        post.setCreator(user);
        post.setTitle(title);
        post.setContent(content);
        post.setLikes(0);

        when(userRepository.findById(creatorId)).thenReturn(Optional.of(User.builder().id(1).build()));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        CompletableFuture<Post> result = postService.create(request);

        Post actualPost = result.get();
        actualPost.setTimeCreated(null);

        assertNotNull("",result.get().toString());
        assertEquals(post, actualPost);
        verify(userRepository, times(1)).findById(creatorId);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testUpdate() throws ExecutionException, InterruptedException {
        int postId = 1;
        String title = "Updated post";
        String content = "Updated content";
        Date updatedTime = new Date();

        UpdatePostRequest request = new UpdatePostRequest();
        request.setPostId(postId);
        request.setTitle(title);
        request.setContent(content);
        request.setTimeCreated(updatedTime);

        Optional<Post> post = Optional.of(new Post());
        Post updatedPost = new Post();
        updatedPost.setTitle(title);
        updatedPost.setContent(content);
        updatedPost.setLikes(0);
        updatedPost.setTimeCreated(updatedTime);

        when(postRepository.findById(postId)).thenReturn(post);
        when(postRepository.save(updatedPost)).thenReturn(updatedPost);

        CompletableFuture<Post> result = postService.update(request);

        Post actualPost = result.get();

        assertEquals(title, actualPost.getTitle());
        assertEquals(content, actualPost.getContent());
        assertEquals(0, actualPost.getLikes());
        assertEquals(updatedTime, actualPost.getTimeCreated());
    }

    @Test
    void testDelete() {
        Integer postId = 1;

        postService.delete(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }

}

