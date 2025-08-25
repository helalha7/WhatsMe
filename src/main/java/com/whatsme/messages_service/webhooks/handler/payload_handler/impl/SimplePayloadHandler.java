package com.whatsme.messages_service.webhooks.handler.payload_handler.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.whatsme.messages_service.webhooks.dtos.GenericMessageDTO;
import com.whatsme.messages_service.webhooks.dtos.GenericWhatsAppPayLoadDTO;
import com.whatsme.messages_service.webhooks.handler.payload_handler.PayloadHandler;
import com.whatsme.messages_service.worker.InboundJobRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SimplePayloadHandler implements PayloadHandler {

    private InboundJobRunner inboundJobRunner;

    @Autowired
    public SimplePayloadHandler(InboundJobRunner inboundJobRunner) {
        this.inboundJobRunner = inboundJobRunner;
    }

    @Override
    public void handlePayload(GenericWhatsAppPayLoadDTO whatsAppPayLoadDTO) {
        GenericWhatsAppPayLoadDTO.Value value = whatsAppPayLoadDTO.entry.getFirst().changes.getFirst().value;
        Map<String, JsonNode> extras = value.extras;
        if(extras.containsKey("messages") && extras.containsKey("contacts")) {
            JsonNode contactsJson = extras.get("contacts");
            JsonNode messagesJson = extras.get("messages");
            String name = contactsJson.get(0).get("profile").get("name").asText();
            String wa_id = contactsJson.get(0).get("wa_id").asText();
            String from = messagesJson.get(0).get("from").asText();
            String id = messagesJson.get(0).get("id").asText();
            String timestamp = messagesJson.get(0).get("timestamp").asText();
            String type = messagesJson.get(0).get("type").asText();

            //creating contacts list:
            GenericMessageDTO.Contact.Profile profile = new GenericMessageDTO.Contact.Profile();
            profile.name = name;
            GenericMessageDTO.Contact contact = new GenericMessageDTO.Contact();
            contact.profile = profile;
            contact.wa_id = wa_id;
            List<GenericMessageDTO.Contact> contacts = new ArrayList<>();
            contacts.add(contact);

            //creating messages list:
            GenericMessageDTO.Message message = new GenericMessageDTO.Message();
            message.from = from;
            message.to = value.metadata.display_phone_number;
            message.id = id;
            message.timestamp = timestamp;
            message.type = type;
            if(type.equals("text"))
                message.text = messagesJson.get(0).get("text");
            List<GenericMessageDTO.Message> messages = new ArrayList<>();
            messages.add(message);

            //creating GenericMessageDTO object
            GenericMessageDTO genericMessageDTO = new GenericMessageDTO();
            genericMessageDTO.contacts = contacts;
            genericMessageDTO.messages = messages;

            String businessId = value.metadata.phone_number_id;
            //handoff
            inboundJobRunner.process(businessId, genericMessageDTO);
            System.out.println("handler");

        }
        if(whatsAppPayLoadDTO.entry.getFirst().changes.getFirst().value.extras.containsKey("statuses")) {

        }
    }
}
