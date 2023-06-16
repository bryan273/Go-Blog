package c6.goblogbackend.blogs.controller;

import c6.goblogbackend.blogs.dto.CreatePostRequest;
import c6.goblogbackend.blogs.dto.GetPostResponse;
import c6.goblogbackend.blogs.dto.SearchPostRequest;
import c6.goblogbackend.blogs.dto.UpdatePostRequest;
import c6.goblogbackend.blogs.model.Post;
import c6.goblogbackend.blogs.service.proxy.PostProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostProxy postProxyService;

    @Operation(summary = "Get all posts that exist")
    @GetMapping("/get_all_post")
    public ResponseEntity<List<Post>> getAllPost(@RequestHeader("X-JWT-TOKEN") String jwt) {
        List<Post> response;
        response = postProxyService.handleReadAll(jwt);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a post by its id")
    @GetMapping("/get_post/{id}")
    public ResponseEntity<GetPostResponse> getPostById(@RequestHeader("X-JWT-TOKEN") String jwt, @Parameter(description = "id of post to be showed") @PathVariable Integer id) {
        GetPostResponse response;
        response = postProxyService.handleRead(jwt, id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new post")
    @PostMapping("/create_post")
    public ResponseEntity<Post> addNewPost (
            @RequestHeader("X-JWT-TOKEN") String jwt, @RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postProxyService.handleCreate(jwt, request));
    }

    @Operation(summary = "Update a post")
    @PutMapping("/update_post")
    public ResponseEntity<Post> updatePost(@RequestHeader("X-JWT-TOKEN") String jwt, @RequestBody UpdatePostRequest request) {
        return ResponseEntity.ok(postProxyService.handleUpdate(jwt, request));
    }

    @Operation(summary = "Delete a post by its id")
    @DeleteMapping("/delete_post/{id}")
    public ResponseEntity<String> deletePost(@Parameter(description = "id of post to be deleted") @PathVariable Integer id, @RequestHeader("X-JWT-TOKEN") String jwt) {
        postProxyService.handleDelete(jwt, id);
        return ResponseEntity.ok(String.format("Deleted Post with id %d", id));
    }

    @Operation(summary = "Search posts by title")
    @PostMapping("/search")
    public ResponseEntity<List<Post>> searchPost(@RequestHeader("X-JWT-TOKEN") String jwt, @RequestBody SearchPostRequest request) {
        return ResponseEntity.ok(postProxyService.handleSearch(jwt, request));

    }

}

