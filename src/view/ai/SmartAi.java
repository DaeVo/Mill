package view.ai;

import controller.Controller;
import model.Move;
import model.AbstractPlayer;

import java.awt.*;

public class SmartAi extends AbstractPlayer {

    private MCTS mctsTree = null;
    public int thinkTime = 0;

    public SmartAi(int thinkTime){
        this.thinkTime = thinkTime;
    }

    @Override
    public void run() {
        System.out.println("smartAI: run()");

        mctsTree.simulation(this, thinkTime, millController);

        Move selectedMove = mctsTree.selectMove(myColor, millController.deepCopy());
        System.out.println("smartAI: Selected move " + selectedMove);
        AiUtils.exectuteMove(millController, selectedMove);
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