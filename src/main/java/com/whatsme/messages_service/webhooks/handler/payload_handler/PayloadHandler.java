package com.whatsme.messages_service.webhooks.handler.payload_handler;

import com.whatsme.messages_service.webhooks.dtos.GenericWhatsAppPayLoadDTO;

public interface PayloadHandler {
    void handlePayload(GenericWhatsAppPayLoadDTO whatsAppPayLoadDTO);
}
