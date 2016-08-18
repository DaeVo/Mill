package view.ai;

import model.Pieces;
import model.Playfield;
import view.AbstractPlayer;

import java.awt.*;


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
			removeStone();
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
                for (Playfield toMove : millController.getState().getNeighbours(p.field)){
                    System.out.println("Ki: Target " + toMove);
                    if (millController.move(p.field.getPoint(), toMove.getPoint()))
                        return;
                }
            }
		}
	}

	private void removeStone(){
		System.out.println("ich bin dumm und scheiße und kann keinen stein removen weil ich ne dumme ki bin");
        for (Pieces p : millController.getState().currentPieces) {
            if (p.color != myColor) {
                millController.removeStone(p.field.getPoint());
            }
        }
    }
}
