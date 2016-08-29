package view.ai;

import controller.Controller;
import model.Move;
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
    public Node parent;
    public Move move;
    public LinkedList<Node> listOfChildren = new LinkedList<>(); //legalMoves = children

  /*  public Node getBestChild(Color myColor, Controller state) {
        byte currentStonesInMill = getMillCount(myColor);

    /*    double bestRatio = 0; //0-1 1=only wins
        Node bestNode = null;
        for (Node node : listOfChildren) {
            //double ratio = node.winCount + Math.sqrt(Math.log(playCount)/ 5 * node.winCount);
            double ratio = node.winCount / node.playCount;
            if (ratio > bestRatio) {
                bestRatio = ratio;
                bestNode = node;
            }


        double tmpWinCount = 0;
        double tmpPlayCount = 9999999;
        Node bestNode = null;
        for (Node node : listOfChildren) {
            if (node.winCount >= tmpWinCount && node.playCount <= tmpPlayCount) {
                tmpWinCount = node.winCount;
                tmpPlayCount = node.playCount;
                bestNode = node;
            }



            //Close Mill Heuristic
            int childStonesInMill = node.getMillCount(myColor);

            if (childStonesInMill > currentStonesInMill) {
                bestNode = node;
                break;
            }

        }
        //Random selection if everything fails
        if (bestNode == null) {
            if (listOfChildren.size() == 0)
                return null;
            bestNode = listOfChildren.get(AiUtils.getRandomNumber() % listOfChildren.size());
        }
        return bestNode;
*/

        /*
        double tmpWinCount = 0;
        double tmpPlayCount = Integer.MAX_VALUE;
        Node tmpNode = new Node();
        for (Node children : listOfChildren) {
            if (children.winCount >= tmpWinCount && children.playCount <= tmpPlayCount) {
                tmpWinCount = children.winCount;
                tmpPlayCount = children.playCount;
                tmpNode = children;
            }
        }
        return tmpNode;

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