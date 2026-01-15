package com.dndplatform.notificationservice.domain;

import com.dndplatform.notificationservice.domain.model.Email;

public interface SendEmailService {
    void send(Email email);
}
