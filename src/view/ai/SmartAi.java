package view.ai;

import controller.Controller;
import model.Pieces;
import view.AbstractPlayer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.math.*;

public class SmartAi extends AbstractPlayer {

    @Override
    public void run() {
        updateLists();
        System.out.println("smartAI: run()");
        switch (millController.getGamePhase())	{
            case Placing:
                place();
                break;
            case Moving:
            case Endgame:
                moving();
                break;

            case RemovingStone:
                removeStone();
                break;
        }

        System.out.println("smartAI: exit");
    }

    private void updateLists() {
        millController.getState().legalPlacing.clear();
        millController.getState().legalMoves.clear();
        //millController.getState().updateFreeMovementLegalMoves();
        millController.getState().updateLegalMoves();
        millController.getState().updateLegalPlacing();
    }

    private void place(){
        //Copy State
        Controller copyCont = millController.deepCopy();
        if (myColor == Color.black)
            copyCont.setWhitePlayer(new DummyPlayer());
        else
            copyCont.setBlackPlayer(new DummyPlayer());

        millController.place(selectRandomPlacing());
        //init mtcs

        //simulate

        //make real call to controller
    }

    private void moving() {
        Move tmpMove = selectRandomMove();
        millController.move(tmpMove.src, tmpMove.dst);
    }

    private void removeStone() {
        millController.removeStone(selectRandomRemove());
    }




    public int getRandomNumber(){
        Random rn = new Random();
        return Math.abs(rn.nextInt());
    }

    private Point randomMoveSource () { //selects a random piece to move this round
        List<Point> tmpList = new LinkedList<Point>();
        for (Pieces p : millController.getState().currentPieces) {
            if (myColor == p.color && millController.getState().legalMoves.containsKey(p.field)) {
                if (millController.getState().legalMoves.get(p.field).size() > 0) {
                    Point tmpPoint = new Point(p.field.x, p.field.y);
                    tmpList.add(tmpPoint);
                }
            }
        }
        return tmpList.get(getRandomNumber() % tmpList.size());
    }

    private Point selectRandomPlacing() { //random move while placing Pieces todo: replace Point with Move
        return millController.getState().legalPlacing.get(getRandomNumber() % millController.getState().legalPlacing.size());
    }

    private Move selectRandomMove() { //random move while moving with more than 3 Pieces.
        Point tmpSrc = randomMoveSource();
        Point tmpPoint = new Point(millController.getState().board[tmpSrc.x][tmpSrc.y].x, millController.getState().board[tmpSrc.x][tmpSrc.y].y);
        List<Point> dstList = millController.getState().legalMoves.get(millController.getState().board[tmpPoint.x][tmpPoint.y]);
        Point tmpDst = new Point(dstList.get(getRandomNumber() % dstList.size()));
        return new Move(tmpSrc, tmpDst);
    }

    private Point selectRandomRemove() { //randomly selected field of an enemy field to remove the piece.
        List<Point> enemyPieces = new LinkedList<Point>();  // todo: replace return with an argument of Move
        for (model.Pieces p : millController.getState().currentPieces) {
            if (p.color != myColor && !millController.getState().isInMill(p)) {
                Point tmpPoint = new Point(p.field.x, p.field.y);
                enemyPieces.add(tmpPoint);
            }
        }
        return new Point(enemyPieces.get(getRandomNumber() % enemyPieces.size()));
    }
}