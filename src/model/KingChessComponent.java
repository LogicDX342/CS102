package model;

import view.Chessboard;
import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 这个类表示国际象棋里面的王
 */
public class KingChessComponent extends ChessComponent {
    /*
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private  Image KING_WHITE;
    private  Image KING_BLACK;

    private Image kingImage;

    private int countKing;
    public boolean notMoved = true;

    @Override
    public void setMoved(){notMoved = false;}

    public void loadResource(String theme) throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("./resource/image/"+theme+"king-white.png"));
        }

        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("./resource/image/"+theme+"king-black.png"));
        }
    }

    private void initiateKingImage(ChessColor color,String theme) {
        try {
            loadResource(theme);
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color,
            ClickController listener, int size,String theme) {
        super(chessboardPoint, location, color, listener, size);
        initiateKingImage(color,theme);
        super.theme=theme;
    }

    @Override
    public char getType() {
        return 'K';
    }

    public void setTwoBlock() {
    }

    @Override
    public boolean getMoved() {
        return notMoved;
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();// source:棋子走之前的坐标
        if (notMoved && destination.getX() == source.getX() &&
                (destination.getY() == 0 || destination.getY() == 7)){
            return true;
        }
        else if (Math.abs(source.getX() - destination.getX()) == 1 && source.getY() == destination.getY()) {
            return true;
        } else if (Math.abs(source.getY() - destination.getY()) == 1 && source.getX() == destination.getX()) {
            return true;
        } // 坐标和之差绝对值为1
          // (source.getY() == destination.getY() || source.getX() == destination.getX())
        else if (Math.abs(source.getX() - destination.getX()) == 1
                && Math.abs(source.getY() - destination.getY()) == 1) {
            countKing = 1;
            return true;
        } // 斜着走一格
        /*else if (!isMoved &&  = 'p' ){

        }
        */
        else {
            return false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(kingImage, 0, 0, getWidth(), getHeight(), this);
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
