package view.ai;

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
		//Point toPlace = millController.getBoard().findFreeNode();
		//System.out.println("Ki: Placing on " + toPlace);
		//millController.place(toPlace);
	}
	
}
