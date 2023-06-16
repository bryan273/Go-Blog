package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.exception.PostDoesNotExistException;
import c6.goblogbackend.blogs.exception.UserNotExistException;
import c6.goblogbackend.blogs.model.Notification;
import c6.goblogbackend.blogs.repository.UserRepository;
import c6.goblogbackend.blogs.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationProxyImpl extends ProxyServiceBase implements NotificationProxy {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public List<Notification> handleReadOld(String jwt, int userId) {
        authenticateJWT(jwt);
        checkUserExistance(userId);

        return notificationService.getPastNotifications(userId);
    }

    @Override
    public List<Notification> handleReadNew(String jwt, int userId) {
        authenticateJWT(jwt);
        checkUserExistance(userId);

        return notificationService.getNotifications(userId);
    }

    void checkUserExistance(int id) throws PostDoesNotExistException{
        var user = userRepository.findById(id);
        if (!user.isPresent()) throw new UserNotExistException();
    }


}
