package com.dndplatform.chat.domain.repository;

import com.dndplatform.chat.domain.model.Message;

public interface MessageCreateRepository {
    Message create(Message message);
}
