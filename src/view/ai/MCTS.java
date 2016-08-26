package view.ai;
import controller.Controller;
import controller.GamePhase;
import model.Gamestate;
import view.AbstractPlayer;

import java.awt.Point;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;
import java.util.List;

/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {


    private Controller currentState = new Controller();  //current gamestate
    public Node root = new Node(); //global root Node
    /*
    initializes the tree
     */
    public void initializeMCTS(){
        root = new Node();
    }

    /*
    chose the best Node
     */
    public void selectMove() { //finally decides for a move and sets the root to the next move
        //logic to select e.g. highest win rate or highest win count node
        // root = selectedNode
    }

    public void updateCurrentGameState(){
        currentState = currentState.deepCopy();
    }

    public void simulation(AbstractPlayer abstractPlayer) {
        //Copy State
        updateCurrentGameState();
        currentState.setWhitePlayer(new DummyPlayer());
        currentState.setBlackPlayer(new DummyPlayer());


        AiUtils.updateLists(currentState);
        System.out.println("smartAI: run()");
        switch (currentState.getGamePhase()) {
            case Placing:
                AiUtils.place(currentState);
                break;
            case Moving:
            case Endgame:
                AiUtils.moving(currentState, abstractPlayer);
                break;

            case RemovingStone:
                AiUtils.removeStone(currentState, abstractPlayer);
                break;
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
    }
