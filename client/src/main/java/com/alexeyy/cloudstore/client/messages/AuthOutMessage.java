package com.alexeyy.cloudstore.client.messages;

public class AuthOutMessage extends OutMessage {
    private String login;
    private String password;

    public AuthOutMessage(String login, String password) {
        this.login = login;
        this.password = password;
        messageStr = "/a " + login + " " + password;
    }
}
