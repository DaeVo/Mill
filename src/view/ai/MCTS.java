package view.ai;
import controller.Controller;
import view.AbstractPlayer;

/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {
    private Controller currentState = new Controller();  //current gamestate
    private Node root = new Node(); //global root Node

    /*
    initializes the tree
     */
    public MCTS() {
        root = new Node();
    }

    /*
    chose the best Node
     */
    public void selectMove() {
        //finally decides for a move and sets the root to the next move
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
        Move treeMove = new Move(null, null);

        switch (currentState.getGamePhase()) {
            case Placing:
                treeMove.dst = AiUtils.selectRandomPlacing(currentState);  //dst for placing
                if (simulationHelper(currentNode, treeMove)) {  //checks if that move has already been done
                    currentNode = simulationHelper(currentNode, treeMove, true); //updates currentNode for the next recursive call
                    break;
                } else {
                    treeMove = AiUtils.moving(currentState, abstractPlayer);
                    currentNode = nodeUpdate(currentNode, treeMove, abstractPlayer);
                    break;
                }

            case Moving:
                treeMove = AiUtils.selectRandomMove(currentState, abstractPlayer);
                if (simulationHelper(currentNode, treeMove)) {
                    currentNode = simulationHelper(currentNode, treeMove, true);
                    break;
                } else {
                    // todo: this is where i just left to get some fooderino
                    break;
                }
            case RemovingStone:
                treeMove.src = AiUtils.selectRandomRemove(currentState, abstractPlayer);  //src for removing
                break;
        }
        simulationR(abstractPlayer, currentNode);
    }

    private Node simulationHelper(Node currentNode, Move treeMove, boolean b) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move == treeMove)
                return node;
        }
        return null;
    }

    private boolean simulationHelper(Node currentNode, Move treeMove) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move == treeMove) {
                currentState = node.currenstate;
                return true;
            }
        }
        return false;
    }

    private Node nodeUpdate(Node currentNode, Move treeMove, AbstractPlayer abstractPlayer) {
        Node tmpNode = new Node();
        tmpNode.move = treeMove;
        tmpNode.currenstate = currentState;
        currentNode.listOfChildren.add(tmpNode);
        return tmpNode;
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        toStringR(root, sb, 0);
        return sb.toString();
    }

    public void toStringR(Node node, StringBuilder sb, int depth){
        for (int i = 0; i < depth; i++){
            sb.append(" ");
        }
        sb.append(node);
        for(Node n : node.listOfChildren){
            toStringR(n, sb, ++depth);
        }
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