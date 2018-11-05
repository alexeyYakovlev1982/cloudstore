import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CloudStoreUser {
    private String login;
    private String password;
    private String storageFolder;
    private String rootFolder = "cloud_store/";

    public CloudStoreUser(String login) {
        this.login = login;
        this.password = getPassword();
        createStorageFolder();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        // return password;
        // db check must be here
        return "456";
    }

    public String getStorageFolder() {
        // db check must be here
        // return storageFolder;
        return rootFolder + this.login + "/";
    }

    public void createStorageFolder(){
        try {
            Files.createDirectories(Paths.get(rootFolder));
        } catch (IOException e) {
            System.out.println((new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": cannot create dir " + rootFolder));
        }
        try {
            Files.createDirectories(Paths.get(rootFolder + login));
        } catch (IOException e) {
            System.out.println((new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ": cannot create dir " + rootFolder + login));
        }

    }


    public boolean checkAuth(String password) {
        return password.equals(this.getPassword());
    }

}
