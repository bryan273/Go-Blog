package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.CreatePostRequest;
import c6.goblogbackend.blogs.dto.GetPostResponse;
import c6.goblogbackend.blogs.dto.SearchPostRequest;
import c6.goblogbackend.blogs.dto.UpdatePostRequest;
import c6.goblogbackend.blogs.model.Post;

import java.util.List;

public interface PostProxy {
    Post handleCreate(String jwt, CreatePostRequest request);
    List<Post> handleReadAll(String jwt);
    GetPostResponse handleRead(String jwt, int postId);
    Post handleUpdate(String jwt, UpdatePostRequest request);
    void handleDelete(String jwt, int postId);
    List<Post> handleSearch(String jwt, SearchPostRequest request);
}
