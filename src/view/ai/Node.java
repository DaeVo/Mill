package view.ai;

import controller.Controller;
import model.GamePhase;
import model.Move;
import model.Piece;
import model.Utils;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Max on 19/08/2016.
 * Nodes for the MCTS
 */
class Node implements java.io.Serializable {
    public double winCount;   //if ai  wins the simulated playout  - winCount++;
    public double playCount;  //if simulated game ends -  playCount++
    public byte millCountWhite;
    public byte millCountBlack;
    public Move move;
    public LinkedList<Node> listOfChildren = new LinkedList<>(); //legalMoves = children
 //   public byte pieceCountW;
 //   public byte pieceCountB;
    Move enemyMillMove = null;
    public Node getBestChild(Color myColor, Controller state) {
        byte currentStonesInMill = getMillCount(myColor);
        //  byte currentPiecesColor = getPieceCount(myColor);
        byte currentStonesInMillEnemy = getMillCount(getEnemyColor(myColor));
        double bestRatio = 0; //0-1 1=only wins
        Node bestNode = null;
        Node ratioNode = null;
        for (Node node : listOfChildren) {
         /*   double ratio = MCTS.root.playCount + Math.sqrt(Math.log(playCount)/ 5 * node.winCount);
            //double ratio = node.winCount / node.playCount;
            if (ratio > bestRatio) {
                bestRatio = ratio;
                bestNode = node;

            }
*/

            double tmpWinCount = 0;
            double tmpPlayCount = Integer.MAX_VALUE;
            for (Node children : listOfChildren) {
                if (children.winCount >= tmpWinCount && children.playCount < tmpPlayCount) {
                    tmpWinCount = children.winCount;
                    tmpPlayCount = children.playCount;
                    bestNode = children;
                }
            }

            /*double tmpWin = 0;
            if (node.winCount > tmpWin) {
                tmpWin = node.winCount;
                bestNode = node;
            }
            */
            //Close Mill Heuristic
            int childStonesInMill = node.getMillCount(myColor);
            if (childStonesInMill > currentStonesInMill) {
                bestNode = node;
                System.out.println("--------------------------------CLOSEMILL");
                return bestNode;
            }


            //prohibit enemy from closing a mill if possible (only with less than 6 pieces (includes placing)
            int currentPieces = state.getState().getPieceCountColor(myColor);
            if (currentPieces < 6) {
                for (Node subchildren : node.listOfChildren) {
                    byte subchildStonesInMillEnemy = subchildren.getMillCount(getEnemyColor(myColor));

                    if (subchildStonesInMillEnemy > currentStonesInMillEnemy) {
                        enemyMillMove = subchildren.move;
                    }
                }
                if (enemyMillMove != null && enemyMillMove.dst != null && !state.getGamePhase().equals(GamePhase.RemovingStone)) {
                    if (node.move.dst.equals(enemyMillMove.dst)) {
                        bestNode = node;
                        break;
                    }
                }
            }
        }





       /*     int childStonesCount = node.getPieceCount(myColor);
            if (childStonesCount < currentPiecesColor) {
                System.out.print("-----------------------------PROBABLYNOTHING");
                bestNode = node;
            }

            //select for higher millcount (should try to deny enemy mills if possible)
            for (Node tmpNode : node.listOfChildren) {

                int childChildStonesCount = tmpNode.getPieceCount(myColor);
                if (childChildStonesCount < currentPiecesColor) {
                    System.out.println("---------------------------PROBABLYNOTHING");
                    bestNode = node;
                }
                for (Node tmpNodeNode : tmpNode.listOfChildren) {
                    int childChildChildStonesCount = tmpNodeNode.getMillCount(myColor);
                    if (currentPiecesColor > childChildChildStonesCount) {
                        System.out.println("DENIED MILL - " + "before: " + tmpNode.getPieceCount(myColor)+ "+" + tmpNode.getPieceCount(getEnemyColor(myColor)) + "after: " + tmpNodeNode.getPieceCount(myColor) + "+" + tmpNodeNode.getPieceCount(getEnemyColor(myColor)));
                        bestNode = node;
                        return bestNode;
                    }
                }
            }
            return bestNode;
            */



            //Random selection if everything fails
            if (bestNode == null) {
                if (listOfChildren.size() == 0)
                    return null;
                bestNode = listOfChildren.get(AiUtils.getRandomNumber() % listOfChildren.size());
            }
            return bestNode;
        }




        /*

        }
        return tmpNode;
*/



    public Color getEnemyColor(Color c) {
        if (c.equals(Color.black))
            return Color.white  ;
        return Color.black;
    }

  /*  public byte getPieceCount(Color c) {
        if(c.equals(Color.WHITE))
            return pieceCountW;
        return pieceCountB;
    }
    */

    public byte getMillCount(Color c){
        if (c.equals(Color.WHITE))
            return millCountWhite;
        return millCountBlack;
    }

    @Override
    public String toString() {
        return String.format("Node - Turn: %d Move: %s\tChildren: %d", 0, move, listOfChildren.size());
    }
}