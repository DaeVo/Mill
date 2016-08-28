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
    public Move move;
    public Controller state;
    public LinkedList<Node> listOfChildren = new LinkedList<>(); //legalMoves = children

    public Node getBestChild(Color myColor) {
        int currentStonesInMill = state.getState().getMillPieceCount(myColor);

        double bestRatio = 0; //0-1 1=only wins
        Node bestNode = null;
        for (Node node : listOfChildren) {
            double ratio = node.winCount + Math.sqrt(Math.log(playCount)/ 2 * node.winCount);
            //node.winCount / node.playCount;
            if (ratio > bestRatio) {
                bestRatio = ratio;
                bestNode = node;
            }

            //Close Mill Heuristic
            int childStonesInMill = node.state.getState().getMillPieceCount(myColor);

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
*/

    }

    @Override
    public String toString() {
        return String.format("Node - Turn: %d Move: %s\tChildren: %d", state.getState().turn, move, listOfChildren.size());
    }
}