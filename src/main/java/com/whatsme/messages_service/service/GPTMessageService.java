// src/main/java/com/whatsme/messages_service/service/GPTMessageService.java
package com.whatsme.messages_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.whatsme.messages_service.config.CourseSettings;
import org.springframework.stereotype.Service;

@Service
public class GPTMessageService {

    private final OpenAIClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String systemInstructions;

    public GPTMessageService(OpenAIClient client, CourseSettings course) {
        this.client = client;
        this.systemInstructions =
                "You are the assistant for a mathematics course. " +
                        "Course title: " + course.getTitle() + ". " +
                        "Price: $" + course.getPrice() + ". " +
                        "Start date: " + course.getStartDate() + ". " +
                        "Duration: " + course.getDurationMonths() + " months. " +
                        "Daily schedule: " + course.getHours() + ". " +
                        "Answer questions accurately and only about this course unless the user asks for something else.";
    }

    /** Simple helper you can reuse anywhere */
    public String complete(String prompt) {
        ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-4.1")
                .instructions(systemInstructions)
                .input(prompt)
                .build();

        Response r = client.responses().create(params);
        return extractText(r);
    }


    private String extractText(Response r) {
        // turn the typed POJO into a JSON tree we can safely traverse
        JsonNode root = mapper.valueToTree(r);

        // 1) direct convenience field, if present
        JsonNode outputText = root.path("output_text");
        if (outputText.isTextual()) {
            String s = outputText.asText();
            return s;
        }

        // 2) your structure: output[0].content[0].text
        JsonNode txt = root.path("output").path(0).path("content").path(0).path("text");
        if (txt.isTextual()) {
            String s = txt.asText();
            return s;
        }

        // 3) fallback: pretty JSON so you can see what's returned
        String fallback = root.toPrettyString();
        return fallback;
    }

}
