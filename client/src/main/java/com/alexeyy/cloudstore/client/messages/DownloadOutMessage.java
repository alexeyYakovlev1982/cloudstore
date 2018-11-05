package com.alexeyy.cloudstore.client.messages;

public class DownloadOutMessage extends OutMessage {

    public DownloadOutMessage(String login, String password, String fileName) {
        this.fileName = fileName;
        msgLogin = login;
        msgPassword = password;
        createMsg();
    }

    public void createMsg() {
        messageStr = "/c " + OutMessage.getNextMessageId() + " " + msgLogin + " " + msgPassword + " download " + fileName;
        messageBytes = messageStr.getBytes();
    }
}
