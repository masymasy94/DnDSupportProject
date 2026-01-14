package com.dndplatform.user.domain.event;

public interface EmailSendRepository {

    void sendEmail(UserRegisteredEvent event);
}
