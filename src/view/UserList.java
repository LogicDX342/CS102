package view;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import model.User;

public class UserList {
    private ArrayList<User> userList = new ArrayList<>();

    public UserList() {
        File userList = new File("./resource/user");
        File[] users = userList.listFiles();
        for (File user : users) {
            try {
                Reader reader = new FileReader(user.getPath());
                char[] buffer = new char[200];
                reader.read(buffer);
                loadUserList(new String(buffer).split("&#"),user);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean signUp(String userName, String password, File avatar) {
        File userData = new File("./resource/user/" + userName + ".txt");
        if (userData.exists()) {
            return false;
        }
        User user = new User(userName, password, avatar, getUserList().size());
        userList.add(user);
        return true;
    }

    private void loadUserList(String[] profile,File userData) throws IOException {
        userList.add(new User(profile[0], profile[1], profile[2], profile[3], profile[4],userData));
    }

    public ArrayList<User> getUserList() {
        return this.userList;
    }

    public JFrame showRank() {
        JFrame rank = new JFrame();
        JPanel rankPanel = new JPanel();
        JLabel userRank;
        rank.setSize(300, 500);
        rank.setResizable(false);
        rank.setLocationRelativeTo(null);
        rank.add(rankPanel);
        rankPanel.setLayout(new GridLayout(11, 3, 5, 5));
        String[] sorted = new String[10 < getUserList().size() ? 10 : getUserList().size()];
        rankPanel.add(new JLabel());
        rankPanel.add(new JLabel("Username"));
        rankPanel.add(new JLabel("Win times"));

        float upperBound = 1000;
        float lowerBound = -1;
        User user;
        for (int i = 0; i < sorted.length; i++) {
            for (int j = 0; j < sorted.length; j++) {
                user = getUserList().get(j);
                if (user.getWinTimes() >= lowerBound && user.getWinTimes() < upperBound) {
                    lowerBound = user.getWinTimes();
                }
            }
            upperBound = lowerBound;
            for (int j = 0; j < sorted.length; j++) {
                user = getUserList().get(j);
                if (user.getWinTimes() == lowerBound) {
                    try {
                        userRank = new JLabel(new ImageIcon(
                                ImageIO.read(user.getAvatar()).getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
                        rankPanel.add(userRank);
                        userRank = new JLabel(user.getUserName());
                        userRank.setFont(new Font("Rockwell", Font.BOLD, 15));
                        rankPanel.add(userRank);
                        userRank = new JLabel(String.valueOf(user.getWinTimes()));
                        userRank.setFont(new Font("Rockwell", Font.BOLD, 15));
                        rankPanel.add(userRank);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            lowerBound = -1;
        }
        for (int i = 0; i < (10 - sorted.length) * 3; i++) {
            rankPanel.add(new JLabel());
        }
        return rank;
    }
}
