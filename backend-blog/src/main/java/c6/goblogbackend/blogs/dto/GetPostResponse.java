package c6.goblogbackend.blogs.dto;

import c6.goblogbackend.blogs.model.Comment;
import c6.goblogbackend.blogs.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostResponse {
    private Post post;
    private List<Comment> comments;
}
