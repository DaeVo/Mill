package view.ai;

import model.Piece;
import model.Playfield;
import view.AbstractPlayer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class StupidAi extends AbstractPlayer {
	@Override
	public void run() {
		System.out.println("Ki: run()");
		switch (millController.getGamePhase())	{
		case Placing:
			place();
			break;
		case Moving:
		    moving();
			break;

		case RemovingStone:
			removeStone();
			break;
		}

		System.out.println("Ki: exit");
	}

	private void place(){
		Point toPlace = millController.getState().findFreePlayfield();
		System.out.println("Ki: Placing on " + toPlace);
		millController.place(toPlace);
	}

	private void moving(){
		for (Piece p : millController.getState().currentPieces) {
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
        System.out.println("Ki: Trying to remove ich");
        List<Piece> listCopy = new LinkedList<>(millController.getState().currentPieces);
        for (Piece p : listCopy) {
            if (p.color != myColor) {
                System.out.println("Ki: Trying to remove " + p.field);
                if (millController.removeStone(p.field.getPoint()))
                	return;
            }
        }
    }
}
