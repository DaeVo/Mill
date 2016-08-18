package view.ai;

import model.Pieces;
import model.Playfield;
import view.AbstractPlayer;

import java.awt.*;
import java.util.Observable;


import static controller.GamePhase.*;


public class StupidAi extends AbstractPlayer {
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
			break;
		}

        System.out.println("");
	}

	private void place(){
		Point toPlace = millController.getState().findFreePlayfield();
		System.out.println("Ki: Placing on " + toPlace);
		millController.place(toPlace);
	}

	private void moving(){
		for (Pieces p : millController.getState().currentPieces) {
			if (p.color == myColor){
                System.out.println("Ki: Trying to move " + p.field);
                for (Playfield toMove : millController.getState().getNeighbors(p.field)){
                    System.out.println("Ki: Target " + toMove);
                    if (millController.move(p.field.getPoint(), toMove.getPoint()))
                        return;
                }
            }
		}
	}

	private void removeStone(){
        for (Pieces p : millController.getState().currentPieces) {
            if (p.color != myColor) {
                millController.removeStone(p.field.getPoint());
            }
        }
    }
}
