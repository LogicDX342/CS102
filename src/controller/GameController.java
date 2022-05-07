package controller;

import view.Chessboard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;

import model.ChessColor;
import model.User;
import model.UserAI;

public class GameController {
    private Chessboard chessboard;
    private User userW;
    private User userB;
    private UserAI userAI;

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public List<String> loadGameFromFile(File save) {
        try {
            List<String> chessData = Files.readAllLines(save.toPath());
            // chessboard.loadGame(chessData);
            return chessData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int saveGameToFile(File save) {
        try {
            save.createNewFile();
            Writer writer;
            writer = new FileWriter(save);
            for (String str : chessboard.getSteps()) {
                writer.write(str);
            }
            for (int i = 0; i < chessboard.getChessComponents().length; i++) {
                for (int j = 0; j < chessboard.getChessComponents()[i].length; j++) {
                    writer.write(chessboard.getChessComponents()[i][j].getName() + " ");
                }
                writer.write("\r\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(save + "is created");
        return 0;
    }
    public void forceSwapColor(){
        chessboard.swapColor();
    }

    public void resetGame() {
        chessboard.resetGame();
    }

    public void setUser(User userW, User userB) {
        this.userW = userW;
        this.userB = userB;
    }

    public void setUser(User userW) {
        this.userW = userW;
        this.userB = userAI;
    }

    public void winCount(ChessColor color) {
        if (color.equals(ChessColor.WHITE)) {
            userW.winCount();
        } else {
            userB.winCount();
        }
    }

}
