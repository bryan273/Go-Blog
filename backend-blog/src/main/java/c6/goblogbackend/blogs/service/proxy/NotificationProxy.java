package c6.goblogbackend.blogs.service.proxy;
import c6.goblogbackend.blogs.model.Notification;

import java.util.List;

public interface NotificationProxy {
    List<Notification> handleReadOld(String jwt, int postId);
    List<Notification> handleReadNew(String jwt, int postId);
}
