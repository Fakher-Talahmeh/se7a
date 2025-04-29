package org.health.se7a.notifications;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private MessageStatus status;
    private String uuid;
}
