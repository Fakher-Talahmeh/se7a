package org.health.se7a.notifications;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocalMessageProvider implements MessageProvider {
    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        return MessageResponse.builder()
                .status(MessageStatus.DELIVERED)
                .uuid(UUID.randomUUID().toString())
                .build();
    }
}
