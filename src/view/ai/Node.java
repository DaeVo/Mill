package view.ai;
import java.util.LinkedList;
import java.awt.Point;

/**
 * Created by Max on 19/08/2016.
 * Nodes for the MCTS
 */
class Node {
    public Node() {}
    double winCount;
    double playCount;
    Point src;
    Point dst;
    LinkedList<Node>  listOfChildren = new LinkedList<Node>();
}