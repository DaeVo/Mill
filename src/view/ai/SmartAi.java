package view.ai;

import controller.Controller;
import model.Pieces;
import model.Playfield;
import view.AbstractPlayer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class SmartAi extends AbstractPlayer {
    @Override
    public void run() {
        System.out.println("Ki: run()");
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

        System.out.println("");
    }

    private void place(){
        //Copy State
        Controller copyCont = millController.deepCopy();
        if (myColor == Color.black)
            copyCont.setWhitePlayer(new DummyPlayer());
        else
            copyCont.setBlackPlayer(new DummyPlayer());

        //init mtcs

        //simulate

        //make real call to controller
    }

    private void moving(){
    }

    private void removeStone(){
    }
}
