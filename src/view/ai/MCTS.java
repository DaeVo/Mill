package view.ai;
import controller.Controller;
import model.GameEnd;
import model.GamePhase;
import view.AbstractPlayer;

import java.awt.*;

/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {
    private Controller currentState;  //current gamestate
    private Node root = new Node(); //global root Node

    /*
    initializes the tree
     */
    public MCTS(Controller cont) {
        root = new Node();
        currentState = cont;
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

    public void simulation(AbstractPlayer abstractPlayer) {
        if (currentState.getState().currentMove != null){
            if(moveAlreadyPerformed(root, currentState.getState().currentMove)) {
                root = getNodeOfAlreadyPerformedMove(root, currentState.getState().currentMove);
            }
            else {
                root = nodeUpdate(root, currentState.getState().currentMove);
            }
        }
        simulationR(abstractPlayer, root);
    }

    private int simulationR(AbstractPlayer abstractPlayer, Node currentNode) {
        updateCurrentGameState();
        AiUtils.updateLists(currentState);
        System.out.println("\n\n\n\n\nrecursion state \n" + toString());
        Move treeMove = new Move(null, null);

        switch (currentState.getGamePhase()) {
            case Placing:
                treeMove.dst = AiUtils.selectRandomPlacing(currentState);  //dst for placing
                if (moveAlreadyPerformed(currentNode, treeMove)) {  //checks if that move has already been done
                    currentNode = getNodeOfAlreadyPerformedMove(currentNode, treeMove); //updates currentNode for the next recursive call
                    break;
                } else {
                    treeMove = AiUtils.place(currentState);
                    currentNode = nodeUpdate(currentNode, treeMove);
                    break;
                }
            case Moving:
                treeMove = AiUtils.selectRandomMove(currentState, abstractPlayer);
                if (moveAlreadyPerformed(currentNode, treeMove)) {
                    currentNode = getNodeOfAlreadyPerformedMove(currentNode, treeMove);
                    break;
                } else {
                    // todo: this is where i just left to get some fooderino
                    treeMove = AiUtils.moving(currentState, abstractPlayer);
                    currentNode = nodeUpdate(currentNode, treeMove);
                    break;
                }
            case RemovingStone:
                treeMove.src = AiUtils.selectRandomRemove(currentState, abstractPlayer);  //src for removing
                if (moveAlreadyPerformed(currentNode, treeMove)) {
                    currentNode = getNodeOfAlreadyPerformedMove(currentNode, treeMove);
                    break;
                }
                else {
                    treeMove = AiUtils.removeStone(currentState, abstractPlayer);
                    currentNode = nodeUpdate(currentNode, treeMove);
                }
                break;
        }

        if (currentState.getGamePhase() == GamePhase.Exit){
            if (currentState.getState().gameEnd == GameEnd.WhiteWon && abstractPlayer.getColor() == Color.white){
                return 1;
            } else if (currentState.getState().gameEnd == GameEnd.BlackWon && abstractPlayer.getColor() == Color.black){
                return 1;
            } else {
                //Draw/Loss
                return 0;
            }
        }

        int i = simulationR(abstractPlayer, currentNode);
        currentNode.winCount += i;
        return i;
    }

    private Node getNodeOfAlreadyPerformedMove(Node currentNode, Move treeMove) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move == treeMove)
                return node;
        }
        return null;
    }

    private boolean moveAlreadyPerformed (Node currentNode, Move treeMove) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move == treeMove) {
                currentState = node.currenstate;
                return true;
            }
        }
        return false;
    }

    private Node nodeUpdate(Node currentNode, Move treeMove) {
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
        sb.append("\n");
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