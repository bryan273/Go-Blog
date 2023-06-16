package c6.goblogbackend.blogs.repository;

import c6.goblogbackend.blogs.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @NonNull
    Optional<Notification> findById(Integer notificationId);

    @Query(value="SELECT * FROM notifications_ WHERE is_notified=FALSE and subscriber_id=?1", nativeQuery = true)
    List<Notification> getNotifications(Integer subscriberId);

    @Query(value="SELECT * FROM notifications_ WHERE is_notified=TRUE and subscriber_id=?1", nativeQuery = true)
    List<Notification> getPastNotifications(Integer subscriberId);
}
