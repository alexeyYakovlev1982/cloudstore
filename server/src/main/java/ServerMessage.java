import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerMessage {
    protected String msgStr;
    private byte[] msgBytes;

    private enum MessageType {COMMAND, TEST, FILE, FILEPART, UNDEFINED}

    ;
    private MessageType msgType;
    private int msgId;
    private String msgLogin;
    private String msgPassword;
    private String[] msgParts;
    private String msgFileName;
    byte[] msgFileDateBytes;
    private String msgFileDataStr;
    private String msgCmd;
    private String msgStatus;
    private String msgReply;
    private byte[] msgReplyBytes;

    private CloudStoreUser msgUser;

    public String getMsgReply() {
        msgReply = "/a " + msgId + " " + msgStatus;
        return msgReply;
    }

    public byte[] getMsgReplyBytes() {
        //msgReply = "/a " + msgId + " " + msgStatus;
        //if (msgReplyBytes == null) {
        //    return msgReply.getBytes();
        // } else return msgReplyBytes;

        msgReply = "/a " + msgId + " " + msgStatus;
        return msgReply.getBytes();
    }

    public ServerMessage(byte[] msgBytes) {
        this.msgBytes = msgBytes;
        msgStr = new String(msgBytes);

        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": Incoming msg detected: " + msgStr);
        //System.out.println(msgStr);

        msgStatus = "NOT_OK";
        decodeMessage();
        if (msgAuthCheck()) {
            msgStatus = "AUTH_OK";
            //msgReply = "/a " + msgId + " " + msgStatus;
            executeMsg();
        } else {
            msgStatus = "AUTH_NOT_OK";
            //msgReply = "/a " + msgId + " " + msgStatus;
        }

    }


    public void decodeMessage() {
        msgParts = msgStr.split(" ");

        switch (msgParts[0]) {
            case "/t":
                msgType = MessageType.TEST;
                break;
            case "/c":
                msgType = MessageType.COMMAND;
                break;
            case "/f":
                msgType = MessageType.FILE;
                break;
            case "/p":
                msgType = MessageType.FILEPART;
                break;
            default:
                msgType = MessageType.UNDEFINED;
                break;
        }

        if (msgType != MessageType.UNDEFINED) {
            msgId = Integer.parseInt(msgParts[1]);
            msgLogin = msgParts[2];
            msgPassword = msgParts[3];
        }

        if (msgType == MessageType.FILE) {
            msgFileName = msgParts[4];
            int msgServideDataLen = 3 + msgParts[1].length() + 1 + msgLogin.length() + 1 + msgPassword.length() + 1 + msgFileName.length() + 1;
            int fileDataLen = msgBytes.length - msgServideDataLen;
            msgFileDateBytes = new byte[fileDataLen];
            System.arraycopy(msgBytes, msgServideDataLen, msgFileDateBytes, 0, fileDataLen);
            msgFileDataStr = new String(msgFileDateBytes);
        }

        if (msgType == MessageType.COMMAND) {
            msgCmd = msgParts[4];
        }
        //printoutMsgeFields();
    }

    public void printoutMsgeFields() {
        System.out.println("msgType:" + msgType);
        System.out.println("msgId:" + msgId);
        System.out.println("msgLogin:" + msgLogin);
        System.out.println("msgPassword:" + msgPassword);
        System.out.println("msgFileName:" + msgFileName);
        System.out.println("msgFileDataStr:" + msgFileDataStr);
    }

    public boolean msgAuthCheck() {
        msgUser = new CloudStoreUser(msgLogin);
        return msgUser.checkAuth(msgPassword);
    }

    public void executeMsg() {
        switch (msgType) {
            case COMMAND:
                if (msgCmd.equals("list")) {
                    msgStatus = "";
                    System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": got LIST command ");
                    try {
                        String[] directories = new File(msgUser.getStorageFolder()).list();
                        for (int i = 0; i < directories.length; i++) {
                            System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": DIR: " + directories[i]);
                            msgStatus = msgStatus + " " + directories[i];
                            //msgReply = "/a " + msgId + " " + msgStatus;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": LIST command failed");
                        msgStatus = "LIST_NOT_OK";
                        //msgReply = "/a " + msgId + " " + msgStatus;
                    }


                } else if (msgCmd.equals("deleteall")) {
                    System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": got DELETEALL command ");
                    try {
                        for (File file : new java.io.File(msgUser.getStorageFolder()).listFiles())
                            if (!file.isDirectory())
                                file.delete();
                        msgStatus = "DELETEALL_OK";
                        //msgReply = "/a " + msgId + " " + msgStatus;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": DELETEALL command failed");
                        msgStatus = "DELETEALL_NOT_OK";
                        //msgReply = "/a " + msgId + " " + msgStatus;
                    }
                } else if (msgCmd.equals("delete")) {
                    try {
                        msgFileName = msgParts[5];
                        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": got DELETE file command for " + msgFileName);
                        new StoredFile(msgUser.getStorageFolder() + msgFileName).deleteFile();
                        msgStatus = "DELETE_FILE_OK " + msgFileName;
                        //msgReply = "/a " + msgId + " " + msgStatus;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": DELETE file command failed");
                        msgStatus = "DELETE_FILE_NOT_OK";
                        //msgReply = "/a " + msgId + " " + msgStatus;
                    }
                } else if (msgCmd.equals("download")) {
                    try {
                        msgFileName = msgParts[5];
                        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": got DOWNLOAD file command for " + msgFileName);
                        StoredFile storedFile = new StoredFile(msgUser.getStorageFolder() + msgFileName);
//                        storedFile.getFileBytes();
//
//
//                        //msgReply = "/a " + msgId + " " + msgFileName + " ";
//                        byte[] messageBytesServicePart = msgReply.getBytes();
//
//                        byte[] fileBytes = storedFile.getFileBytes();
//
//                        // concatanation : message service part and message file bytes part
//                        msgReplyBytes = new byte[messageBytesServicePart.length + fileBytes.length];
//                        System.arraycopy(messageBytesServicePart, 0, msgReplyBytes, 0, messageBytesServicePart.length);
//                        System.arraycopy(fileBytes, 0, msgReplyBytes, messageBytesServicePart.length, fileBytes.length);

                        msgStatus = msgFileName + " " + new String(storedFile.getFileBytes());


                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": DOWNLOAD file command failed");
                        msgStatus = "DOWNLOAD_FILE_NOT_OK";
                        //msgReply = "/a " + msgId + " " + msgStatus;
                    }
                }

                break;
            case FILE:
                try {
                    new StoredFile(msgUser.getStorageFolder() + msgFileName).saveFile(msgFileDateBytes);
                    System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": File " + msgFileName + " is saved at users " + msgLogin + " folder.");
                    msgStatus = "FILE_STORED_OK " + msgFileName;
                    //msgReply = "/a " + msgId + " " + msgStatus;
                } catch (Exception e) {
                    System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": File " + msgFileName + " is NOT saved at user's " + msgLogin + " folder.");
                    msgStatus = "FILE_STORED_NOT_OK";
                    //msgReply = "/a " + msgId + " " + msgStatus;
                }
                break;
            default:
                break;
        }
    }

}
