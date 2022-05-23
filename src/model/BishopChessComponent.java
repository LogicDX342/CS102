package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 这个类表示国际象棋里面的象
 */
public class BishopChessComponent extends ChessComponent{
    /*
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private Image BISHOP_WHITE;
    private Image BISHOP_BLACK;

    private Image bishopImage;

    @Override
    public void setMoved() {
    }
    @Override
    public boolean getMoved() {
        return false;
    }

    public void loadResource(String theme) throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("./resource/image/"+theme+"bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("./resource/image/"+theme+"bishop-black.png"));
        }
    }

    private void initiateBishopImage(ChessColor color,String theme) {
        try {
            loadResource(theme);
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size,String theme) {
        super(chessboardPoint, location, color, listener, size);
        initiateBishopImage(color,theme);
        super.theme=theme;
    }

    public char getType(){
        return 'B';
    }

    public void setTwoBlock(){  
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();//source:棋子走之前的坐标
        if (source.getX() - source.getY() == destination.getX() - destination.getY()) {//向右上走
            for (int col = Math.min(source.getY(), destination.getY()) + 1, row = Math.min(source.getX(), destination.getX()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++, row++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getX() + source.getY() == destination.getX() + destination.getY()) {//向左上走
            for (int col = Math.min(source.getY(), destination.getY()) + 1, row = Math.max(source.getX(), destination.getX()) - 1;
                 col < Math.max(source.getY(), destination.getY()); col++, row--) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bishopImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
        if(isTargeted()){
            g.setColor(Color.GRAY);
            g.fillOval(27,30,20,20);
        }
    }
}
