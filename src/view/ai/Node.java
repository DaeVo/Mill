package view.ai;

import controller.Controller;

import java.util.LinkedList;
import java.awt.Point;

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

    @Override
    public String toString() {
        return String.format("Node - Move: %s\tChildren: %d", move, listOfChildren.size());
    }
}