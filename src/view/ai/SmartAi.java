package view.ai;

import controller.Controller;
import model.Pieces;
import view.AbstractPlayer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class SmartAi extends AbstractPlayer {

    @Override
    public void run() {
        AiUtils.updateLists(millController);
        System.out.println("smartAI: run()");
        switch (millController.getGamePhase())	{
            case Placing:
                place();
                break;
            case Moving:
            case Endgame:
                moving();
                break;

            case RemovingStone:
                removeStone();
                break;
        }

        System.out.println("smartAI: exit");
    }

    private void place(){
        //Copy State
      /*  Controller copyCont = millController.deepCopy();
        if (myColor == Color.black)
            copyCont.setWhitePlayer(new DummyPlayer());
        else
            copyCont.setBlackPlayer(new DummyPlayer());
*/
        millController.place(AiUtils.selectRandomPlacing(millController));
        //init mtcs

        //simulate

        //make real call to controller
    }
    private void moving() {
        Move tmpMove = AiUtils.selectRandomMove(millController, this);
        millController.move(tmpMove.src, tmpMove.dst);
    }

    private void removeStone() {
        millController.removeStone(AiUtils.selectRandomRemove(millController, this));
    }
}