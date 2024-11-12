package com.example.stressApp.Model;

public class MessageModel {
    private final String message;
    private final String role;

    public MessageModel(String message, String role) {
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

}

