package view.ai;

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
        switch (millController.getGamePhase()) {
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

    private void place() {

    }

    private void moving() {
    }

    private void removeStone() {
    }
}
