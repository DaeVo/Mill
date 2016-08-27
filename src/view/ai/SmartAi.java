package view.ai;

import controller.Controller;
import model.Move;
import model.AbstractPlayer;

import java.awt.*;

public class SmartAi extends AbstractPlayer {

    private MCTS mctsTree = null;

    @Override
    public void run() {
        System.out.println("smartAI: run()");

        startSimulation();

        Move selectedMove = mctsTree.selectMove();
        System.out.println("smartAI: Selected move " + selectedMove);
        AiUtils.exectuteMove(millController, selectedMove);

        /*
        //!!!!!!Update on the actual controller. DONT REMOVE
        AiUtils.updateLists(millController);
        switch (millController.getGamePhase()) {
            case Placing:
                AiUtils.place(millController);
                break;
            case Moving:
                AiUtils.moving(millController, this);
                break;

            case RemovingStone:
                AiUtils.removeStone(millController, this);
                break;
        }
        */

        System.out.println("smartAI: exit");
    }

    private void startSimulation() {
        mctsTree.simulation(this, 5 * 1000, millController);
    }

    public void turnInfo(Color moveColor, Move move){
        if (move == null){
            //Notify from controller.startGame()
            mctsTree = new MCTS(millController);
            return;
        }

        if (!moveColor.equals(myColor)) {
            System.out.println("smartAI: Foreign move: " + move);
            mctsTree.doForeignMove(millController, move);
        }

    }
}