package view.ai;

import controller.Controller;
import model.GameEnd;
import model.GamePhase;
import view.IPlayer;

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
        Node child = root.listOfChildren.get(AiUtils.getRandomNumber() % root.listOfChildren.size());
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


    public Node selection(Node currentNode) {
        if (currentNode.listOfChildren.size() != currentNode.state.getState().getLegelMoveList(currentNode.state.getState().turnColor).size()) {
            return currentNode;
        } else {
            //TODO: select child - random or by win/play ratio
            currentNode.listOfChildren.get(AiUtils.getRandomNumber() % currentNode.listOfChildren.size());
        }

        return null;
    }

    public Node expansion(Node selectedNode) {
        List<Move> legalMoves = selectedNode.state.getState().getLegelMoveList(selectedNode.state.getState().turnColor);
        moveLoop:
        for (Move tmpMove : new LinkedList<>(legalMoves)) {
            for (Node tmpNode : selectedNode.listOfChildren) {
                if (tmpNode.move.equals(tmpMove)) {
                    legalMoves.remove(tmpMove);
                    break moveLoop;
                }
            }
        }

        Move newMove = legalMoves.get(AiUtils.getRandomNumber() % legalMoves.size());

        //TODO: FUCK THIS
        Node newNode = createChildNode(selectedNode, newMove);
        newNode.state = selectedNode.state.deepCopy();
        exectuteMove(newNode);

        return newNode;
    }

    public void exectuteMove(Node currentNode) {
        switch (currentNode.state.getGamePhase()) {
            case Placing:
                currentNode.state.place(currentNode.move.dst);
            case Moving:
                currentNode.state.move(currentNode.move.src, currentNode.move.dst);
            case RemovingStone:
                currentNode.state.removeStone(currentNode.move.src);
        }
    }


    public void simulation(IPlayer abstractPlayer, int timeout) {
        expireDate = new GregorianCalendar();
        expireDate.set(Calendar.MILLISECOND, timeout);

        if (root.state.getState().currentMove != null) {
            doForeignMove(root.state.getState().currentMove);
        }

        while (true){
            //Loop to search new üaths
            simulationR(abstractPlayer, root);
        }
    }

    private double simulationR(IPlayer abstractPlayer, Node currentNode) {
        try {
            // System.out.println("\n\n\n\n\nrecursion state \n" + toString());
            AiUtils.updateLists(currentNode.state);

            Node selectedNode = selection(currentNode);
            Node childNode = expansion(selectedNode);

            //Exit by win
            if (currentNode.state.getGamePhase() == GamePhase.Exit) {
                if (currentNode.state.getState().gameEnd == GameEnd.WhiteWon && abstractPlayer.getColor().equals(Color.white)) {
                    return 1;
                } else if (currentNode.state.getState().gameEnd == GameEnd.BlackWon && abstractPlayer.getColor().equals(Color.black)) {
                    return 1;
                } else {
                    //Draw/Loss
                    return 0;
                }
            }

            //Exit by time contraint
            if (expireDate.before(new GregorianCalendar())) {
                System.out.println("\n\n\n\n\nrecursion state \n" + toString());
                return 0;
            }


            double i = simulationR(abstractPlayer, childNode);
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
