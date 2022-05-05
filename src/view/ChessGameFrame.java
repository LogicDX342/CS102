package view;

import controller.GameController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    // public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    private JLabel statusLabel;

    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project"); // 设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addStartGameButtom();
        addRankButtom();
        addSignInButtom();
        addSignUpButtom();
        
    }

    private void addStartGameButtom() {
        JButton button = new JButton("Start Game");
        button.setLocation(WIDTH/2-100, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Start game");
            getContentPane().removeAll();
            addChessboard();
            addLabel();
            addLoadButton();
            addSaveButton();
            addResetButton();
            repaint();
        });
    }
    private void addRankButtom(){
        JButton button = new JButton(new ImageIcon(" images"));
        button.setLocation(WIDTH/2-100, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
        });
    }
    private void addSignInButtom(){
        JButton button = new JButton("Sign in");
        button.setLocation(10, 10);
        button.setSize(50, 50);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
        });
    }
    private void addSignUpButtom(){
        JButton button = new JButton("Sign up");
        button.setLocation(80,  10 );
        button.setSize(50, 50);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            JOptionPane.showInputDialog(this, "UserName");
        });
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        add(chessboard);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        statusLabel = new JLabel("WHITE");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    public void changeLable(String color) {
        statusLabel.setText(color);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    // private void addHelloButton() {
    // JButton button = new JButton("Show Hello Here");
    // button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello,
    // world!"));
    // button.setLocation(HEIGTH, HEIGTH / 10 + 120);
    // button.setSize(200, 60);
    // button.setFont(new Font("Rockwell", Font.BOLD, 20));
    // add(button);
    // }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            JFileChooser jfc = new JFileChooser("resource");
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("txt", "txt");
            jfc.addChoosableFileFilter(fnef);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                gameController.loadGameFromFile(jfc.getSelectedFile());
            }
        });
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click save");
            JFileChooser jfc = new JFileChooser("resource");
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("txt", "txt");
            jfc.addChoosableFileFilter(fnef);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                gameController.saveGameToFile(jfc.getSelectedFile());
            }

        });
    }

    private void addResetButton() {
        JButton button = new JButton("Reset");
        button.setLocation(HEIGTH, HEIGTH / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click reset");
            if (JOptionPane.showConfirmDialog(this, "Confirm to reset?", "Comfirm", 0) == JOptionPane.YES_OPTION) {
                gameController.resetGame();
            }
        });
    }

}
