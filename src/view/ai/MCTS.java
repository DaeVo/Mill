package view.ai;

import controller.Controller;
import model.GameEnd;
import model.GamePhase;
import model.Move;
import model.IPlayer;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {
    private Node root = new Node(); //global root Node
    private GregorianCalendar expireDate;
    private boolean randomSelection = false;

    /*
    initializes the tree
     */
    public MCTS(Controller cont) {
        root = new Node();
        root.state = cont.deepCopy();
    }

    /*
    chose the best Node
     */
    public Move selectMove() { //finally decides for a move and sets the root to the next move
        //logic to select e.g. highest win rate or highest win count node
        // root = selectedNode
        Node child;
        if (randomSelection)
            child = root.listOfChildren.get(AiUtils.getRandomNumber() % root.listOfChildren.size());
        else
            child = root.getBestChild();

        root = child;
        return root.move;
    }

    public void doForeignMove(Move move) {
        //Set root to selected child
        if (moveAlreadyPerformed(root, move)) {
            root = getNodeOfAlreadyPerformedMove(root, move);
        } else {
            root = createChildNode(root, move);
        }
    }


    public Node selection(Node currentNode, IPlayer player) {
        if (currentNode.listOfChildren.size() !=  AiUtils.getLegalMoves(currentNode.state, player, currentNode).size()) {
            return currentNode;
        } else {
            return root.listOfChildren.get(AiUtils.getRandomNumber() % root.listOfChildren.size());
        }
    }

    public Node expansion(Node selectedNode, IPlayer player) {
        List<Move> legalMoves = AiUtils.getLegalMoves(selectedNode.state, player, selectedNode);
        Move newMove = legalMoves.get(AiUtils.getRandomNumber() % legalMoves.size());

        Node newNode = createChildNode(selectedNode, newMove);
        AiUtils.exectuteMove(newNode.state, newNode.move);

        return newNode;
    }


    public void simulation(IPlayer abstractPlayer, int timeout) {
        expireDate = new GregorianCalendar();
        expireDate.set(Calendar.MILLISECOND, timeout);

        if (root.state.getState().currentMove != null) {
            doForeignMove(root.state.getState().currentMove);
        }

        while (true) {
            //Loop to search new üaths
            simulationR(abstractPlayer, root);

            if (expireDate.before(new GregorianCalendar())) {
                System.out.println("\n\n\n\n\nrecursion state \n" + toString());
                break;
            }
        }
    }

    private double simulationR(IPlayer player, Node currentNode) {
        try {
            // System.out.println("\n\n\n\n\nrecursion state \n" + toString());
            AiUtils.updateLists(currentNode.state);

            Node selectedNode = selection(currentNode, player);
            Node childNode = expansion(selectedNode, player);

            //Exit by win
            System.out.println(childNode.state.getGamePhase());
            if (childNode.state.getGamePhase().equals(GamePhase.Exit)) {
                System.out.println("Playout at turn " + childNode.state.getState().turn);
                if (childNode.state.getState().gameEnd.equals(GameEnd.WhiteWon) && player.getColor().equals(Color.white)) {
                    return 1;
                } else if (childNode.state.getState().gameEnd.equals(GameEnd.BlackWon) && player.getColor().equals(Color.black)) {
                    return 1;
                } else {
                    //Draw/Loss
                    return 0;
                }
            }

            //Exit by time contraint
            if (expireDate.before(new GregorianCalendar())) {
                System.out.println("\n\n\n\n\nrecursion state \n" + toString());
                //return 0;
            }

            double i = simulationR(player, childNode);
            currentNode.playCount += 1;
            currentNode.winCount += i;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentNode.winCount;
    }


    private boolean moveAlreadyPerformed(Node currentNode, Move treeMove) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move.equals(treeMove)) {
                return true;
            }
        }
        return false;
    }

    private Node getNodeOfAlreadyPerformedMove(Node currentNode, Move treeMove) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move.equals(treeMove))
                return node;
        }
        return null;
    }

    private Node createChildNode(Node currentNode, Move move) {
        Node tmpNode = new Node();
        tmpNode.move = move;
        tmpNode.state = currentNode.state.deepCopy();
        currentNode.listOfChildren.add(tmpNode);
        return tmpNode;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringR(root, sb, 0);
        return sb.toString();
    }

    public void toStringR(Node node, StringBuilder sb, int depth) {
        for (int i = 0; i < depth; i++) {
            sb.append(" ");
        }
        sb.append(node);
        sb.append("\n");
        for (Node n : node.listOfChildren) {
            toStringR(n, sb, ++depth);
        }
    }
}
