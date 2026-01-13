package com.dndplatform.notificationservice.domain;

import com.dndplatform.notificationservice.domain.model.Email;
import com.dndplatform.notificationservice.domain.model.EmailResult;

public interface SendEmailService {
    EmailResult send(Email email);
}
