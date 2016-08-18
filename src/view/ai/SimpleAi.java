package view.ai;

import model.Pieces;
import view.AbstractPlayer;

import java.awt.Point;
import java.util.Observable;


import static controller.GamePhase.*;


public class SimpleAi extends AbstractPlayer {
	@Override
	public void run() {
		System.out.println("Ki: run()");
		switch (millController.getGamePhase())	{
		case Placing:
			place();
			break;
		case Moving:
			break;
		case Endgame:
			break;
		case RemovingStone:
			break;
		}
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

            }
		}



	}
}
