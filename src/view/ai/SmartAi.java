package view.ai;

import controller.Controller;
import model.Pieces;
import view.AbstractPlayer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.math.*;

public class SmartAi extends AbstractPlayer {

    private MCTS MCTSobject = new MCTS();

    @Override
    public void run() {
        AiUtils.updateLists(millController);
        System.out.println("smartAI: run()");
        switch (millController.getGamePhase())	{
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

        System.out.println("smartAI: exit");
    }

   /* private void startSimulation(){
        MCTSobject.simulation(this);
    }
    */
}