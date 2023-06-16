package c6.goblogbackend.blogs.controller;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.service.proxy.CommentProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = {"*"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor

public class CommentController {

    private final CommentProxy commentProxyService;

    @Operation(summary = "Get a comment by its id")
    @GetMapping("/id/{id}")
    public ResponseEntity<List<Comment>> getCommentById(@Parameter(description = "id of comment to be showed") @PathVariable Integer id, @RequestHeader("X-JWT-TOKEN") String jwt) {
        List<Comment> response;
        response = commentProxyService.handleReadAll(jwt, id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new comment")
    @PostMapping("/create")
    public ResponseEntity<Comment> addComment (
            @RequestHeader("X-JWT-TOKEN") String jwt, @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(commentProxyService.handleCreate(jwt, request));
    }

    @Operation(summary = "Delete a comment by its id")
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@Parameter(description = "id of comment to be deleted") @PathVariable Integer commentId, @RequestHeader("X-JWT-TOKEN") String jwt) {
        commentProxyService.handleDelete(jwt, commentId);
        return ResponseEntity.ok(String.format("Deleted Comment with id %d", commentId));
    }
}

