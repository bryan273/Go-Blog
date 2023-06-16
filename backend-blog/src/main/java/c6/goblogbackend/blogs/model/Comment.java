package c6.goblogbackend.blogs.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comment", columnList = "post"),
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @JsonIncludeProperties({"id", "username"})
    @ManyToOne
    @JoinColumn(name="creator", referencedColumnName="user_id")
    private User creator;

    @Column(name="post")
    private Integer postId;

    @Column(name = "time_created")
    private Date timeCreated;

    @Column(name = "content")
    private String content;


}

