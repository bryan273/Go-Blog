package c6.goblogbackend.blogs.controller;

import c6.goblogbackend.blogs.model.Notification;
import c6.goblogbackend.blogs.service.proxy.NotificationProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationProxy notificationProxy;


    @Operation(summary = "Get new notification for a specific userId")
    @GetMapping("/get_new_notification/{userId}")
    public ResponseEntity<List<Notification>> getNewNotification(@Parameter(description = "id of user to be showed new notifications") @PathVariable Integer userId, @RequestHeader("X-JWT-TOKEN") String jwt) {
        List<Notification> response;
        response = notificationProxy.handleReadNew(jwt, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get old notification for a specific userId")
    @GetMapping("/get_old_notification/{userId}")
    public ResponseEntity<List<Notification>> getOldNotification(@Parameter(description = "id of user to be showed old notifications") @PathVariable Integer userId, @RequestHeader("X-JWT-TOKEN") String jwt) {
        List<Notification> response;
        response = notificationProxy.handleReadOld(jwt, userId);
        return ResponseEntity.ok(response);
    }

}
