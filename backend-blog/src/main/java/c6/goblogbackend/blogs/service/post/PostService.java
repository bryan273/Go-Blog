package c6.goblogbackend.blogs.service.post;

import c6.goblogbackend.blogs.dto.CreatePostRequest;
import c6.goblogbackend.blogs.dto.GetPostResponse;
import c6.goblogbackend.blogs.dto.SearchPostRequest;
import c6.goblogbackend.blogs.dto.UpdatePostRequest;
import c6.goblogbackend.blogs.model.Post;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PostService {
    List<Post> findAll();
    GetPostResponse findById(Integer id);
    CompletableFuture<Post> create(CreatePostRequest request);
    CompletableFuture<Post> update(UpdatePostRequest request);
    void delete(Integer id);
    List<Post> findByTitleIgnoreCase(SearchPostRequest request);
}
