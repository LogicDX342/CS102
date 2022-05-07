package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class User {
    private File avatar;
    private String userName;
    private String password;
    private int winTimes;
    private File userData;
    private int ID;

    public User(String userName, String password, File avatar, int ID) {
        this.userName = userName;
        this.password = password;
        this.avatar = avatar;
        try {
            this.ID = ID;
            userData = new File("Chess/resource/user/" + userName + ".txt");
            userData.createNewFile();
            Writer writer = new FileWriter(userData);
            writer.write(userName + "&#" + password + "&#" + avatar.getAbsolutePath() + "&#" + ID + "&#" + winTimes);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public User(String userName, String password, String avatar, String ID, String winTimes) throws IOException {
        this.userName = userName;
        this.password = password;
        this.avatar = new File(avatar);
        this.ID = Integer.parseInt(ID);
        this.winTimes = Integer.parseInt(winTimes);
    }

    public void winCount() {
        winTimes++;
        Writer writer;
        try {
            writer = new FileWriter(userData);
            writer.write(userName + "&#" + password + "&#" + avatar.getAbsolutePath() + "&#" + ID + "&#" + winTimes);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public File getAvatar() {
        return avatar;
    }

    public int getWinTimes() {
        return winTimes;
    }

    public boolean checkPassword(String password) {
        if(this.password.equals(password)){
            return true;
        }
        return false;
    }
}
