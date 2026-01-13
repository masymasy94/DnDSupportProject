package com.dndplatform.notificationservice.domain.repository;

import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailResult;

public interface EmailSendRepository {
    EmailResult send(Email email);
}
