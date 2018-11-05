package com.alexeyy.cloudstore.client.messages;

import java.util.ArrayList;

public class GetListOfFilesOutMessage extends OutMessage {

    public GetListOfFilesOutMessage(String login, String password) {
        msgLogin = login;
        msgPassword = password;
        createMsg();
    }


    public void createMsg() {
        messageStr = "/c " + OutMessage.getNextMessageId() + " " + msgLogin + " " + msgPassword + " list";
        messageBytes = messageStr.getBytes();
    }

    public ArrayList<String> getListOfServerFiles() {
        ArrayList<String> listOfServerFiles = new ArrayList<String>();
        String[] strArrlistOfServerFiles = serverReplyStr.split(" ");
        for (int i = 2; i < strArrlistOfServerFiles.length; i++) {
            if (strArrlistOfServerFiles[i].length() > 0) {
                listOfServerFiles.add(strArrlistOfServerFiles[i]);
            }
        }
        return listOfServerFiles;
    }

}
