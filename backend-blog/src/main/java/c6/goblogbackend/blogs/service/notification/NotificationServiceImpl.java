package c6.goblogbackend.blogs.service.notification;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Notification;
import c6.goblogbackend.blogs.repository.NotificationRepository;
import c6.goblogbackend.blogs.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public Notification create(CreateCommentRequest request) {

        var user = userRepository.findById(request.getCreatorId()).orElse(null);

        var notification = Notification.builder()
                .timeCreated(new Date())
                .commentContent(request.getContent())
                .postId(request.getPostId())
                .creator(user)
                .subscriberId(request.getSubscriberId())
                .isNotified(false)
                .build();
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(Integer userId) {

        List<Notification> notifications =  notificationRepository.getNotifications(userId);
        Thread updateNotification = new Thread(() -> {

                notifications.forEach(notif -> notif.setNotified(true));
                
                notificationRepository.saveAllAndFlush(notifications);
        });
        
        updateNotification.start();
        return notifications;
    }


    public List<Notification> getPastNotifications(Integer userId) {
    
        return notificationRepository.getPastNotifications(userId);
    }
}
