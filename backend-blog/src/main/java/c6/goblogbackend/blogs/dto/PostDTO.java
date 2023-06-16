package c6.goblogbackend.blogs.dto;


import c6.goblogbackend.blogs.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import c6.goblogbackend.blogs.model.Post;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Post post;
    private List<Comment> comments;
}
