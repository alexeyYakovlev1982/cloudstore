package com.alexeyy.cloudstore.client.messages;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

public class OutMessage {
    protected String messageStr;
    public byte[] messageBytes;
    protected static int messageId = 0;
    protected String msgLogin;
    protected String msgPassword;
    public static String serverIP = "localhost";
    public static int serverPort = 8189;
    protected byte[] serverReplyBytes[];

    public String getServerReplyStr() {
        return serverReplyStr;
    }

    protected String serverReplyStr;
    protected String fileName;

    public static int getMessageId() {
        return messageId;
    }

    public static void setMessageId(int messageId) {
        OutMessage.messageId = messageId;
    }

    public static int getNextMessageId() {
        messageId++;
        return messageId;
    }

    public String getMessageStr() {
        return messageStr + " ....";
//        return new String(messageBytes);
    }

    public void setMessageStr(String messageStr) {
        this.messageStr = messageStr;
    }

    public void send(TextArea logTextArea) {
        logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": sending to server: " + getMessageStr() + "\n");

        try {
            Socket socket = new Socket(serverIP, serverPort);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            out.writeInt(messageBytes.length);
            out.write(messageBytes);
            out.flush();

            try {
                logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": waiting for reply from server..." + "\n");
                //System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": waiting for reply from server...");
                String s = in.readUTF();
                logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": received reply from server: " + s + "\n");
            } catch (IOException e) {
                logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": server does not reply." + "\n");
            }

            out.close();
            in.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendNetty(TextArea logTextArea) {
        logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": sending to server: " + getMessageStr() + "\n");

        ObjectEncoderOutputStream oeos = null;
        ObjectDecoderInputStream odis = null;

        try (Socket socket = new Socket("localhost", 8189)) {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            oeos.writeInt(messageBytes.length);
            oeos.write(messageBytes);
            //oeos.writeObject(messageStr);
            oeos.flush();

            logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": waiting for reply from server..." + "\n");

            odis = new ObjectDecoderInputStream(socket.getInputStream());
            serverReplyStr = (String)odis.readObject();
            logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": received reply from server: " + serverReplyStr + "\n");
            System.out.println("Answer from server: " + serverReplyStr);



        } catch (Exception e) {
            logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": server does not reply." + "\n");
            e.printStackTrace();
        } finally {
            try {
                oeos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                odis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
