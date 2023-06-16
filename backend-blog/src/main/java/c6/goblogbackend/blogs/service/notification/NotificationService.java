package c6.goblogbackend.blogs.service.notification;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification create(CreateCommentRequest request);
    List<Notification> getNotifications(Integer userId);
    List<Notification> getPastNotifications(Integer userId);
}
