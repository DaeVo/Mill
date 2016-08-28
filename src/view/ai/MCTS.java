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

        System.out.printf("Root tree PlayCount %f WinCount %f pc %f, wc %f " ,root.playCount, root.winCount, child.playCount, child.winCount);
        root = child;
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
        AiUtils.updateLists(currentNode.state);
        int moveCount = AiUtils.getLegalMovesCount(currentNode.state, player, currentNode);
        if (moveCount == 0) {
            System.out.print(1);
            AiUtils.getLegalMovesCount(currentNode.state, player, currentNode);
        }

        if (currentNode.listOfChildren.size() !=  moveCount) {
            return currentNode;
        } else {
            //return selection(currentNode.getBestChild(player.getColor()), player);
            Node tmpNode ;
            do {
                tmpNode= currentNode.listOfChildren.get(AiUtils.getRandomNumber() % currentNode.listOfChildren.size());
                //deadlock?
            } while (tmpNode.state.getGamePhase() == GamePhase.Exit);
            return selection(tmpNode, player);
        }
    }

    public Node expansion(Node selectedNode, IPlayer player) {
        AiUtils.updateLists(selectedNode.state);
        List<Move> legalMoves = AiUtils.getLegalMoves(selectedNode.state, player, selectedNode, true);

        if (legalMoves.size() == 0) {
            legalMoves = AiUtils.getLegalMoves(selectedNode.state, player, selectedNode, false);
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
            double result = simulationR(kiPlayer, root, 0);
            root.playCount += 1;
            root.winCount += result;

            if (expireDate.before(new GregorianCalendar())) {
                break;
            }
        }
    }

    private double simulationR(IPlayer kiPlayer, Node currentNode, int depth) {
        Node selectedNode = null;
        Node childNode = null;

        try {
            if (depth > 500)
                System.out.print(1);
            // System.out.println("\n\n\n\n\nrecursion state \n" + toString());

            selectedNode = selection(currentNode, currentNode.state.getTurnPlayer());
            childNode = expansion(selectedNode, currentNode.state.getTurnPlayer());

            //Exit by win
            //System.out.println(selectedNode.state.getState().turn + " " + selectedNode.state.getGamePhase() + " " + childNode.move);
            //BoardFactory.printBoard(childNode.state.getState().board);
            if (childNode.state.getGamePhase().equals(GamePhase.Exit)) {
                selectedNode.playCount = 1;
                childNode.playCount = 1;
                //System.out.println("Playout at turn " + childNode.state.getState().turn);
                if (childNode.state.getState().gameEnd.equals(GameEnd.WhiteWon) && kiPlayer.getColor().equals(Color.white)) {
                    return 1.0;
                } else if (childNode.state.getState().gameEnd.equals(GameEnd.BlackWon) && kiPlayer.getColor().equals(Color.black)) {
                    return 1.0;
                } else {
                    //Draw/Loss
                    return 0.0;
                }
            }

            //Exit by time contraint
            if (expireDate.before(new GregorianCalendar())) {
                return 0.0;
            }

            double result = simulationR(kiPlayer, childNode, ++depth);
            selectedNode.playCount += 1;
            selectedNode.winCount += result;

            if (childNode.listOfChildren.size() > childNode.playCount)
                System.out.print(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return selectedNode.winCount;
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
