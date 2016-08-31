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
    public static Node root = null; //global root Node
    private GregorianCalendar expireDate;
    private boolean treeDone = false;
    private static final int RUNFACTOR = 3;
    public int selectionCount;

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
        } else if (moveAlreadyPerformed(root, move)) {
            root = getNodeOfAlreadyPerformedMove(root, move);
        } else {
            root = createChildNode(root, move);
        }
    }



    public Node selection(Node currentNode, IPlayer player, Controller state) {
        selectionCount++;
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

            Node resultNode;
            if (selectionCount % 15 == 0)
                resultNode = currentNode.getBestChild(player.getColor(), state);
            else
                resultNode = currentNode.listOfChildren.get(AiUtils.getRandomNumber() % currentNode.listOfChildren.size());


            List<Move> legalMoves = AiUtils.getLegalMoves(state, player, currentNode, true);
            if (legalMoves.size() == 0) {
                legalMoves = AiUtils.getLegalMoves(state, player, currentNode, false);
                AiUtils.exectuteMove(state, resultNode.move);

                if (state.getGamePhase() == GamePhase.Exit) {
                    return null;
                }
                //resultNode = selection(resultNode, player, state);

            }    return resultNode;
        }
    }

    public Node expansion(Node selectedNode, IPlayer player, Controller state) {
        AiUtils.updateLists(state);
        List<Move> legalMoves = AiUtils.getLegalMoves(state, player, selectedNode, true);

        if (legalMoves.size() == 0) {
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
            Controller c = realController.deepCopy();
            double result = simulationR(kiPlayer, root, c, 0);
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

         if (!state.getGamePhase().equals(GameEnd.BlackWon) || !state.getGamePhase().equals(GameEnd.WhiteWon) || !state.getGamePhase().equals(GameEnd.Draw))
              selectedNode = selection(currentNode, state.getTurnPlayer(), state);
         childNode = expansion(selectedNode, state.getTurnPlayer(), state);
               if (selectedNode == null) {
                //Selection run to dead end.
                //Rerun
                   treeDone = false;
                   selectionCount = 0;
                   return -1;
            }

            //Exit by win
            if (state.getGamePhase().equals(GamePhase.Exit)) {
                if (state.getState().gameEnd.equals(GameEnd.WhiteWon) && kiPlayer.getColor().equals(Color.white)) {
                    result = 1;
                } else if (state.getState().gameEnd.equals(GameEnd.BlackWon) && kiPlayer.getColor().equals(Color.black)) {
                    result = 1;
                } else {
                    //Draw/Loss
                    result = 0;
                }
                //Start update phase
                selectedNode.playCount += 1;
                selectedNode.winCount += result;
                childNode.playCount = 1;
                childNode.winCount = result;
                return result;
            }

            //Exit by time constraint
            if (expireDate.before(new GregorianCalendar())) {
                selectionCount = 0;
                return 0.0;
            }

            result = simulationR(kiPlayer, childNode, state, ++depth);
            selectedNode.playCount += 1;
            selectedNode.winCount += result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        selectionCount = 0;
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
