package model;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class User {
    private Image avatar;
    private String userName;
    private int winTimes;

    public User (String userName) throws IOException{
        this.userName=userName;
        avatar=ImageIO.read(new File("images/avatars"));
    }
    public void winCount(){
        winTimes++;
    }
    public int getWinTimes(){
        return winTimes;
    }
}
