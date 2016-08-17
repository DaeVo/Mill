package view.human;


import view.AbstractPlayer;

public class ConsoleView extends AbstractPlayer {
	
	@Override
	public void run() {
		System.out.println("Console: run()");
		switch (millController.getGamePhase())	{
			case Placing:
				break;
			case Moving:
				break;
			case Endgame:
				break;
			case RemovingStone:
				break;
		}
	}
}

