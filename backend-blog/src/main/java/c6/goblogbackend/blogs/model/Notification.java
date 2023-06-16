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
@Table(name = "notifications_", indexes = {
        @Index(name = "idx_notification", columnList = "subscriber_id,post"),
})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @Column(name="subscriber_id")
    private Integer subscriberId;

    @Column(name = "time_created")
    private Date timeCreated;

    @Column(name = "comment")
    private String commentContent;

    @Column(name="post")
    private Integer postId;

    @JsonIncludeProperties({"id", "username"})
    @ManyToOne
    @JoinColumn(name="creator", referencedColumnName="user_id")
    private User creator;

    @Column(name="is_notified")
    private boolean isNotified;
}
