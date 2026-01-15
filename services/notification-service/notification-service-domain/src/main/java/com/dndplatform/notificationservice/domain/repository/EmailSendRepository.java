package com.dndplatform.notificationservice.domain.repository;

import com.dndplatform.notificationservice.domain.model.Email;

public interface EmailSendRepository {
    void send(Email email);
}
