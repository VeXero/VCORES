package io.bootify.vcore.service;

import io.bootify.vcore.model.NotificationDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Send a notification to the public notifications topic.
     */
    public void sendNotification(NotificationDTO notification) {
        try {
            messagingTemplate.convertAndSend("/topic/notifications", notification);
        } catch (Exception e) {
            System.err.println("Failed to send notification via WebSocket: " + e.getMessage());
        }
    }
}

