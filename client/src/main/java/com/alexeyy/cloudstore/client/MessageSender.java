package com.alexeyy.cloudstore.client;

import com.alexeyy.cloudstore.client.messages.OutMessage;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MessageSender {

    public static void sendMessage(OutMessage messageToSend, TextArea logTextArea) {
        logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": sending to server: " + messageToSend.getMessageStr() + "\n");

        try {
            Socket socket = new Socket("localhost", 8189);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            out.writeInt(messageToSend.messageBytes.length);
            out.write(messageToSend.messageBytes);
            out.flush();

//            try {
//                System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + " waiting for reply from server...");
//                while (true) {
//                    String s = in.readUTF();
//                    logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + " reply from server: " + s);
//                }
//            } catch (IOException e) {
//                logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": server does not reply.");
//            } finally {
//
//            }

//            // waiting for server reply message in a separate thread:
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": waiting for reply from server...");
//                        while (true) {
//                            String s = in.readUTF();
//                            logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": reply from server: " + s);
//                        }
//                    } catch (IOException e) {
//                        logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": server does not reply.");
//                    } finally {
//                        try {
//                            out.close();
//                            in.close();
//                            socket.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//            t.setDaemon(true);
//            t.start();

//            t.join();
//            TimeUnit.SECONDS.sleep(5);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }


//            out.close();
//            in.close();
//            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void sendMessage(OutMessage messageToSend, TextArea logTextArea){
//        logTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": sending to server: " + messageToSend.getMessageStr() + "\n");
//        sendMessage(messageToSend);
//    }


}
