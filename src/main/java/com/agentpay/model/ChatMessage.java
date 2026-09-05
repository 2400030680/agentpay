package com.agentpay.model;

public class ChatMessage {
    private String sender; // "user" or "agent"
    private String message;
    private Object payload; // Can hold a Product or Checkout details

    public ChatMessage() {}

    public ChatMessage(String sender, String message, Object payload) {
        this.sender = sender;
        this.message = message;
        this.payload = payload;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
