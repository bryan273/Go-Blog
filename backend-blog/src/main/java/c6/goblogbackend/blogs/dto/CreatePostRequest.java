package c6.goblogbackend.blogs.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    private int creatorId;
    private String creator;
    private String title;
    private String content;
    private int likes;
    private Date timeCreated;
}
