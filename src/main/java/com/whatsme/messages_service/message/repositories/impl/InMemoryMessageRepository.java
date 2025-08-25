package com.whatsme.messages_service.message.repositories.impl;

import com.whatsme.messages_service.message.models.Message;
import com.whatsme.messages_service.message.repositories.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryMessageRepository implements MessageRepository {
    private List<Message> messages = new ArrayList<>();

    @Override
    public void saveMessage(Message message) {
        messages.add(message);
    }

    @Override
    public List<Message> getAllMessages() {
        return messages;
    }
}
