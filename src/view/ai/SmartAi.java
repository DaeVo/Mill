package view.ai;

import view.AbstractPlayer;

public class SmartAi extends AbstractPlayer {

    private MCTS mctsTree = new MCTS();

    @Override
    public void run() {
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

    private void startSimulation(){
        MCTSobject.simulation(this);
    }
}