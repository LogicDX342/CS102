package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 这个类表示国际象棋里面的兵
 */
public class PawnChessComponent extends ChessComponent {
    /*
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;

    private Image pawnImage;
    private boolean twoBlock = true;

    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./resource/image/pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./resource/image/pawn-black.png"));
        }
    }

    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color,
            ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiatePawnImage(color);
    }

    @Override
    public char getType() {
        return 'P';
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {

        ChessboardPoint source = getChessboardPoint();// source:棋子走之前的坐标

        if (source.getY() == destination.getY()) {// 向前走
            if ((destination.getX() - source.getX() == 1 && chessColor == ChessColor.BLACK ||
                    source.getX() - destination.getX() == 1 && chessColor == ChessColor.WHITE) && // 黑色向下，白色向上
                    chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {// 向前一格且目的地为空
                return true;
            } else if (twoBlock && destination.getX() - source.getX() == 2 && chessColor == ChessColor.BLACK &&
                    chessComponents[destination.getX() - 1][destination.getY()] instanceof EmptySlotComponent &&
                    chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {// 向前两格且目的地为空
                return true;
            } else if (twoBlock && destination.getX() - source.getX() == -2 && chessColor == ChessColor.WHITE &&
                    chessComponents[destination.getX() + 1][destination.getY()] instanceof EmptySlotComponent &&
                    chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {// 向前两格且目的地为空
                return true;
            } else {
                return false;
            }
        } else if (Math.abs(source.getY() - destination.getY()) == 1 &&
                ((destination.getX() - source.getX() == 1 && chessColor == ChessColor.BLACK) ||
                (source.getX() - destination.getX() == 1 && chessColor == ChessColor.WHITE))) {// 向左或右吃子
            if (chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {// 为空则不能走
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void setTwoBlock() {
        twoBlock = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(pawnImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }
}
