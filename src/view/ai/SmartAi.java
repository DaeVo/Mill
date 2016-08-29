package view.ai;

import controller.Controller;
import model.Move;
import model.AbstractPlayer;

import java.awt.*;

public class SmartAi extends AbstractPlayer {

    private MCTS mctsTree = null;
    private int thinkTime = 5000;

    public SmartAi(int thinkTime){
        this.thinkTime = thinkTime;
    }


    @Override
    public void run() {
        System.out.println("smartAI: run()");

        //mctsTree.simulation(this, thinkTime, millController);
       mctsTree.MCTSrun(thinkTime, millController, this);
        Move selectedMove = mctsTree.selectMove(myColor, millController.deepCopy());
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


    public void turnInfo(Color moveColor, Move move){
        if (move == null){
            //Notify from controller.startGame()
            mctsTree = new MCTS(millController);
            return;
        }

        if (!moveColor.equals(myColor)) {
            System.out.println("smartAI: Foreign move: " + move);
            mctsTree.doForeignMove(move);
        }

    }
}