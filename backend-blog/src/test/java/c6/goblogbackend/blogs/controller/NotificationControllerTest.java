package c6.goblogbackend.blogs.controller;

import c6.goblogbackend.blogs.model.Notification;
import c6.goblogbackend.blogs.service.proxy.NotificationProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    private NotificationController notificationController;

    @Mock
    private NotificationProxy notificationProxy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationController = new NotificationController(notificationProxy);
    }

    @Test
    void getNewNotification_ValidUserIdAndJwt_ReturnsNotifications() {
        // Arrange
        int userId = 1;
        String jwt = "valid-jwt";

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

        when(notificationProxy.handleReadNew(jwt, userId)).thenReturn(expectedNotifications);

        // Act
        ResponseEntity<List<Notification>> response = notificationController.getNewNotification(userId, jwt);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedNotifications, response.getBody());
        verify(notificationProxy, times(1)).handleReadNew(jwt, userId);
    }

    @Test
    void getOldNotification_ValidUserIdAndJwt_ReturnsNotifications() {
        // Arrange
        int userId = 1;
        String jwt = "valid-jwt";

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

        when(notificationProxy.handleReadOld(jwt, userId)).thenReturn(expectedNotifications);

        // Act
        ResponseEntity<List<Notification>> response = notificationController.getOldNotification(userId, jwt);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedNotifications, response.getBody());
        verify(notificationProxy, times(1)).handleReadOld(jwt, userId);
    }
}