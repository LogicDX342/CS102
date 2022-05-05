package controller;

import view.Chessboard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;

public class GameController {
    private Chessboard chessboard;

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public List<String> loadGameFromFile(File save) {
        try {
            List<String> chessData = Files.readAllLines(save.toPath());
            chessboard.loadGame(chessData);
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
            for(int i=0;i<chessboard.getChessComponents().length;i++){
                for(int j=0;j<chessboard.getChessComponents()[i].length;j++){
                    writer.write(chessboard.getChessComponents()[i][j].getName()+" ");
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

    public void resetGame() {
        chessboard.resetGame();
    }

    public String[] getSaveList() {
        File file = new File("resource");
        String[] saveList = file.list();
        for (int i = 0; i < saveList.length; i++) {
            saveList[i] = saveList[i].substring(0, saveList[i].lastIndexOf("."));
        }
        return saveList;
    }

}
