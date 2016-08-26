package view.human;


import model.GamePhase;
import view.AbstractPlayer;

import java.awt.*;
import java.util.Scanner;

public class ConsoleView extends AbstractPlayer {
	Scanner sc = new Scanner(System.in);

	@Override
	public void run() {
		System.out.println("Console: run()");
		switch (millController.getGamePhase())	{
			case Placing:
				place();
				break;
			case Moving:
				move();
				break;
			case RemovingStone:
				remove();
				break;
		}
	}

	private void place(){
		boolean validMove = false;
		while (!validMove) {
			System.out.println("Place a stone");
			validMove = millController.place(readCords());
		}
	}

	private void remove(){
		boolean validRemove = false;
		while (!validRemove) {
			System.out.println("Remove an enemy stone");
			validRemove = millController.removeStone(readCords());
		}
	}
	private void move(){

		boolean validMove = false;
		while (!validMove) {
			System.out.println("Move a stone. Source Place:");
			Point src = readCords();
			System.out.println("Destation Place:");
			Point dst = readCords();

			if (millController.getGamePhase() == GamePhase.Moving)
				validMove = millController.move(src, dst);
			else
				validMove = millController.moveFreely(src, dst);
		}

	}
	private Point readCords(){
		System.out.println("Format x y - X: 0-7 Y: 0-2 - Bsp 5 1");
		String tmp = sc.next();
		Integer x = Integer.parseInt(tmp);
		tmp = sc.next();
		Integer y = Integer.parseInt(tmp);

		return new Point(x, y);
	}
}

