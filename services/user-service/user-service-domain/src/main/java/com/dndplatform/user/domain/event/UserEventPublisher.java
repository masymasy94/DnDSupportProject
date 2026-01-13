package com.dndplatform.user.domain.event;

public interface UserEventPublisher {

    void publishUserRegistered(UserRegisteredEvent event);
}
