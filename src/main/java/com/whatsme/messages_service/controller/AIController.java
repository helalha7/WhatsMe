// src/main/java/com/whatsme/messages_service/controller/AIController.java
package com.whatsme.messages_service.controller;

import com.openai.client.OpenAIClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIController {
    private final OpenAIClient client;

    public AIController(OpenAIClient client) {
        this.client = client;
    }

    @GetMapping("/unicorn")
    public String unicorn() {
        Response r = client.responses().create(
                ResponseCreateParams.builder()
                        .model("gpt-4.1")
                        .input("Tell me a three sentence bedtime story about a unicorn.")
                        .build()
        );
        return r.toString(); // keep it simple; no getOutput() in 3.x
    }
}
