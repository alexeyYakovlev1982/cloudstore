package com.alexeyy.cloudstore.client.messages;

public class DeleteFileOutMessage extends OutMessage {

    public DeleteFileOutMessage(String login, String password, String fileName) {
        this.fileName = fileName;
        msgLogin = login;
        msgPassword = password;
        createMsg();
    }

    public void createMsg() {
        messageStr = "/c " + OutMessage.getNextMessageId() + " " + msgLogin + " " + msgPassword + " delete " + fileName;
        messageBytes = messageStr.getBytes();
    }
}
