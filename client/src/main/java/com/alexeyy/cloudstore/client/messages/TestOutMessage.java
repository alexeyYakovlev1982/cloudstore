package com.alexeyy.cloudstore.client.messages;

public class TestOutMessage extends OutMessage {
    public TestOutMessage(String testMessageStr, String login, String password) {
        messageStr = "/t " + OutMessage.getNextMessageId() + " " + login + " " + password + " " + testMessageStr;
        messageBytes = messageStr.getBytes();
    }
}
