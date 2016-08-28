package view.ai;

import controller.Controller;
import model.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {
    private Node root = null; //global root Node
    private GregorianCalendar expireDate;
    private boolean randomSelection = false;

    /*
    initializes the tree
     */
    public MCTS(Controller controller) {
        root = new Node();
        root.state = controller.deepCopy();
    }

    /*
    chose the best Node
     */
    public Move selectMove(Color myColor) { //finally decides for a move and sets the root to the next move
        //logic to select e.g. highest win rate or highest win count node
        // root = selectedNode
        Node child;
        if (randomSelection)
            child = root.listOfChildren.get(AiUtils.getRandomNumber() % root.listOfChildren.size());
        else
            child = root.getBestChild(myColor);

        if (child == null || child.move == null) {
            System.out.print(1);
            child = root.getBestChild(myColor);
        }
        root = child;
        System.out.printf("pc %f, wc %f ", root.playCount, root.winCount);
        return root.move;

    }

    public void doForeignMove(Controller realController, Move move) {
        //Set root to selected child
        //First move
        if (root.move == null) {
            root.move = move;
            AiUtils.exectuteMove(root.state, root.move);
        } else if (moveAlreadyPerformed(root, move)) {
            root = getNodeOfAlreadyPerformedMove(root, move);
        } else {
            root = createChildNode(root, move);
            AiUtils.exectuteMove(root.state, root.move);
        }
    }


    public Node selection(Node currentNode, IPlayer player) {
        if (currentNode.listOfChildren.size() !=  AiUtils.getLegalMovesCount(currentNode.state, player, currentNode)) {
            return currentNode;
        } else {
            //return root.getBestChild(player.getColor());
            return currentNode.listOfChildren.get(AiUtils.getRandomNumber() % currentNode.listOfChildren.size());
        }
    }

    public Node expansion(Node selectedNode, IPlayer player) {
        List<Move> legalMoves = AiUtils.getLegalMoves(selectedNode.state, player, selectedNode);
        if (legalMoves.size() == 0) {
            System.out.print(1);
            AiUtils.getLegalMovesCount(selectedNode.state, player, selectedNode);
        }
        Move newMove = legalMoves.get(AiUtils.getRandomNumber() % legalMoves.size());
        Node newNode = createChildNode(selectedNode, newMove);
        AiUtils.exectuteMove(newNode.state, newNode.move);

        return newNode;
    }


    public void simulation(IPlayer kiPlayer, int timeout, Controller realController) {
        expireDate = new GregorianCalendar();
        expireDate.set(Calendar.MILLISECOND, timeout);

        while (true) {
            //Loop to search new üaths
            simulationR(kiPlayer, root);

            if (expireDate.before(new GregorianCalendar())) {
                break;
            }
        }

        //System.out.println("\n\n\n\n\nrecursion state \n" + toString());
        System.out.printf("Root tree PlayCount %f WinCount %f " ,root.playCount, root.winCount);
    }

    private double simulationR(IPlayer kiPlayer, Node currentNode) {
        try {
            // System.out.println("\n\n\n\n\nrecursion state \n" + toString());
            AiUtils.updateLists(currentNode.state);

            Node selectedNode = selection(currentNode, currentNode.state.getTurnPlayer());
            Node childNode = expansion(selectedNode, currentNode.state.getTurnPlayer());

            //Exit by win
            //System.out.println(selectedNode.state.getState().turn + " " + selectedNode.state.getGamePhase() + " " + childNode.move);
            //BoardFactory.printBoard(childNode.state.getState().board);
            if (childNode.state.getGamePhase().equals(GamePhase.Exit)) {
                //System.out.println("Playout at turn " + childNode.state.getState().turn);
                if (childNode.state.getState().gameEnd.equals(GameEnd.WhiteWon) && kiPlayer.getColor().equals(Color.white)) {
                    return 1;
                } else if (childNode.state.getState().gameEnd.equals(GameEnd.BlackWon) && kiPlayer.getColor().equals(Color.black)) {
                    return 1;
                } else {
                    //Draw/Loss
                    return 0;
                }
            }

            //Exit by time contraint
            if (expireDate.before(new GregorianCalendar())) {
                return 0;
            }

            double i = simulationR(kiPlayer, childNode);
            currentNode.playCount += 1;
            currentNode.winCount += i;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentNode.winCount;
    }


    private boolean moveAlreadyPerformed(Node currentNode, Move move) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move.equals(move)) {
                return true;
            }
        }
        return false;
    }

    private Node getNodeOfAlreadyPerformedMove(Node currentNode, Move move) {
        for (Node node : currentNode.listOfChildren) {
            if (node.move.equals(move))
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
