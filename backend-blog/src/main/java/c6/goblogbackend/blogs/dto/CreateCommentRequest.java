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
public class CreateCommentRequest {
    private String content;
    private String creator;
    private int creatorId;
    private int postId;
    private Date timeCreated;
    private int subscriberId;

}
