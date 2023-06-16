package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.CreatePostRequest;
import c6.goblogbackend.blogs.dto.GetPostResponse;
import c6.goblogbackend.blogs.dto.SearchPostRequest;
import c6.goblogbackend.blogs.dto.UpdatePostRequest;
import c6.goblogbackend.blogs.exception.PostCreationException;
import c6.goblogbackend.blogs.exception.PostDoesNotExistException;
import c6.goblogbackend.blogs.model.Post;
import c6.goblogbackend.blogs.repository.PostRepository;
import c6.goblogbackend.blogs.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class PostProxyImpl extends ProxyServiceBase implements PostProxy {

    private final PostRepository postRepository;
    private final PostService postService;

    @Override
    public Post handleCreate(String jwt, CreatePostRequest request) {
        authenticateJWT(jwt);
        Post result = null;

        try {
            result =  postService.create(request).get();
        } catch (ExecutionException err) {
            throw new PostCreationException("Post creation execution error", err.getCause());
        } catch (InterruptedException err) {
            Thread.currentThread().interrupt();
            throw new PostCreationException("Post creation interrupted", err);
        }

        return result;
    }

    @Override
    public List<Post> handleSearch(String jwt, SearchPostRequest request) {
        authenticateJWT(jwt);

        return postService.findByTitleIgnoreCase(request);
    }

    public Post handleUpdate(String jwt, UpdatePostRequest request) {
        authenticateJWT(jwt);
        if (isPostDoesNotExist(request.getPostId())) {
            throw new PostDoesNotExistException(request.getPostId());
        }

        Post updated;
        try {
            updated = postService.update(request).get();
        } catch (ExecutionException err) {
            throw new PostCreationException("Post update execution error", err.getCause());
        } catch (InterruptedException err) {
            Thread.currentThread().interrupt();
            throw new PostCreationException("Post update interrupted", err);
        }
        return updated;
    }

    @Override
    public void handleDelete(String jwt, int postId) {
        authenticateJWT(jwt);
        if (isPostDoesNotExist(postId)) {
            throw new PostDoesNotExistException(postId);
        }
        postService.delete(postId);
    }

    @Override
    public List<Post> handleReadAll(String jwt) {
        authenticateJWT(jwt);
        return postService.findAll();
    }

    @Override
    public GetPostResponse handleRead(String jwt, int postId) {
        authenticateJWT(jwt);
        if (isPostDoesNotExist(postId)) {
            throw new PostDoesNotExistException(postId);
        }
        return postService.findById(postId);
    }

    public boolean isPostDoesNotExist(Integer id) {
        return postRepository.findById(id).isEmpty();
    }
}
