package com.whatsme.messages_service.worker;

import com.whatsme.messages_service.message.models.Message;
import com.whatsme.messages_service.message.repositories.MessageRepository;
import com.whatsme.messages_service.service.GPTMessageService;
import com.whatsme.messages_service.service.MessageAggregator;
import com.whatsme.messages_service.webhooks.dtos.GenericMessageDTO;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class InboundJobRunner {
    private MessageRepository messageRepository;
    private final GPTMessageService gptMessageService;
    private final MessageAggregator messageAggregator;
    public InboundJobRunner(MessageRepository messageRepository,
                            GPTMessageService gptMessageService,
                            MessageAggregator messageAggregator) {
        this.messageRepository = messageRepository;
        this.gptMessageService = gptMessageService;
        this.messageAggregator = messageAggregator;
    }
    @Async
    public void process(String businessId, GenericMessageDTO dto) {
        var inbound = dto.messages.getFirst();
        var from = inbound.from;
        var to   = inbound.to;
        System.out.println(businessId);

        Message message = new Message();
        message.setContent(inbound.text.get("body").asText());
        message.setFrom(from);
        message.setTo(to);
        messageRepository.saveMessage(message);

        messageAggregator.onIncoming(from, businessId, message.getContent());
    }


//    private void sendMessage(String toPhoneNumber, String fromPhoneNumber, String message) {
//        RestClient client = RestClient.create();
//        client
//                .post()
//                .uri("https://graph.facebook.com/v23.0/"+fromPhoneNumber+"/messages")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer EAAjgyZBYaOQkBPRg9QQhOqSUQVJPkBZANwKjCIcgVTBy1ouw7bjTHil70eAz4BvlTDFOyzx2loWi2ctZBJn3P0XYb3nXrV11iL6rWi5HTiYIM7rTZC2zGR2Fh6WbmSUqkZCrl6wkKOPkWTCNjZAuGdg7GFQhXdODwwtXZCzRNPjAJDJxNHp76TwMt5PfudyZAHDYIAZDZD")
//                .body(
//                        Map.of(
//                                "messaging_product","whatsapp",
//                                "recipient_type","individual",
//                                "to",toPhoneNumber,
//                                "type","text",
//                                "text", Map.of(
//                                        "body",message
//                                )
//                        )
//                ).retrieve().body(String.class);
//    }
}
