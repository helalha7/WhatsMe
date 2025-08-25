package com.whatsme.messages_service.message.models;

public class Message {
    private String content;
    private String to;
    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
