package view.ai;
import controller.Controller;
import view.AbstractPlayer;

/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {


    private Controller currentState = new Controller();  //current gamestate
    public Node root = new Node(); //global root Node
    Move treeMove = new Move(null, null);

    /*
    initializes the tree
     */
    public void initializeMCTS() {
        root = new Node();
    }

    /*
    chose the best Node
     */
    public void selectMove() { //finally decides for a move and sets the root to the next move
        //logic to select e.g. highest win rate or highest win count node
        // root = selectedNode
    }

    public void updateCurrentGameState() {
        currentState = currentState.deepCopy();
    }

    private void simulationR(AbstractPlayer abstractPlayer, Node currentNode) {
        updateCurrentGameState();
        AiUtils.updateLists(currentState);
        System.out.println("recursion");

        switch (currentState.getGamePhase()) {
            case Placing:
                treeMove.dst = AiUtils.selectRandomPlacing(currentState);  //dst for placing
                if(simulationHelper(currentNode, treeMove)) {  //checks if that move has already been done
                    currentNode = simulationHelper(currentNode, treeMove, true); //updates currentNode for the next recursive call
                    break;
                }
                else {
                    treeMove = AiUtils.moving(currentState, abstractPlayer);
                    Node tmpNode = new Node();
                    tmpNode.move = treeMove;
                    tmpNode.currenstate = this.currentState;
                    currentNode.listOfChildren.add(tmpNode);
                }
                break;
            case Moving:
                treeMove = AiUtils.selectRandomMove(currentState, abstractPlayer);
                break;
            case RemovingStone:
                treeMove.src = AiUtils.selectRandomRemove(currentState, abstractPlayer);  //src for removing
                break;
        }
        simulationR(abstractPlayer, currentNode);
    }
    private Node simulationHelper (Node currentNode, Move treeMove, boolean b) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move == treeMove) return node;
        } return null;
    }

    private boolean simulationHelper(Node currentNode, Move treeMove) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move == treeMove) {
                currentState = node.currenstate;
                return true;}
        } return false;
    }
}



        /*

        Controller copyCont = millController.deepCopy();
        if (myColor == Color.black)
            copyCont.setWhitePlayer(new DummyPlayer());
        else
            copyCont.setBlackPlayer(new DummyPlayer());
            */

//init mtcs

//simulate

//make real call to controller