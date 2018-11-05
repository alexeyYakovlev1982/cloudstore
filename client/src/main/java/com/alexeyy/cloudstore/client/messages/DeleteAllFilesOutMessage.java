package com.alexeyy.cloudstore.client.messages;

public class DeleteAllFilesOutMessage extends OutMessage {

    public DeleteAllFilesOutMessage(String login, String password) {
        msgLogin = login;
        msgPassword = password;
        createMsg();
    }

    public void createMsg() {
        messageStr = "/c " + OutMessage.getNextMessageId() + " " + msgLogin + " " + msgPassword + " deleteall";
        messageBytes = messageStr.getBytes();
    }
}
