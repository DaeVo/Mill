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
    private boolean treeDone = false;
    private static final int RUNFACTOR = 3;

    /*
    initializes the tree
     */
    public MCTS(Controller controller) {
        root = new Node();
    }

    /*
    chose the best Node
     */
    public Move selectMove(Color myColor, Controller state) { //finally decides for a move and sets the root to the next move
        //logic to select e.g. highest win rate or highest win count node
        // root = selectedNode
        Node child = root.getBestChild(myColor, state);
        if (child == null || child.move == null) {
            System.out.println("Select bestChild failed root: " + root);
            child = root.getBestChild(myColor, state);
        }

        System.out.printf("Root tree PlayCount %f WinCount %f pc %f, wc %f ", root.playCount, root.winCount, child.playCount, child.winCount);
        root = child;
        return root.move;

    }

    public void doForeignMove(Move move) {
        //Set root to selected child
        //First move
        if (root.move == null) {
            root.move = move;
            //AiUtils.exectuteMove(root.state, root.move);
        } else if (moveAlreadyPerformed(root, move)) {
            root = getNodeOfAlreadyPerformedMove(root, move);
        } else {
            root = createChildNode(root, move);
            //AiUtils.exectuteMove(root.state, root.move);
        }
    }


    public Node selection(Node currentNode, IPlayer player, Controller state) {
        AiUtils.updateLists(state);
        currentNode.millCountBlack = state.getState().getMillPieceCount(Color.black);
        currentNode.millCountWhite = state.getState().getMillPieceCount(Color.white);

        int moveCount = AiUtils.getLegalMovesCount(state, player);
        if (moveCount == 0) {
            System.out.println("Selection; No moves available (using backup path) node:" + currentNode);
            AiUtils.getLegalMovesCount(state, player);
        }

        if (currentNode.listOfChildren.size() != moveCount) {
            return currentNode;
        } else {
            Node tmpNode;
            Node resultNode;

            //Select childs, check for if they are in exit state
            tmpNode = currentNode.getBestChild(player.getColor(), state);
            AiUtils.exectuteMove(state, tmpNode.move);

            if (state.getGamePhase() == GamePhase.Exit) {
                //exit child found!
                return null;
            }
            resultNode = selection(tmpNode, player, state);
            return resultNode;
        }
    }

    public Node expansion(Node selectedNode, IPlayer player, Controller state) {
        AiUtils.updateLists(state);
        List<Move> legalMoves = AiUtils.getLegalMoves(state, player, selectedNode, true);

        if (legalMoves.size() == 0) {
            System.out.println("legalMoves.size() == 0 " + selectedNode);
            legalMoves = AiUtils.getLegalMoves(state, player, selectedNode, false);
        }
        Move newMove = legalMoves.get(AiUtils.getRandomNumber() % legalMoves.size());
        Node newNode = createChildNode(selectedNode, newMove);
        AiUtils.exectuteMove(state, newNode.move);
        return newNode;
    }


    public void simulation(IPlayer kiPlayer, int timeout, Controller realController) {
        expireDate = new GregorianCalendar();
        expireDate.set(Calendar.MILLISECOND, timeout);
        treeDone = false;

        while (!treeDone) {
            //Loop to search new üaths
            Controller c = realController.deepCopy();
            double result = simulationR(kiPlayer, root, c, 0);
            //-1 dead end, 0 draw/loss, 1 win
            if (result >= 0){
                root.playCount += 1;
                root.winCount += result;
            }


            if (expireDate.before(new GregorianCalendar())) {
                break;
            }
        }
    }

    private double simulationR(IPlayer kiPlayer, Node currentNode, Controller state, int depth) {
        Node selectedNode = null;
        Node childNode = null;
        double result = 0;

        try {
            if (depth > 500)
                System.out.print(1);

            selectedNode = selection(currentNode, state.getTurnPlayer(), state);
            if (selectedNode == null) {
                //Selection run to dead end.
                //Rerun
                treeDone = false;
                return -1;
            }

            childNode = expansion(selectedNode, state.getTurnPlayer(), state);

            //Exit by win
            //System.out.println(selectedNode.state.getState().turn + " " + selectedNode.state.getGamePhase() + " " + childNode.move);
            //BoardFactory.printBoard(childNode.state.getState().board);
            if (state.getGamePhase().equals(GamePhase.Exit)) {
                //System.out.println("Playout at turn " + childNode.state.getState().turn);
                if (state.getState().gameEnd.equals(GameEnd.WhiteWon) && kiPlayer.getColor().equals(Color.white)) {
                    result = 1;
                } else if (state.getState().gameEnd.equals(GameEnd.BlackWon) && kiPlayer.getColor().equals(Color.black)) {
                    result = 1;
                } else {
                    //Draw/Loss
                    result = 0;
                }
                //Start update phase :)
                selectedNode.playCount += 1;
                selectedNode.winCount += result;
                childNode.playCount = 1;
                childNode.winCount = result;
                return result;
            }

            //Exit by time constraint
            if (expireDate.before(new GregorianCalendar())) {
                return 0.0;
            }

            result = simulationR(kiPlayer, childNode, state, ++depth);
            selectedNode.playCount += 1;
            selectedNode.winCount += result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
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
        //tmpNode.state = currentNode.state.deepCopy();
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
