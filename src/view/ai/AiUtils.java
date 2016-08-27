package view.ai;

import controller.Controller;
import model.GamePhase;
import model.Piece;
import view.AbstractPlayer;

import java.awt.*;
import java.util.*;
import java.util.List;

import static model.Utils.freeMoveAllowed;

/**
 * Created by Max on 25/08/2016.
 */
public class AiUtils {

    public static int getRandomNumber(){
        Random rn = new Random();
        return Math.abs(rn.nextInt());
    }

    public static Point randomMoveSource (Controller controller, AbstractPlayer abstractPlayer) { //selects a random piece to move this round
        List<Point> tmpList = new LinkedList<>();
        for (Piece p : controller.getState().currentPieces) {
            if (abstractPlayer.getColor() == p.color && controller.getState().legalMoves.containsKey(p.field)) {
                if (controller.getState().legalMoves.get(p.field).size() > 0) {
                    Point tmpPoint = new Point(p.field.x, p.field.y);
                    tmpList.add(tmpPoint);
                }
            }
        }
        return tmpList.get(getRandomNumber() % tmpList.size());
    }

    public static Point selectRandomPlacing(Controller controller) { //random move while placing Pieces
        if (controller.getState().legalPlacing.size() == 0){
            System.out.println("lol");
        }
        return controller.getState().legalPlacing.get(getRandomNumber() % controller.getState().legalPlacing.size());
    }

    public static Move selectRandomMove(Controller controller, AbstractPlayer abstractPlayer) { //random move while moving with more than 3 Pieces.
        Point tmpSrc = randomMoveSource(controller, abstractPlayer);
        List<Point> dstList = controller.getState().legalMoves.get(controller.getState().board[tmpSrc.x][tmpSrc.y]);
        Point tmpDst = new Point(dstList.get(getRandomNumber() % dstList.size()));
        return new Move(tmpSrc, tmpDst);
    }

    public static Point selectRandomRemove(Controller controller, AbstractPlayer abstractPlayer) { //randomly selected field of an enemy field to remove the piece.
        List<Point> enemyPieces = new LinkedList<Point>();
        for (Piece p : controller.getState().currentPieces) {
            if (p.color != abstractPlayer.getColor() && !controller.getState().isInMill(p)) {
                Point tmpPoint = new Point(p.field.x, p.field.y);
                enemyPieces.add(tmpPoint);
            }
        }
        return new Point(enemyPieces.get(getRandomNumber() % enemyPieces.size()));
    }

    public static void updateLists(Controller controller) {
        controller.getState().legalPlacing.clear();
        controller.getState().legalMoves.clear();
        if (!freeMoveAllowed(controller, controller.getTurnColor())){
            controller.getState().updateLegalMoves();
        } else {
            controller.getState().updateFreeMovementLegalMoves();
        }
        controller.getState().updateLegalPlacing();
    }

    public static Move place(Controller controller){
        Move tmpMove = new Move(null, null);
        tmpMove.dst = selectRandomPlacing(controller);
        controller.place(tmpMove.dst);
        return tmpMove;
        //Copy State
        /*
        Controller copyCont = millController.deepCopy();
        if (myColor == Color.black)
            copyCont.setWhitePlayer(new DummyPlayer());
        else
            copyCont.setBlackPlayer(new DummyPlayer());
            */

        //init mtcs

        //simulate

        //make real call to controller
    }

    public static Move moving(Controller controller, AbstractPlayer abstractPlayer) {
        Move tmpMove = selectRandomMove(controller, abstractPlayer);
        if (!freeMoveAllowed(controller, controller.getTurnColor())){
            controller.move(tmpMove.src, tmpMove.dst);
            return tmpMove;
        } else {
            controller.setSleep(1);
            controller.moveFreely(tmpMove.src, tmpMove.dst);
            return tmpMove;
        }
    }

    public static Move removeStone(Controller controller, AbstractPlayer abstractPlayer) {
        Move tmpMove = new Move(null, null);
        tmpMove.src = selectRandomRemove(controller, abstractPlayer);
        controller.removeStone(tmpMove.src);  //using src =value, dst = null to differ from placing Moves
        return tmpMove;
    }

}

