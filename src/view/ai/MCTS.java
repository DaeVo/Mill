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
        Node child = getBestChild(myColor, state, root);
        if (child == null || child.move == null) {
            System.out.println("Select bestChild failed root: " + root);
            child = getBestChild(myColor, state, root);
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
      //  currentNode.millCountBlack = state.getState().getMillPieceCount(Color.black);
      //  currentNode.millCountWhite = state.getState().getMillPieceCount(Color.white);

        int moveCount = AiUtils.getLegalMovesCount(state, player);
        if (moveCount == 0) {
            System.out.println("Selection; No moves available (using backup path) node:" + currentNode);
            AiUtils.getLegalMovesCount(state, player);
        }

        if (currentNode.listOfChildren.size() != moveCount) {
            System.out.println("returning old node");
            return currentNode;
        } else {
            Node tmpNode;
            //Select childs, check for if they are in exit state
            //tmpNode = currentNode.getBestChild(player.getColor(), state);
            tmpNode = currentNode.listOfChildren.get(AiUtils.getRandomNumber() % currentNode.listOfChildren.size());
            // tmpNode = getBestChild(player.getColor(), state, currentNode);  <-- later if sim works.
            AiUtils.exectuteMove(state, tmpNode.move);

            if (state.getGamePhase() == GamePhase.Exit) {
                System.out.println("exit child found in selection!");
                return null;
            }
            System.out.println("selected node: " + tmpNode);
            return tmpNode;
            // resultNode = selection(tmpNode, player, state);
            // return resultNode;
            //return getBestChild(player.getColor() ,state, currentNode);
        }
    }

    public Node expansion(Node selectedNode, IPlayer player, Controller state) {
        AiUtils.updateLists(state);
        List<Move> legalMoves = AiUtils.getLegalMoves(state, player, selectedNode, false);

        if (legalMoves.size() == 0) {
            System.out.println("legalMoves.size() == 0 " + selectedNode);
            legalMoves = AiUtils.getLegalMoves(state, player, selectedNode, true);
        }

        Move newMove = legalMoves.get(AiUtils.getRandomNumber() % legalMoves.size());
        Node newNode = createChildNode(selectedNode, newMove);
        newNode.parent = selectedNode;
        System.out.println("expandedNode: " + newNode + "parent: " + newNode.parent);
        return newNode;
    }


    public Node MCTSrun(int timeout, Controller state, IPlayer player) {
        int check = 0;
        System.out.println("MCTSrun");
        expireDate = new GregorianCalendar();
        expireDate.set(Calendar.MILLISECOND, timeout);
        // while (!expireDate.before(new GregorianCalendar())){
        int count = 0;
        while (!expireDate.before(new GregorianCalendar())) {
            count++;
            System.out.println("MCTS RUNS FOR THE " + count +  " time.");
            Node currentNode = root;

            Controller c = state.deepCopy();

            Node selectedNode = selection(currentNode, player, c);
            System.out.print("starting exp");

            Node expandedNode = expansion(selectedNode, player, c);
            System.out.print("starting sim");


            double tmp = simulation(c, expandedNode, player);
            System.out.print("ending sim");

            backPopagation(currentNode, c, player, tmp);
       /*     selectedNode = SelectLeafNode(rootNode)
            newNode = ExpandNode(selectedNode)
            result = Simulate(NewNode, numberOfSimulations)
            backpropagate(selectedNode, result)
*/
        }
        return getBestChild(player.getColor(), state, root);
    }

    public double simulation(Controller c, Node currentNode, IPlayer player) {

        int stopcheck = 0;

        if (currentNode == null) {
            //Selection run to dead end.
            //Rerun
            treeDone = false;
            return -1;
        }
    System.out.println("LIST SIZE: "  + currentNode.listOfChildren.size());
        int count = 0;
        int result = 0;
        while (c.getGamePhase() != GamePhase.Exit) {
            count++;
            List<Move> legalMoves = AiUtils.getLegalMoves(c, player, currentNode, false);
            currentNode.move = legalMoves.get(AiUtils.getRandomNumber() % legalMoves.size());
            AiUtils.exectuteMove(c, currentNode.move);
            if(count < 50)
            System.out.println("moved + " + count + " - move " + currentNode.move);


            if (expireDate.before(new GregorianCalendar())) {
                System.out.print("breaking through inner timeconstraint");
                return 100;
            }
            stopcheck++;
            //if(stopcheck > 100) return 0;
            if (c.getGamePhase().equals(GamePhase.Exit)) {

                if (c.getState().gameEnd.equals(GameEnd.WhiteWon) && player.getColor().equals(Color.white)) {
                    result = 1;
                } else if (c.getState().gameEnd.equals(GameEnd.BlackWon) && player.getColor().equals(Color.black)) {
                    System.out.print("blackwin");
                    result = 1;
                } else {
                    //Draw/Loss
                    result = 0;
                }
            }
        }
        return result;
    }


    public void backPopagation(Node currentNode, Controller state, IPlayer kiPlayer, double result) {
        while (currentNode != null) {
            currentNode.playCount++;
            currentNode.winCount += result;
            currentNode = currentNode.parent;
        }
    }

  /*  public void simulation(IPlayer kiPlayer, int timeout, Controller realController) {
        expireDate = new GregorianCalendar();
        expireDate.set(Calendar.MILLISECOND, timeout);
        treeDone = false;

        while (!treeDone) {
            //Loop to search new üaths
            Controller c = realController.deepCopy();
            double result = simulationR(kiPlayer, root, c, 0);
            //-1 dead end, 0 draw/loss, 1 win
            if (result >= 0) {
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
            selectedNode.playCount++;
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

*/

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

    public Node getBestChild(Color myColor, Controller state, Node currentNode) {
        byte currentStonesInMill = currentNode.getMillCount(myColor);

         double bestRatio = 0; //0-1 1=only wins
        /*Node bestNode = null;
        for (Node node : currentNode.listOfChildren) {
              //double ratio = node.winCount + Math.sqrt(Math.log(playCount)/ 5 * node.winCount);
           double ratio = node.winCount / node.playCount;
            if (ratio > bestRatio) {
                bestRatio = ratio;
                bestNode = node;
            }
            */

        double tmpWinCount = 0;
        double tmpPlayCount = 9999999;
        Node bestNode = null;
        for (Node node : currentNode.listOfChildren) {
            if (node.winCount >= tmpWinCount && node.playCount <= tmpPlayCount) {
                tmpWinCount = node.winCount;
                tmpPlayCount = node.playCount;
                bestNode = node;
            }



            //Close Mill Heuristic
            int childStonesInMill = node.getMillCount(myColor);

            if (childStonesInMill > currentStonesInMill) {
                bestNode = node;
                break;
            }

        }
        //Random selection if everything fails
        if (bestNode == null) {
            if (currentNode.listOfChildren.size() == 0)
                return null;
            System.out.println("everything failed");
            bestNode = currentNode.listOfChildren.get(AiUtils.getRandomNumber() % currentNode.listOfChildren.size());
        }
        return bestNode;
    }
}

        /*
        double tmpWinCount = 0;
        double tmpPlayCount = Integer.MAX_VALUE;
        Node tmpNode = new Node();
        for (Node children : listOfChildren) {
            if (children.winCount >= tmpWinCount && children.playCount <= tmpPlayCount) {
                tmpWinCount = children.winCount;
                tmpPlayCount = children.playCount;
                tmpNode = children;
            }
        }
        return tmpNode;

}
*/