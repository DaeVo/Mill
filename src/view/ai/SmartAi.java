package view.ai;

import model.Gamestate;
import model.Pieces;
import model.Playfield;
import view.AbstractPlayer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SmartAi extends AbstractPlayer {

    Gamestate gametateObject = new Gamestate(null);

    @Override
    public void run() {
        System.out.println("Ki: run()");
        switch (millController.getGamePhase()) {
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

    private void place() {

    }

    private void moving() {
    }

    private void removeStone() {
    }




    public int getRandomNumber(){
        Random rn = new Random();
        return rn.nextInt();
    }
    public Color color;

    private Point randomMoveSource () { //selects a random piece to move this round
        List<Point> tmpList = new LinkedList<Point>();
        for (model.Pieces p : gametateObject.currentPieces) {
            if (color == p.color) {
                Point tmpPoint = new Point (p.field.x, p.field.y);
                tmpList.add(tmpPoint);
            }
        }
        return tmpList.get(getRandomNumber() % tmpList.size());
    }

    private Point selectRandomPlacing() { //random move while placing Pieces
        //place here already?
        return gametateObject.legalPlacing.get(getRandomNumber() % gametateObject.legalPlacing.size());
    }

    private Move selectRandomMove() { //random move while moving with more than 3 Pieces.
        Point tmpSrc = randomMoveSource();
        List<Point> dstList = gametateObject.legalMoves.get(tmpSrc);
        //perform move here already?
        return new Move(tmpSrc, dstList.get(getRandomNumber() % dstList.size()));
    }

    private Point selectRandomRemove() { //randomly selected field of an enemy field to remove the piece.
        List<Point> enemyPieces = new LinkedList<Point>();
        for (model.Pieces p : gametateObject.currentPieces) {
            if (p.color != color) {
                Point tmpPoint = new Point(p.field.x, p.field.y);
                enemyPieces.add(tmpPoint);
            }
        }
        //conquer field here already?
        return enemyPieces.get(getRandomNumber() % enemyPieces.size());
    }
}