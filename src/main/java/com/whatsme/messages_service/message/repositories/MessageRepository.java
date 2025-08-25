package com.whatsme.messages_service.message.repositories;

import com.whatsme.messages_service.message.models.Message;

import java.util.List;

public interface MessageRepository {
    void saveMessage(Message message);
    List<Message> getAllMessages();

}
