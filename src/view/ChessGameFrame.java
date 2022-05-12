package view;

import controller.GameController;
import model.User;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

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
    private UserList userList = new UserList();
    private JPanel menu;
    private JPanel game;
    private JPanel setting;
    private CardLayout card = new CardLayout();
    private File background;
    private javax.swing.Timer timer;
    private JLabel countdownLabel = new JLabel();

    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project"); // 设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;
        background = new File("./resource/image/background1.png");

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(card);

        menu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    g.drawImage(ImageIO.read(background), 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        menu.setLayout(null);

        setting = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    g.drawImage(ImageIO.read(background), 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        setting.setLayout(new BoxLayout(setting, BoxLayout.Y_AXIS));

        game = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    g.drawImage(ImageIO.read(background), 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        game.setLayout(null);

        add(menu, "Menu");
        addStartGameButton();
        addRankButton();
        addSettingButton();

        add(game, "Game");
        addChessboard();
        addColorLabel();
        addLoadButton();
        addSaveButton();
        addRegretButton();
        addMenuButton();

        add(setting, "Setting");
        addSignInButton();
        addSignUpButton();
        addBackgroundButton();

        card.show(this.getContentPane(), "Menu");

        musicPlayer("bgm.wav", true);
    }

    private void addStartGameButton() {
        JButton button = new JButton("Start Game");
        button.setLocation(WIDTH / 2 - 100, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        menu.add(button);
        button.addActionListener(e -> {
            System.out.println("Start game");
            String[] optionsMode = { "Local", "Link start!", "AI" };
            switch (JOptionPane.showOptionDialog(this, "Choose a mode", "Start game", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, optionsMode, optionsMode[0])) {
                case 0:
                    String userName = JOptionPane.showInputDialog(this, "Input your username", "Sign in",
                            JOptionPane.INFORMATION_MESSAGE);
                    if (userName != null) {
                        String password = JOptionPane.showInputDialog(this, "Input your password", "Sign in",
                                JOptionPane.INFORMATION_MESSAGE);
                        if (password != null) {
                            boolean check = false;
                            for (User user : userList.getUserList()) {
                                if (user.getUserName().equals(userName)) {
                                    if (user.checkPassword(password)) {
                                        gameController.setUser(user);
                                        check = true;
                                    }
                                }
                            }
                            if (check) {
                                JOptionPane.showMessageDialog(this, "Successfully sign in");
                                card.show(this.getContentPane(), "Game");
                                addCountdownLabel(true);
                            } else {
                                JOptionPane.showMessageDialog(this, "Wrong username or password");
                            }
                        }
                    }
                    break;
                case 1:
                    try {
                        InetAddress address = InetAddress.getLocalHost();

                        String host = JOptionPane.showInputDialog(this,
                                "Your address:" + address.getHostAddress() + "\nInput an address", "Start game",
                                JOptionPane.INFORMATION_MESSAGE);
                        if (host != null) {
                            if (host.equals("")) {
                                gameController.serverStart();
                                card.show(this.getContentPane(), "Game");
                                addCountdownLabel(true);

                            } else {
                                card.show(this.getContentPane(), "Game");
                                game.repaint();
                                addCountdownLabel(true);

                                gameController.clientStart(host);
                            }
                        }
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case 2:
                    String[] optionsHardness = { "Simple", "Normal" };
                    gameController.setHardness(JOptionPane.showOptionDialog(this, "Choose hardness", "Start game",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, optionsHardness, optionsHardness[0]));
                    card.show(this.getContentPane(), "Game");
                    addCountdownLabel(true);
                    break;
            }

        });
    }

    private void addRankButton() {
        JButton button = new JButton("Rank");
        button.setLocation(WIDTH / 2 - 100, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        menu.add(button);

        button.addActionListener(e -> {
            System.out.println("Click rank");
            SwingUtilities.invokeLater(() -> {
                JFrame rank = userList.showRank();
                rank.setVisible(true);
            });
        });
    }

    private void addSettingButton() {
        JButton button = new JButton("Setting");
        button.setLocation(WIDTH / 2 - 100, HEIGTH / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        menu.add(button);

        button.addActionListener(e -> {
            System.out.println("Click setting");
            SwingUtilities.invokeLater(() -> {
                card.show(this.getContentPane(), "Setting");
            });
        });
    }

    private void addSignInButton() {
        JButton button = new JButton("Sign in");
        // button.setSize(200, 50);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        setting.add(Box.createHorizontalGlue());
        setting.add(button);

        button.addActionListener(e -> {
            String userName = JOptionPane.showInputDialog(this, "Input your username", "Sign in",
                    JOptionPane.INFORMATION_MESSAGE);
            if (userName != null) {
                String password = JOptionPane.showInputDialog(this, "Input your password", "Sign in",
                        JOptionPane.INFORMATION_MESSAGE);
                if (password != null) {
                    boolean check = false;
                    for (User user : userList.getUserList()) {
                        if (user.getUserName().equals(userName)) {
                            if (user.checkPassword(password)) {
                                gameController.setUser(user);
                                check = true;
                            }
                        }
                    }
                    if (check) {
                        JOptionPane.showMessageDialog(this, "Successfully sign in");
                    } else {
                        JOptionPane.showMessageDialog(this, "Wrong username or password");
                    }
                }
            }

        });
    }

    private void addSignUpButton() {
        JButton button = new JButton("Sign up");
        // button.setLocation(80, 10);
        button.setSize(50, 50);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        setting.add(Box.createHorizontalGlue());
        setting.add(button);

        button.addActionListener(e -> {
            // JFrame jf = new JFrame("Sign UP");
            // JPanel jp = new JPanel();
            // jp.setLayout(null);
            // jp.add(new JLabel("username"));
            // jp.add(new JTextField(20));
            // jp.add(new JLabel("password"));
            // jp.add(new JTextField(20));
            // jf.setSize(400, 200);
            // jf.setLocationRelativeTo(null);
            // jf.setVisible(true);

            String userName = JOptionPane.showInputDialog(this, "Input your username", "Sign up",
                    JOptionPane.INFORMATION_MESSAGE);
            if (userName != null) {
                String password = JOptionPane.showInputDialog(this, "Input your password", "Sign up",
                        JOptionPane.INFORMATION_MESSAGE);
                if (password != null) {
                    String[] opt = { "Default", "Select from file" };
                    switch (JOptionPane.showOptionDialog(this, "Choose your avatar", "Sign up",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, opt, opt[0])) {
                        case 0:
                            if (!userList.signUp(userName, password,
                                    new File("./resource/image/defaultAvatar.png"))) {
                                JOptionPane.showMessageDialog(this, "User exist");
                            }
                            break;
                        case 1:
                            JFileChooser jfc = new JFileChooser("./resource/image/");
                            jfc.setAcceptAllFileFilterUsed(false);
                            FileNameExtensionFilter fnef = new FileNameExtensionFilter("image", "jpg", "jpeg", "png");
                            jfc.addChoosableFileFilter(fnef);
                            userList.signUp(userName, password, jfc.getSelectedFile());
                    }
                }
            }
        });
    }

    private void addBackgroundButton() {
        JButton button = new JButton("Background");
        // button.setLocation(150, 10);
        button.setSize(50, 50);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        setting.add(Box.createHorizontalGlue());
        setting.add(button);
        setting.add(Box.createHorizontalGlue());
        
        button.addActionListener(e -> {
            System.out.println("Change background");
            String[] options = { "1", "2" };
            String buff = JOptionPane.showInputDialog(this, "Choose a background", " Background setting",
                    JOptionPane.INFORMATION_MESSAGE, null, options, options[0]).toString();
            background = new File("./resource/image/background" + buff + ".png");
            repaint();
        });
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE, this);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        game.add(chessboard);
    }

    /**
     * 在游戏面板中添加标签
     */

    private void addColorLabel() {
        statusLabel = new JLabel("WHITE");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        statusLabel.setForeground(Color.WHITE);
        game.add(statusLabel);
    }

    public void changeColorLabel(String color) {
        statusLabel.setText(color);
    }

    public void addCountdownLabel(boolean start) {
        if (start) {
            countdownLabel.setLocation(HEIGTH, HEIGTH / 10 + 60);
            countdownLabel.setSize(200, 60);
            countdownLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
            countdownLabel.setForeground(Color.WHITE);
            countdownLabel.setText(new SimpleDateFormat("mm:ss").format(500 * 1000));

            timer = new javax.swing.Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] buff = countdownLabel.getText().split(":");
                    int timeleft = Integer.parseInt(buff[0]) * 60 * 1000 + Integer.parseInt(buff[1]) * 1000 - 1000;
                    countdownLabel.setText(new SimpleDateFormat("mm:ss").format(timeleft));
                    game.add(countdownLabel);
                    if (timeleft == 0) {
                        gameController.forceSwapColor();
                    }
                }
            });
            timer.start();
        } else {
            timer.stop();
        }
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));

        game.add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            JFileChooser jfc = new JFileChooser("./resource/save");
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
        game.add(button);

        button.addActionListener(e -> {
            System.out.println("Click save");
            JFileChooser jfc = new JFileChooser("./resource/save");
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("txt", "txt");
            jfc.addChoosableFileFilter(fnef);
            if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String fileName = jfc.getName(jfc.getSelectedFile());
                if (fileName.indexOf(".txt") == -1) {
                    gameController.saveGameToFile(new File(jfc.getCurrentDirectory() + "/" + fileName + ".txt"));
                } else {
                    gameController.saveGameToFile(jfc.getSelectedFile());
                }
            }

        });
    }

    private void addResetButton() {
        JButton button = new JButton("Reset");
        button.setLocation(HEIGTH, HEIGTH / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        game.add(button);

        button.addActionListener(e -> {
            System.out.println("Click reset");
            if (JOptionPane.showConfirmDialog(this, "Confirm to reset?", "Comfirm", 0) == JOptionPane.YES_OPTION) {
                gameController.resetGame();
            }
        });
    }

    private void addRegretButton() {
        JButton button = new JButton("Regret");
        button.setLocation(HEIGTH, HEIGTH / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        game.add(button);

        button.addActionListener(e -> {
            System.out.println("Click reset");
                gameController.regretStep();
            
        });
    }

    private void addReplayButton() {
        JButton button = new JButton("Replay");
        button.setLocation(HEIGTH, HEIGTH / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        game.add(button);

        button.addActionListener(e -> {
            System.out.println("Click replay");
            JFileChooser jfc = new JFileChooser("./resource/save");
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("txt", "txt");
            jfc.addChoosableFileFilter(fnef);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                gameController.replayGameFromFile(jfc.getSelectedFile());
            }
        });
    }

    private void addMenuButton() {
        JButton button = new JButton("Menu");
        button.setLocation(HEIGTH, HEIGTH / 10 + 540);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        game.add(button);

        button.addActionListener(e -> {
            System.out.println("Click menu");
            gameController.resetGame();
            card.show(this.getContentPane(), "Menu");
            addCountdownLabel(false);
        });
    }

    protected void musicPlayer(String musicFile, boolean loop) {
        File music = new File("./resource/music/" + musicFile);
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(music);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
