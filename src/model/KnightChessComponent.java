package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 这个类表示国际象棋里面的马
 */
public class KnightChessComponent extends ChessComponent {
    /*
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private Image KNIGHT_WHITE;
    private Image KNIGHT_BLACK;

    private Image knightImage;

    @Override
    public void setMoved(){
    }
    @Override
    public boolean getMoved() {
        return false;
    }

    public void loadResource(String theme) throws IOException {
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("./resource/image/"+theme+"knight-white.png"));
        }

        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("./resource/image/"+theme+"knight-black.png"));
        }
    }

    private void initiateKnightImage(ChessColor color,String theme) {
        try {
            loadResource(theme);
            if (color == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (color == ChessColor.BLACK) {
                knightImage = KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color,
            ClickController listener, int size,String theme) {
        super(chessboardPoint, location, color, listener, size);
        initiateKnightImage(color,theme);
        super.theme=theme;
    }

    @Override
    public char getType() {
        return 'N';
    }

    public void setTwoBlock() {
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();// source:棋子走之前的坐标
        if (Math.abs(source.getX() - destination.getX()) == 2 && Math.abs(source.getY() - destination.getY()) == 1) {
            return true;
        } else if (Math.abs(source.getX() - destination.getX()) == 1
                && Math.abs(source.getY() - destination.getY()) == 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(knightImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }if(isTargeted()){
            g.setColor(Color.GRAY);
            g.fillOval(27,30,20,20);
        }
    }
}
