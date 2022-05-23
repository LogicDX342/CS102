package controller;

import model.ChessColor;
import model.ChessComponent;
import view.Chessboard;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {//判断之前是否选中其他棋子
            if (handleFirst(chessComponent)) {//判断是不是选对了行棋方
                chessComponent.setSelected(true);//添加选择框
                chessboard.showTargeted(chessComponent);//显示可移动路径
                first = chessComponent;//标记为已经选中
                first.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                chessboard.hideTargeted();
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {//选择移动位置
                if (chessComponent.getType() == 'R' && first.getType() == 'K' && first.getMoved() && chessComponent.getMoved()
                        && (chessComponent.getChessboardPoint().getY() == 0 || chessComponent.getChessboardPoint().getY() == 7)) {//选王和车，位置正确
                    if (chessComponent.getChessColor() == ChessColor.WHITE && chessComponent.getChessboardPoint().getY() == 0 &&
                            chessboard.getChessComponents()[7][1].getType() == '_' && chessboard.getChessComponents()[7][2].getType() == '_'
                            && chessboard.getChessComponents()[7][3].getType() == '_' && !chessboard.getChessComponents()[7][2].isTargeted() /*&&
                            !chessboard.getChessComponents()[7][3].isTargeted()*/ && !chessboard.getChessComponents()[7][4].isTargeted()) {
                        chessboard.hideTargeted();
                        chessboard.swapColor();
                        chessboard.swapChessComponents(first, chessboard.getChessComponents()[7][2], true);
                        chessboard.swapChessComponents(chessComponent, chessboard.getChessComponents()[7][3], true);
                        first.setSelected(false);
                        first = null;
                    }
                    else if (chessComponent.getChessColor() == ChessColor.BLACK && chessComponent.getChessboardPoint().getY() == 0 &&
                            chessboard.getChessComponents()[0][1].getType() == '_' && chessboard.getChessComponents()[0][2].getType() == '_'
                            && chessboard.getChessComponents()[0][3].getType() == '_' && !chessboard.getChessComponents()[0][2].isTargeted() /*&&
                            !chessboard.getChessComponents()[7][3].isTargeted()*/ && !chessboard.getChessComponents()[0][4].isTargeted()) {
                        chessboard.hideTargeted();
                        chessboard.swapColor();
                        chessboard.swapChessComponents(first, chessboard.getChessComponents()[0][2], true);
                        chessboard.swapChessComponents(chessComponent, chessboard.getChessComponents()[0][3], true);
                        first.setSelected(false);
                        first = null;
                    }
                    else if (chessComponent.getChessColor() == ChessColor.WHITE && chessComponent.getChessboardPoint().getY() == 7 &&
                            chessboard.getChessComponents()[7][5].getType() == '_' && chessboard.getChessComponents()[7][6].getType() == '_'
                             /*&& !chessboard.getChessComponents()[7][2].isTargeted()*/ /*&&
                            !chessboard.getChessComponents()[7][3].isTargeted()*/ && !chessboard.getChessComponents()[7][4].isTargeted()){
                        chessboard.hideTargeted();
                        chessboard.swapColor();
                        chessboard.swapChessComponents(first, chessboard.getChessComponents()[7][6], true);
                        chessboard.swapChessComponents(chessComponent, chessboard.getChessComponents()[7][5], true);
                        first.setSelected(false);
                        first = null;
                    }
                    else if (chessComponent.getChessColor() == ChessColor.BLACK && chessComponent.getChessboardPoint().getY() == 7 &&
                            chessboard.getChessComponents()[0][5].getType() == '_' && chessboard.getChessComponents()[0][6].getType() == '_'
                            /*&& !chessboard.getChessComponents()[7][2].isTargeted()*/ /*&&
                            !chessboard.getChessComponents()[7][3].isTargeted()*/ && !chessboard.getChessComponents()[0][4].isTargeted()){
                        chessboard.hideTargeted();
                        chessboard.swapColor();
                        chessboard.swapChessComponents(first, chessboard.getChessComponents()[0][6], true);
                        chessboard.swapChessComponents(chessComponent, chessboard.getChessComponents()[0][5], true);
                        first.setSelected(false);
                        first = null;
                    }
                    else {
                    }
                }
                // repaint in swap chess method.
                else {
                    chessboard.hideTargeted();
                    chessboard.swapChessComponents(first, chessComponent, true);
                    chessboard.swapColor();

                    first.setSelected(false);
                    first = null;
                }
            }
        }
    }
    public void reset(){
        first=null;
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        if (chessComponent.getType() == 'R' && first.getType() == 'K' && first.getMoved() && chessComponent.getMoved()
                && (chessComponent.getChessboardPoint().getY() == 0 || chessComponent.getChessboardPoint().getY() == 7)) {
            return true;
        } else {
            return (chessComponent.getChessColor() != first.getChessColor() &&
                    first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint()));
        }
    }

}
