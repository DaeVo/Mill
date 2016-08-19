package view.ai;

/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {

    //todo: deep copy of controller (or maybe pieces + board + playfields?)


    public Node root; //global Root Node

    /*
    initializes the tree
     */
    public void initializeMCTS(){
        root = new Node();
    }
}
