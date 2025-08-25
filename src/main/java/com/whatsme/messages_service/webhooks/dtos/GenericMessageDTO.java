package com.whatsme.messages_service.webhooks.dtos;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class GenericMessageDTO {
    public List<Contact> contacts;
    public List<Message> messages;

    public static class Contact {
        public Profile profile;
        public String wa_id;

        public static class Profile {
            public String name;

            @Override
            public String toString() {
                return "Profile{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "profile=" + profile +
                    ", wa_id='" + wa_id + '\'' +
                    '}';
        }
    }

    public static class Message {
        public String from;
        public String to;
        public String id;
        public String timestamp;
        public String type;

        public JsonNode text;        // e.g., { "body": "hi" }
        public JsonNode interactive; // e.g., { "type":"button_reply", "button_reply": {...} }
        public JsonNode image;       // if you need later
        public JsonNode document;    // if you need later
        public JsonNode context;     // { "id": "wamid.of.your.msg" } – useful to link replies

        @Override
        public String toString() {
            return "Message{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", id='" + id + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", type='" + type + '\'' +
                    ", text=" + text +
                    ", interactive=" + interactive +
                    ", image=" + image +
                    ", document=" + document +
                    ", context=" + context +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GenericMessageDTO{" +
                "contacts = { "+ contacts+
                ", messages=" + messages +
                '}';
    }
}
