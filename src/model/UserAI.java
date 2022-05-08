package model;

import java.io.File;
import java.io.IOException;

public class UserAI extends User {

    public UserAI() {
        super("userAI", "userAI", new File("Chess/resource/image/aiAvatar.png"), -1);
    }

    public UserAI(String userName, String password, String avatar, String ID, String winTimes) throws IOException {
        super(userName, password, avatar, ID, winTimes);
    }

}
