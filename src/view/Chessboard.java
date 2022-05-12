package view;

import model.*;
import controller.ClickController;
import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.WHITE;
    // all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private ArrayList<String> steps = new ArrayList<String>();
    private ChessGameFrame chessGameFrame;
    private GameController gameController;
    private Timer timer;
    private boolean delay;

    public Chessboard(int width, int height, ChessGameFrame chessGameFrame) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        this.chessGameFrame = chessGameFrame;

        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

        initiateEmptyChessboard();

        // FIXME: Initialize chessboard for testing only.
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);

        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);
        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);
        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);
        initKingOnBoard(0, 4, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);
        initPawnOnBoard(1, 0, ChessColor.BLACK);
        initPawnOnBoard(1, 1, ChessColor.BLACK);
        initPawnOnBoard(1, 2, ChessColor.BLACK);
        initPawnOnBoard(1, 3, ChessColor.BLACK);
        initPawnOnBoard(1, 4, ChessColor.BLACK);
        initPawnOnBoard(1, 5, ChessColor.BLACK);
        initPawnOnBoard(1, 6, ChessColor.BLACK);
        initPawnOnBoard(1, 7, ChessColor.BLACK);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 0, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 1, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 2, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 3, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 4, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 5, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 6, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 7, ChessColor.WHITE);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2, boolean online) {

        saveStepToFile(chess1, chess2);

        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController,
                    CHESS_SIZE));

        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;

        // \ chess1.paintImmediately(chess1.getX(), chess1.getY(), chess1.getWidth(),
        // chess1.getHeight());
        // chess2.paintImmediately(chess2.getX(), chess2.getY(), chess2.getWidth(),
        // chess2.getHeight());
        chess1.repaint();
        chess2.repaint();
        if (online) {
            gameController.sendStep(steps.get(steps.size() - 1));
        }

        chessGameFrame.musicPlayer("chessSound.wav", false);
    }

    public void saveStepToFile(ChessComponent first, ChessComponent chessComponent) {
        String step = first.getChessColor().getName().substring(0, 1) + first.getType()
                + String.valueOf((char) (first.getChessboardPoint().getX() + 97))
                + String.valueOf(first.getChessboardPoint().getY())
                + String.valueOf((char) (chessComponent.getChessboardPoint().getX() + 97))
                + String.valueOf(chessComponent.getChessboardPoint().getY());
        steps.add(step);
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {

                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController,
                        CHESS_SIZE));
            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
        chessGameFrame.changeColorLabel(currentColor.toString());
        chessGameFrame.addCountdownLabel(false);
        chessGameFrame.addCountdownLabel(true);
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col),
                color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col),
                calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col),
                calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col),
                color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col),
                color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    private void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col),
                color, clickController, CHESS_SIZE);

        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void showTargeted(ChessComponent chess) {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                if (chess.canMoveTo(chessComponents, new ChessboardPoint(i, j))
                        && !chessComponents[i][j].getChessColor().equals(chess.getChessColor()))
                    chessComponents[i][j].setTargeted(true);
                chessComponents[i][j].repaint();
            }
        }
    }

    public void hideTargeted() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                chessComponents[i][j].setTargeted(false);
                chessComponents[i][j].repaint();
            }
        }
    }

    Thread thread;

    public void loadGame(List<String> chessData) {
        resetGame();
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                for (String arg : chessData) {
                    if (!arg.equals("end")) {
                        if (!arg.matches("^[WB][BKNPQR][a-h][0-7][a-h][0-7]")) {
                            JOptionPane.showMessageDialog(chessGameFrame, "Wrong save");
                            break;
                        }
                        Color color;
                        if (arg.charAt(0) == 'B') {
                            color = Color.BLACK;
                        } else {
                            color = Color.WHITE;
                        }
                        if (getCurrentColor().getColor().equals(color)) {
                            int col1 = arg.charAt(2) - 'a', row1 = arg.charAt(3) - '0';
                            int col2 = arg.charAt(4) - 'a', row2 = arg.charAt(5) - '0';
                            ChessComponent chess1 = chessComponents[col1][row1], chess2 = chessComponents[col2][row2];
                            if (chess1.getChessColor() != getCurrentColor()
                                    || chess2.getChessColor() == getCurrentColor() ||
                                    !chess1.canMoveTo(getChessComponents(), chess2.getChessboardPoint())) {
                                JOptionPane.showMessageDialog(chessGameFrame, "Wrong save");
                                break;
                            }
                            if (delay) {
                                thread = new Thread() {
                                    @Override
                                    public void run() {
                                        swapChessComponents(chess1, chess2, false);
                                        swapColor();
                                    }
                                };
                                thread.start();
                                try {
                                    TimeUnit.SECONDS.sleep(2);
                                    // Thread.sleep(delay);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                swapChessComponents(chess1, chess2, false);
                                swapColor();
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                setDelay(false);
            }
        };
        thread2.start();
    }

    public void setDelay(boolean set) {
        delay = set;
    }

    public void resetGame() {
        initiateEmptyChessboard();
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);
        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);
        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);
        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);
        initKingOnBoard(0, 4, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);
        initPawnOnBoard(1, 0, ChessColor.BLACK);
        initPawnOnBoard(1, 1, ChessColor.BLACK);
        initPawnOnBoard(1, 2, ChessColor.BLACK);
        initPawnOnBoard(1, 3, ChessColor.BLACK);
        initPawnOnBoard(1, 4, ChessColor.BLACK);
        initPawnOnBoard(1, 5, ChessColor.BLACK);
        initPawnOnBoard(1, 6, ChessColor.BLACK);
        initPawnOnBoard(1, 7, ChessColor.BLACK);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 0, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 1, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 2, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 3, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 4, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 5, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 6, ChessColor.WHITE);
        initPawnOnBoard(CHESSBOARD_SIZE - 2, 7, ChessColor.WHITE);
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                chessComponents[i][j].repaint();
            }
        }
        steps = new ArrayList<>();
        currentColor = ChessColor.WHITE;
        chessGameFrame.addCountdownLabel(false);
        chessGameFrame.addCountdownLabel(true);
        chessGameFrame.changeColorLabel(currentColor.toString());

    }
}
