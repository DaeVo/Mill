package view.ai;
import java.util.LinkedList;

/**
 * Created by Max on 19/08/2016.
 * Nodes for the MCTS
 */
class Node {
    public Node() {}
    double winCount;
    double playCount;
    LinkedList<Node>  listOfChildren = new LinkedList<Node>();
}