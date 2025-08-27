// src/main/java/com/whatsme/messages_service/service/WhatsAppSender.java
package com.whatsme.messages_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class WhatsAppSender {
    //@Value("${whatsapp.phone-number-id}") String phoneNumberId; // numeric PHONE_NUMBER_ID
    @Value("${whatsapp.token}") String whatsappToken;           // system user token


    //private final RestClient client = RestClient.create();

    /** to = user phone E.164 digits (no '+'); from is ignored here since we use the configured PHONE_NUMBER_ID */
    public void sendText(String from,String to, String body) {


        RestClient client = RestClient.create();
        client
                .post()
                .uri("https://graph.facebook.com/v23.0/"+from+"/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+whatsappToken)
                .body(
                        Map.of(
                                "messaging_product","whatsapp",
                                "recipient_type","individual",
                                "to",to,
                                "type","text",
                                "text", Map.of(
                                        "body",body
                                )
                        )
                ).retrieve().body(String.class);
    }
}
