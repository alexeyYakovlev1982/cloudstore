package com.alexeyy.cloudstore.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StoredFile {
    private String fileName;
    private File localStoredFile;
    byte[] fileBytes;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public StoredFile(String fileName) {
        this.fileName = fileName;
    }

    public void saveFile(byte[] fileBytes){
        localStoredFile = new File(fileName);
        this.fileBytes = fileBytes;
        try {
        //    System.out.println(">>> Saving the file...");
            Files.write(localStoredFile.toPath(), this.fileBytes);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void deleteFile (){
        localStoredFile = new File(fileName);
        localStoredFile.delete();
    }

    public byte[] getFileBytes(){
        try {
            fileBytes = Files.readAllBytes(new File(fileName).toPath());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return fileBytes;
    }

    public String getFilePath(){
        return localStoredFile.getAbsolutePath();
    }

}
