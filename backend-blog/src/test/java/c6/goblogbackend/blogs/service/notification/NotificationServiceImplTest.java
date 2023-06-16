package c6.goblogbackend.blogs.service.notification;

import c6.goblogbackend.blogs.dto.CreateCommentRequest;
import c6.goblogbackend.blogs.model.Notification;
import c6.goblogbackend.blogs.repository.NotificationRepository;
import c6.goblogbackend.blogs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    private NotificationServiceImpl notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationServiceImpl(notificationRepository, userRepository);
    }

    @Test
    void create_ValidRequest_ReturnsCreatedNotification() {
        // Arrange
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(1);
        request.setContent("This is a test comment.");
        request.setCreatorId(1);
        request.setSubscriberId(2);

        Notification expectedNotification = new Notification();
        expectedNotification.setNotificationId(1);
        expectedNotification.setTimeCreated(new Date());
        expectedNotification.setCommentContent(request.getContent());
        expectedNotification.setPostId(request.getPostId());
        expectedNotification.setCreator(null); // Mocking the user retrieval
        expectedNotification.setSubscriberId(request.getSubscriberId());
        expectedNotification.setNotified(false);

        when(userRepository.findById(request.getCreatorId())).thenReturn(Optional.empty());
        when(notificationRepository.save(any(Notification.class))).thenReturn(expectedNotification);

        // Act
        Notification createdNotification = notificationService.create(request);

        // Assert
        assertNotNull(createdNotification);
        assertEquals(expectedNotification.getNotificationId(), createdNotification.getNotificationId());
        assertEquals(expectedNotification.getTimeCreated(), createdNotification.getTimeCreated());
        assertEquals(expectedNotification.getCommentContent(), createdNotification.getCommentContent());
        assertEquals(expectedNotification.getPostId(), createdNotification.getPostId());
        assertEquals(expectedNotification.getCreator(), createdNotification.getCreator());
        assertEquals(expectedNotification.getSubscriberId(), createdNotification.getSubscriberId());
        assertEquals(expectedNotification.isNotified(), createdNotification.isNotified());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void getNotifications_ValidUserId_ReturnsNotifications() {
        // Arrange
        int userId = 1;

        List<Notification> expectedNotifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setNotificationId(1);
        notification1.setCommentContent("Comment 1");
        notification1.setSubscriberId(userId);
        expectedNotifications.add(notification1);

        Notification notification2 = new Notification();
        notification2.setNotificationId(2);
        notification2.setCommentContent("Comment 2");
        notification2.setSubscriberId(userId);
        expectedNotifications.add(notification2);

        when(notificationRepository.getNotifications(userId)).thenReturn(expectedNotifications);

        // Act
        List<Notification> notifications = notificationService.getNotifications(userId);

        // Assert
        assertNotNull(notifications);
        assertEquals(expectedNotifications.size(), notifications.size());
        assertEquals(expectedNotifications.get(0).getNotificationId(), notifications.get(0).getNotificationId());
        assertEquals(expectedNotifications.get(0).getCommentContent(), notifications.get(0).getCommentContent());
        assertEquals(expectedNotifications.get(0).getSubscriberId(), notifications.get(0).getSubscriberId());
        assertEquals(expectedNotifications.get(1).getNotificationId(), notifications.get(1).getNotificationId());
        assertEquals(expectedNotifications.get(1).getCommentContent(), notifications.get(1).getCommentContent());
        assertEquals(expectedNotifications.get(1).getSubscriberId(), notifications.get(1).getSubscriberId());
        verify(notificationRepository, times(1)).getNotifications(userId);
    }

    @Test
    void getPastNotifications_ValidUserId_ReturnsNotifications() {
        // Arrange
        int userId = 1;

        List<Notification> expectedNotifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setNotificationId(1);
        notification1.setCommentContent("Comment 1");
        notification1.setSubscriberId(userId);
        expectedNotifications.add(notification1);

        Notification notification2 = new Notification();
        notification2.setNotificationId(2);
        notification2.setCommentContent("Comment 2");
        notification2.setSubscriberId(userId);
        expectedNotifications.add(notification2);

        when(notificationRepository.getPastNotifications(userId)).thenReturn(expectedNotifications);

        // Act
        List<Notification> notifications = notificationService.getPastNotifications(userId);

        // Assert
        assertNotNull(notifications);
        assertEquals(expectedNotifications.size(), notifications.size());
        assertEquals(expectedNotifications.get(0).getNotificationId(), notifications.get(0).getNotificationId());
        assertEquals(expectedNotifications.get(0).getCommentContent(), notifications.get(0).getCommentContent());
        assertEquals(expectedNotifications.get(0).getSubscriberId(), notifications.get(0).getSubscriberId());
        assertEquals(expectedNotifications.get(1).getNotificationId(), notifications.get(1).getNotificationId());
        assertEquals(expectedNotifications.get(1).getCommentContent(), notifications.get(1).getCommentContent());
        assertEquals(expectedNotifications.get(1).getSubscriberId(), notifications.get(1).getSubscriberId());
        verify(notificationRepository, times(1)).getPastNotifications(userId);
    }
}
