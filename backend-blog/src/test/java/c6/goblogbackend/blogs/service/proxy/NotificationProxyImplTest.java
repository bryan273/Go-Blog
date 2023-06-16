package c6.goblogbackend.blogs.service.proxy;

import c6.goblogbackend.blogs.dto.AuthenticationResponse;
import c6.goblogbackend.blogs.exception.UserNotExistException;
import c6.goblogbackend.blogs.model.Notification;
import c6.goblogbackend.blogs.model.User;
import c6.goblogbackend.blogs.repository.UserRepository;
import c6.goblogbackend.blogs.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationProxyImplTest {

    private NotificationProxyImpl notificationProxy;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResponseEntity<AuthenticationResponse> mockResponseEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationProxy = new NotificationProxyImpl(notificationService, userRepository);
        notificationProxy.setRestTemplate(mock(RestTemplate.class));
        notificationProxy.setAUTH_SERVICE_BASE_URL("http://example.com");
        notificationProxy.setAPI_KEY("api-key");
    }

    @Test
    void handleReadOld_ValidTokenAndUserId_ReturnsNotifications() {
        // Arrange
        String jwt = "valid-jwt";
        int userId = 1;

        User user = User.builder().id(userId).build();

        List<Notification> expectedNotifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setNotificationId(1);
        notification1.setCommentContent("Comment 1");
        notification1.setPostId(1);
        expectedNotifications.add(notification1);

        Notification notification2 = new Notification();
        notification2.setNotificationId(2);
        notification2.setCommentContent("Comment 2");
        notification2.setPostId(2);
        expectedNotifications.add(notification2);

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(notificationProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);

        when(notificationService.getPastNotifications(userId)).thenReturn(expectedNotifications);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        // Act
        List<Notification> notifications = notificationProxy.handleReadOld(jwt, userId);

        // Assert
        assertNotNull(notifications);
        assertEquals(expectedNotifications.size(), notifications.size());
        assertEquals(expectedNotifications.get(0).getNotificationId(), notifications.get(0).getNotificationId());
        assertEquals(expectedNotifications.get(0).getCommentContent(), notifications.get(0).getCommentContent());
        assertEquals(expectedNotifications.get(0).getPostId(), notifications.get(0).getPostId());
        assertEquals(expectedNotifications.get(1).getNotificationId(), notifications.get(1).getNotificationId());
        assertEquals(expectedNotifications.get(1).getCommentContent(), notifications.get(1).getCommentContent());
        assertEquals(expectedNotifications.get(1).getPostId(), notifications.get(1).getPostId());
        verify(notificationService, times(1)).getPastNotifications(userId);
    }

    @Test
    void handleReadOld_InvalidUserId_ThrowsUserNotExistException() {
        // Arrange
        String jwt = "valid-jwt";
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotExistException.class, () -> notificationProxy.handleReadOld(jwt, userId));
        verify(notificationService, never()).getPastNotifications(userId);
    }

    @Test
    void handleReadNew_ValidTokenAndUserId_ReturnsNotifications() {
        // Arrange
        String jwt = "valid-jwt";
        int userId = 1;

        User user = User.builder().id(userId).build();

        List<Notification> expectedNotifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setNotificationId(1);
        notification1.setCommentContent("Comment 1");
        notification1.setPostId(1);
        expectedNotifications.add(notification1);

        Notification notification2 = new Notification();
        notification2.setNotificationId(2);
        notification2.setCommentContent("Comment 2");
        notification2.setPostId(2);
        expectedNotifications.add(notification2);

        AuthenticationResponse responseBody = AuthenticationResponse.builder()
                                                                    .isAuthenticated(true)
                                                                    .userId(1)
                                                                    .username("bryan")
                                                                    .build(); 

        when(notificationProxy.authenticateJWT(jwt)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(responseBody);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(notificationService.getNotifications(userId)).thenReturn(expectedNotifications);

        // Act
        List<Notification> notifications = notificationProxy.handleReadNew(jwt, userId);

        // Assert
        assertNotNull(notifications);
        assertEquals(expectedNotifications.size(), notifications.size());
        assertEquals(expectedNotifications.get(0).getNotificationId(), notifications.get(0).getNotificationId());
        assertEquals(expectedNotifications.get(0).getCommentContent(), notifications.get(0).getCommentContent());
        assertEquals(expectedNotifications.get(0).getPostId(), notifications.get(0).getPostId());
        assertEquals(expectedNotifications.get(1).getNotificationId(), notifications.get(1).getNotificationId());
        assertEquals(expectedNotifications.get(1).getCommentContent(), notifications.get(1).getCommentContent());
        assertEquals(expectedNotifications.get(1).getPostId(), notifications.get(1).getPostId());
        verify(notificationService, times(1)).getNotifications(userId);
    }

    @Test
    void handleReadNew_InvalidUserId_ThrowsUserNotExistException() {
        // Arrange
        String jwt = "valid-jwt";
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotExistException.class, () -> notificationProxy.handleReadNew(jwt, userId));
        verify(notificationService, never()).getNotifications(userId);
    }

    @Test
    void checkUserExistance_UserExists_DoesNotThrowException() {
        // Arrange
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertDoesNotThrow(() -> notificationProxy.checkUserExistance(userId));
    }

    @Test
    void checkUserExistance_UserDoesNotExist_ThrowsUserNotExistException() {
        // Arrange
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotExistException.class, () -> notificationProxy.checkUserExistance(userId));
    }
}
