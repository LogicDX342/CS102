package model;

import view.Chessboard;
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
    private Image PAWN_WHITE;
    private Image PAWN_BLACK;

    private Image pawnImage;
    private boolean twoBlock = true;
    private boolean eatPassPawn = false;

    @Override
    public void setMoved() {
    }

    public void loadResource(String theme) throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./resource/image/"+theme+"pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./resource/image/"+theme+"pawn-black.png"));
        }
    }

    private void initiatePawnImage(ChessColor color,String theme) {
        try {
            loadResource(theme);
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
                              ClickController listener, int size,String theme) {
        super(chessboardPoint, location, color, listener, size);
        initiatePawnImage(color,theme);
        super.theme=theme;
    }

    @Override
    public char getType() {
        return 'P';
    }

    @Override
    public boolean getMoved() {
        return false;
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
                Chessboard.markTwo = Chessboard.stepNumber;
                Chessboard.markTwoX = destination.getX();
                Chessboard.markTwoY = destination.getY();
                return true;
            } else if (twoBlock && destination.getX() - source.getX() == -2 && chessColor == ChessColor.WHITE &&
                    chessComponents[destination.getX() + 1][destination.getY()] instanceof EmptySlotComponent &&
                    chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent) {// 向前两格且目的地为空
                Chessboard.markTwo = Chessboard.stepNumber;
                Chessboard.markTwoX = destination.getX();
                Chessboard.markTwoY = destination.getY();
                return true;
            } else {
                return false;
            }
        }//向前走结束
        else if (Math.abs(source.getY() - destination.getY()) == 1 &&
                ((destination.getX() - source.getX() == 1 && chessColor == ChessColor.BLACK) ||
                        (source.getX() - destination.getX() == 1 && chessColor == ChessColor.WHITE))) { // 向左或右吃子
            if ((destination.getX() == 0 || destination.getX() == 7) &&
                    !(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)){//行至底线，对角有子
                return true;
            }//判定底线升变条件
            else if (Chessboard.stepNumber == Chessboard.markTwo + 1 && Math.abs(source.getY() - destination.getY()) == 1 &&
                    ((destination.getX() - source.getX() == 1 && chessColor == ChessColor.BLACK) ||
                            (source.getX() - destination.getX() == 1 && chessColor == ChessColor.WHITE)) &&
                    source.getX() == Chessboard.markTwoX && destination.getY() == Chessboard.markTwoY) {
                setMoved();
                return true;
            } //吃过路兵
            if (!(chessComponents[destination.getX()][destination.getY()] instanceof EmptySlotComponent)) { // 为空则不能走
                return true;
            } else {
                return false;
            }
        }// 吃子的实现到这结束
        else {
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
        }if(isTargeted()){
            g.setColor(Color.GRAY);
            g.fillOval(27,30,20,20);
        }
    }
}
