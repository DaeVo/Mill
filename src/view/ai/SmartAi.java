package view.ai;

import view.AbstractPlayer;

public class SmartAi extends AbstractPlayer {

    private MCTS mctsTree = null;

    @Override
    public void run() {
        System.out.println("smartAI: run()");

        if (mctsTree == null)
            mctsTree = new MCTS(millController);
        //startSimulation();

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

        System.out.println("smartAI: exit");
    }

    private void startSimulation() {
        mctsTree.simulation(this, 5 * 1000);
    }
}