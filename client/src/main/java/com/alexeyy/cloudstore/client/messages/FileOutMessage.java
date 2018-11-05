package com.alexeyy.cloudstore.client.messages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileOutMessage extends OutMessage {
    File msgFile;
    byte[] fileBytes;

    public FileOutMessage(String fileName, String login, String password) {
        this.fileName = fileName;
        msgFile = new File(fileName);
        msgLogin = login;
        msgPassword = password;
        createFileMsg();

//        messageStr = "/f " + OutMessage.getNextMessageId() + " " + login + " " + password + " " + fileName + " ";
//        byte[] messageBytesServicePart = messageStr.getBytes();
//
//        try {
//            fileBytes = Files.readAllBytes(msgFile.toPath());
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//
//        // concatanation : message service part and message file bytes part
//        messageBytes = new byte[messageBytesServicePart.length + fileBytes.length];
//        System.arraycopy(messageBytesServicePart, 0, messageBytes, 0, messageBytesServicePart.length);
//        System.arraycopy(fileBytes, 0, messageBytes, messageBytesServicePart.length, fileBytes.length);
    }


    public FileOutMessage(File file, String login, String password) {
        msgFile = file;
        msgLogin = login;
        msgPassword = password;
        createFileMsg();
    }


    public void createFileMsg() {
        messageStr = "/f " + OutMessage.getNextMessageId() + " " + msgLogin + " " + msgPassword + " " + msgFile.getName() + " ";
        byte[] messageBytesServicePart = messageStr.getBytes();

        try {
            fileBytes = Files.readAllBytes(msgFile.toPath());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // concatanation : message service part and message file bytes part
        messageBytes = new byte[messageBytesServicePart.length + fileBytes.length];
        System.arraycopy(messageBytesServicePart, 0, messageBytes, 0, messageBytesServicePart.length);
        System.arraycopy(fileBytes, 0, messageBytes, messageBytesServicePart.length, fileBytes.length);
    }


    public static void createTestFile() {
//                creating test file to send
        File fileOut = new File("test.txt");
        try {
            String stringToWrite = "12345678";
            Files.write(fileOut.toPath(), stringToWrite.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
