package view.ai;

import controller.Controller;
import model.GamePhase;
import model.Move;
import model.Piece;
import model.IPlayer;

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

    public static void updateLists(Controller controller) {
        controller.getState().legalPlacing.clear();
        controller.getState().legalMoves.clear();
        if (controller.getGamePhase() == GamePhase.Placing){
            controller.getState().updateLegalPlacing();
        } else {
            if (!freeMoveAllowed(controller, controller.getTurnColor())){
                controller.getState().updateLegalMoves();
            } else {
                controller.getState().updateFreeMovementLegalMoves();
            }
        }
    }

    public static int getLegalMovesCount(Controller controller, IPlayer player, Node currentNode){
        switch (controller.getGamePhase()) {
            case Moving:
                return currentNode.state.getState().getLegalMoveList(currentNode.state.getState().turnColor).size();
            case Placing:
                return currentNode.state.getState().legalPlacing.size();

            case RemovingStone:
                int count = 0;
                for (Piece p : currentNode.state.getState().currentPieces) {
                    if (!p.color.equals(player.getColor())) {
                        count++;
                    }
                }
                return count;
        }
        return 0;
    }

    public static List<Move> getLegalMoves(Controller controller, IPlayer player, Node currentNode, boolean remove){
        switch (controller.getGamePhase()) {
            case Moving:
                List<Move> legalMoves = currentNode.state.getState().getLegalMoveList(currentNode.state.getState().turnColor);

                if (!remove) return legalMoves;
                for (Move tmpMove : new LinkedList<>(legalMoves)) {
                    for (Node tmpNode : currentNode.listOfChildren) {
                        if (tmpNode.move.equals(tmpMove)) {
                            legalMoves.remove(tmpMove);
                            break;
                        }
                    }
                }
                return legalMoves;
            case Placing:
                List<Move> legalPlacing = new LinkedList<>();
                for (Point p : currentNode.state.getState().legalPlacing) {
                    Move tmpMove = new Move(null, p);
                    legalPlacing.add(tmpMove);
                }
                if (!remove) return legalPlacing;
                for (Move move : new LinkedList<>(legalPlacing)) {
                    for (Node tmpNode : currentNode.listOfChildren) {
                        if(tmpNode.move.equals(move)) {
                            legalPlacing.remove(move);
                            break;
                        }
                    }
                }
                return legalPlacing;
            case RemovingStone:
                List<Move> legalRemove = new LinkedList<>();
                for (Piece p : currentNode.state.getState().currentPieces) {
                    if (!p.color.equals(player.getColor()) && !controller.getState().isInMill(p)) {
                        Point tmpPoint = new Point(p.field.x, p.field.y);
                        Move tmpMove = new Move(tmpPoint, null);
                        legalRemove.add(tmpMove);
                    }
                }
                //Everything in mill check
                if (legalRemove.size() == 0) {
                    for (Piece p : controller.getState().currentPieces) {
                        if (!p.color.equals(player.getColor())) {
                            Point tmpPoint = new Point(p.field.x, p.field.y);
                            Move tmpMove = new Move(tmpPoint, null);
                            legalRemove.add(tmpMove);
                        }
                    }
                }

                return legalRemove;
        }
       return null;
    }

    public static void exectuteMove(Controller controller, Move move) {
        switch (controller.getGamePhase()) {
            case Placing:
                controller.place(move.dst);
                break;
            case Moving:
                controller.move(move.src, move.dst);
                break;
            case RemovingStone:
                controller.removeStone(move.src);
                break;
        }
    }

}

