package com.company.aiassistant.model;

/**
 * Represents a chat message.
 */
public class ChatMessage {

    private MessageType type;

    private String content;

    public ChatMessage() {
    }

    public ChatMessage(
            MessageType type,
            String content) {

        this.type = type;
        this.content = content;
    }

    public MessageType getType() {

        return type;
    }

    public void setType(
            MessageType type) {

        this.type = type;
    }

    public String getContent() {

        return content;
    }

    public void setContent(
            String content) {

        this.content = content;
    }
}