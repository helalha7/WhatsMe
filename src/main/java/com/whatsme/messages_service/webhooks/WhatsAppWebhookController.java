package com.whatsme.messages_service.webhooks;

import com.whatsme.messages_service.webhooks.dtos.GenericWhatsAppPayLoadDTO;
import com.whatsme.messages_service.webhooks.handler.payload_handler.PayloadHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks/whatsapp")
public class WhatsAppWebhookController {

    private final String myToken = "helal212";
    private PayloadHandler payloadHandler;

    @Autowired
    public WhatsAppWebhookController(PayloadHandler payloadHandler) {
        this.payloadHandler = payloadHandler;
    }

    /***
     * <H1>Meta Verification</H1>
     * <p>Meta verification request, it only runs once</p>
     ***/
    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") int challenge,
            @RequestParam("hub.verify_token") String verifyToken
    ) {
        if(verifyToken.equals(myToken))
            return new ResponseEntity<>(challenge+"", HttpStatus.OK);
        return new ResponseEntity<>("failed to verify", HttpStatus.BAD_REQUEST);
    }


    /***
     * <H1>WhatsApp Events Listner</H1>
     * All the whatsapp events will come here
     * ***/
    @PostMapping
    public ResponseEntity<Void> listenForEvents(@RequestBody GenericWhatsAppPayLoadDTO payload) {
        payloadHandler.handlePayload(payload);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
