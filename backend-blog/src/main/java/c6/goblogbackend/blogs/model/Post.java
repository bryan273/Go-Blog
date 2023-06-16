package c6.goblogbackend.blogs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "posts" , indexes = {
        @Index(name = "idx_post", columnList = "post_id"),
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @JsonIncludeProperties({"id", "username"})
    @ManyToOne
    @JoinColumn(name="creator", referencedColumnName="user_id")
    private User creator;


    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "likes")
    private int likes;

    @Column(name = "time_created")
    private Date timeCreated;

} 