package view.ai;

/**
 * Created by Max on 19/08/2016.
 */
public class MCTS {

    //todo: deep copy of controller (or maybe pieces + board + playfields?)

    public Node root = new Node(); //global Root Node
    //public Node createTree(){}


    /*
    resets the tree
     */
    public void initializeMCTS(){
           root = new Node();
    }
}
