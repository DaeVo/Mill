package view.ai;
import model.Gamestate;
import java.awt.Point;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;
import java.util.List;

/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {



    public Node root; //global root Node

    /*
    initializes the tree
     */
    public void initializeMCTS(){
        root = new Node();
    }

    /*
    chose the best Node
     */
    public void selectMove() { //finally decides for a move and sets the root to the next move
        //logic to select e.g. highest win rate or highest win count node
        // root = selectedNode
    }
}
