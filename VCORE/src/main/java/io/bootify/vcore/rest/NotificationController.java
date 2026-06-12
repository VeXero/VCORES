package io.bootify.vcore.rest;

import io.bootify.vcore.model.NotificationDTO;
import io.bootify.vcore.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody NotificationDTO notification) {
        notificationService.sendNotification(notification);
        return ResponseEntity.ok("Notification sent");
    }
}

