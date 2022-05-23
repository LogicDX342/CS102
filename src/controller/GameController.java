package controller;

import view.Chessboard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;
import model.ChessColor;
import model.ChessComponent;
import model.User;
import model.UserAI;

public class GameController {
    private Chessboard chessboard;
    private User userW;
    private User userB;
    private UserAI userAI;
    private Server server;
    private Client client;

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
        chessboard.setGameController(this);
    }

    public void serverStart() {
        server = new Server();
    }

    public void clientStart(String host) {
        client = new Client(host);
        Thread thread = new Thread() {
            @Override
            public void run() {
                move(client.receive(), false);
            }
        };
        thread.start();
    }

    public void sendStep(String step) {
        if (server != null) {
            server.send(step);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    move(server.receive(), false);
                }
            };
            thread.start();
        }
        if (client != null) {
            client.send(step);
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                move(client.receive(), false);
            }
        };
        thread.start();
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

    public void regretStep() {
        if (chessboard.getSteps().size() != 0) {
            chessboard.getSteps().remove(chessboard.getSteps().size() - 1);
            chessboard.loadGame(chessboard.getSteps());
        }
    }

    public int saveGameToFile(File save) {
        try {
            save.createNewFile();
            Writer writer;
            writer = new FileWriter(save);
            for (String str : chessboard.getSteps()) {
                writer.write(str + "\n");
            }
            writer.write("end\n");

            for (int i = 0; i < chessboard.getChessComponents().length; i++) {
                for (int j = 0; j < chessboard.getChessComponents()[i].length; j++) {
                    writer.write(chessboard.getChessComponents()[i][j].getChessColor().getName().substring(0, 1)
                            + chessboard.getChessComponents()[i][j].getType() + " ");
                }
                writer.write("\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(save + "is created");
        return 0;
    }

    public void replayGameFromFile(File save) {
        chessboard.setDelay(true);
        loadGameFromFile(save);
    }

    public void move(String step, boolean online) {
        // Color color;
        // if (step.charAt(0) == 'B') {
        // color = Color.BLACK;
        // } else {
        // color = Color.WHITE;
        // }
        // if (chessboard.getCurrentColor().getColor().equals(color)) {
        int col1 = step.charAt(2) - 'a', row1 = step.charAt(3) - '0';
        int col2 = step.charAt(4) - 'a', row2 = step.charAt(5) - '0';
        ChessComponent chess1 = chessboard.getChessComponents()[col1][row1],
                chess2 = chessboard.getChessComponents()[col2][row2];
        chessboard.swapChessComponents(chess1, chess2, online);
        chessboard.swapColor();
        // }
    }

    public void forceSwapColor() {
        chessboard.swapColor();
    }
    public void setTheme(int theme){
        chessboard.setTheme(theme);
    }

    public void resetGame() {
        if (server != null) {
            server.close();
        }
        if (client != null) {
            client.close();
        }
        chessboard.resetGame();
    }

    public void setUser(User user) {
        if (userW == null) {
            this.userW = user;
        } else {
            this.userB = user;
        }
    }

    public void setHardness(int hardness) {
        UserAI ai = new UserAI();
        setUser(ai);
        switch (hardness) {
            case 0:
            case 1:
            case 2:
        }
    }

    public void winCount(ChessColor color) {
        if (color.equals(ChessColor.WHITE)) {
            if (userW != null) {
                userW.winCount();
            }
        } else {
            if (userB != null) {
                userB.winCount();
            }
        }
    }

}
