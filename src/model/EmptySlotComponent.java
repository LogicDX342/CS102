package model;

import view.ChessboardPoint;
import controller.ClickController;

import java.awt.*;
import java.io.IOException;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size,String theme) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size);
        super.theme=theme;
    }

    @Override
    public void setMoved(){
    }
    @Override
    public boolean getMoved() {
        return false;
    }

    @Override
    public char getType() {
        return '_';
    }

    public void setTwoBlock() {
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public void loadResource(String theme) throws IOException {
        // No resource!
      
    }

}
