package com.whatsme.messages_service.message.controller;

import com.whatsme.messages_service.message.models.Message;
import com.whatsme.messages_service.message.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    private MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/api/messages")
    public List<Message> getAllMessages() {
        return messageRepository.getAllMessages();
    }
}
