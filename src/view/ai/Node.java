package view.ai;

import java.util.LinkedList;
import java.awt.Point;

/**
 * Created by Max on 19/08/2016.
 * Nodes for the MCTS
 */
class Node {
    public Node() {
    }

    double winCount;   //if ai  wins the simulated playout  - winCount++;
    double playCount;  //if simulated game ends -  playCount++
    Move move;
    LinkedList<Node> listOfChildren = new LinkedList<Node>(); //legalMoves = children
}