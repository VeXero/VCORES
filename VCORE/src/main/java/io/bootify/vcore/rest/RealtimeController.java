package io.bootify.vcore.rest;

import io.bootify.vcore.model.NotificationDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Simple STOMP message controller that accepts inbound messages from clients
 * and broadcasts them to the `/topic/notifications` destination.
 *
 * This demonstrates bidirectional realtime capability: clients can send to `/app/notify`
 * and will receive messages on `/topic/notifications`.
 */
@Controller
public class RealtimeController {

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public NotificationDTO handleNotify(@Payload NotificationDTO notification) {
        // Here you could enrich notification (add server timestamp, validate, persist, etc.)
        return notification;
    }
}

