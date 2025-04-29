package org.health.se7a.notifications;


public interface MessageProvider {

    MessageResponse sendMessage(MessageRequest request);

}