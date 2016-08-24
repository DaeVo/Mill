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

        System.out.println("");
    }

    private void updateLists() {
        millController.getState().legalPlacing.clear();
        millController.getState().legalMoves.clear();
        millController.getState().updateFreeMovementLegalMoves();
        millController.getState().updateLegalMoves();
        millController.getState().updateLegalPlacing();
    }

    private void place(){
        //Copy State
      /*  Controller copyCont = millController.deepCopy();
        if (myColor == Color.black)
            copyCont.setWhitePlayer(new DummyPlayer());
        else
            copyCont.setBlackPlayer(new DummyPlayer());
*/
        millController.place(selectRandomPlacing());
        //init mtcs

        //simulate

        //make real call to controller
    }

    private void moving() {
        millController.move(selectRandomMove().src, selectRandomMove().dst);
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
            if (myColor == p.color) {
                Point tmpPoint = new Point (p.field.x, p.field.y);
                tmpList.add(tmpPoint);
            }
        }
        return tmpList.get(getRandomNumber() % tmpList.size());
    }

    private Point selectRandomPlacing() { //random move while placing Pieces todo: replace Point with Move
        Point tmpPoint =  millController.getState().legalPlacing.get(getRandomNumber() % millController.getState().legalPlacing.size());
        millController.place(tmpPoint);
        return tmpPoint;
    }

    private Move selectRandomMove() { //random move while moving with more than 3 Pieces.
        Point tmpSrc = randomMoveSource();
        List<Point> dstList = millController.getState().legalMoves.get(tmpSrc);
        Point tmpDst = new Point(dstList.get(getRandomNumber() % dstList.size()));
        millController.move(tmpSrc, tmpDst);
        return new Move(tmpSrc, tmpDst);
    }

    private Point selectRandomRemove() { //randomly selected field of an enemy field to remove the piece.
        List<Point> enemyPieces = new LinkedList<Point>();  // todo: replace return with an argument of Move
        for (model.Pieces p : millController.getState().currentPieces) {
            if (p.color != myColor) {
                Point tmpPoint = new Point(p.field.x, p.field.y);
                enemyPieces.add(tmpPoint);
            }
        }
        Point tmpPoint = new Point(enemyPieces.get(getRandomNumber() % enemyPieces.size()));
        millController.removeStone(tmpPoint);
        return tmpPoint;
    }
}