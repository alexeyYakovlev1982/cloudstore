package com.alexeyy.cloudstore.client;

import com.alexeyy.cloudstore.client.messages.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Controller {

    @FXML
    TextField chosenFileTF;

    @FXML
    Button sendFileBtn;

    @FXML
    Button refreshBtn;

    @FXML
    Button deleteBtn;

    @FXML
    Button downloadBtn;

    @FXML
    Button deleteAllBtn;

    @FXML
    TextArea clientTextArea;

    @FXML
    ListView<String> serverLV;

    @FXML
    TextField loginTF;

    @FXML
    TextField passwordTF;

    @FXML
    Text serverText;


    final FileChooser fileChooser = new FileChooser();
    public Stage primaryStage = Main.primaryStage;
    File chosenFileToSend;

    private ObservableList<String> serverFilesList;


    public void sendFileBtnClickReaction() {

//        serverFilesList = FXCollections.observableArrayList ("file1", "file2", "file3", "file4");
//        serverFilesList.add("file5");
//        serverLV.setItems(serverFilesList);

        if (loginTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
            clientTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": ERROR - please enter correct login & password.\n");
        } else {
            if (chosenFileTF.getText().equals("")) {
                clientTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": ERROR - please choose the file to upload.\n");
            } else {
//                MessageSender.sendMessage(new FileOutMessage("test.txt", loginTF.getText(), passwordTF.getText()), clientTextArea);
//                System.out.println(">>>>>>> " + chosenFileTF.getText());
//                MessageSender.sendMessage(new FileOutMessage(chosenFileTF.getText(), loginTF.getText(), passwordTF.getText()), clientTextArea);
//                new FileOutMessage(chosenFileTF.getText(), loginTF.getText(), passwordTF.getText()).send(clientTextArea);
                new FileOutMessage(chosenFileTF.getText(), loginTF.getText(), passwordTF.getText()).sendNetty(clientTextArea);
//                serverLV.refresh();
            }
        }
        refreshBtnReaction();
    }

    public void refreshBtnReaction() {
        if (loginTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
            clientTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": ERROR - please enter correct login & password.\n");
        } else {
            if (chosenFileTF.getText().equals("")) {
                clientTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": ERROR - please choose the file to upload.\n");
            } else {
                GetListOfFilesOutMessage getListOfFilesMessage = new GetListOfFilesOutMessage(loginTF.getText(), passwordTF.getText());
                getListOfFilesMessage.sendNetty(clientTextArea);

                serverFilesList = FXCollections.observableArrayList(getListOfFilesMessage.getListOfServerFiles());
                serverLV.setItems(serverFilesList);

                serverLV.refresh();
                refreshBtn.setDisable(false);
                //downloadBtn.setDisable(false);
                //deleteBtn.setDisable(false);
                deleteAllBtn.setDisable(false);
                serverText.setText("Server Files List:");
                sendFileBtn.setDisable(false);
            }
        }
    }

    public void deleteAllBtnReaction() {
        if (loginTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
            clientTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": ERROR - please enter correct login & password.\n");
        } else {
            DeleteAllFilesOutMessage deletaAllFilesMessage = new DeleteAllFilesOutMessage(loginTF.getText(), passwordTF.getText());
            deletaAllFilesMessage.sendNetty(clientTextArea);
            refreshBtnReaction();
        }
    }

    public void deleteBtnReaction() {
        if (loginTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
            clientTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": ERROR - please enter correct login & password.\n");
        } else {
            DeleteFileOutMessage deleteFileMessage = new DeleteFileOutMessage(loginTF.getText(), passwordTF.getText(), serverLV.getSelectionModel().getSelectedItem());
            deleteFileMessage.sendNetty(clientTextArea);
            refreshBtnReaction();
        }
        downloadBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }

    public void downloadBtnReaction(){
        if (loginTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
            clientTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": ERROR - please enter correct login & password.\n");
        } else {
            DownloadOutMessage downloadMessage = new DownloadOutMessage(loginTF.getText(), passwordTF.getText(), serverLV.getSelectionModel().getSelectedItem());
            downloadMessage.sendNetty(clientTextArea);



            String[] msgParts = downloadMessage.getServerReplyStr().split(" ");
            String msgFileName = msgParts[2];

            int msgServiceDataLen = 3 + msgParts[1].length() + 1 + msgFileName.length() + 1;
            int fileDataLen = downloadMessage.getServerReplyStr().length() - msgServiceDataLen;
            byte[] msgFileDateBytes = new byte[fileDataLen];

            System.arraycopy(downloadMessage.getServerReplyStr().getBytes(), msgServiceDataLen, msgFileDateBytes, 0, fileDataLen);
            //msgFileDataStr = new String(msgFileDateBytes);

            StoredFile storedFile = new StoredFile(msgFileName);
            storedFile.saveFile(msgFileDateBytes);

            clientTextArea.appendText(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": FILE saved - " + storedFile.getFilePath() + "\n");

            refreshBtnReaction();
        }



        downloadBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }

    public void chooseFileBtnClickReaction() {
        chosenFileToSend = fileChooser.showOpenDialog(primaryStage);
        chosenFileTF.clear();
        if (chosenFileToSend != null) {
            chosenFileTF.setText(chosenFileToSend.getAbsolutePath());
        }
        clientTextArea.appendText("\n" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": chosen file: " + chosenFileToSend.getAbsolutePath() + "\n");
    }


    public void serverLVClicked() {
        downloadBtn.setDisable(false);
        deleteBtn.setDisable(false);
        clientTextArea.appendText("\n" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": chosen server file: " + serverLV.getSelectionModel().getSelectedItem() + "\n");
    }


}
