package controller;

import model.Gamestate;
import view.IPlayer;

import java.awt.*;
import java.util.List;

public class Controller {
	private IPlayer blackPlayer;
	private IPlayer whitePlayer;
	private Gamestate gameBoard;
	
	private int turn;
	private Color turnColor;
	private GamePhase gameState;
	private GamePhase oldState;

	
	public Controller(Gamestate gs){
		gameBoard = gs;
	}
	
	public void startGame(IPlayer bp, IPlayer wp){
		//notfiy first playwer
		blackPlayer = bp;
		whitePlayer = wp;
		blackPlayer.setController(this);
		whitePlayer.setController(this);
		
		turn = 1;
		turnColor = Color.black;
		gameState = GamePhase.Placing;
		printTurnInfo();
		
		new Thread(blackPlayer).start();
	}
	
	public void place(Point p){
		/*
		gameBoard.modifyNode(turnColor, p);
		
		if (gameBoard.getNodeCount() == 18)
			gameState = GamePhase.Moving;
			*/
		endTurn();
	}
	
	
	public void move(Point src, Point dst){
		//gameBoard.move(src, dst);
		

		
		endTurn();
	}
	
	public void removeStone(Point p){
		//gameBoard.modifyNode(NodeStatus.Free, p);
		gameState = oldState;
		endTurn();
	}
	
	
	private void endTurn(){
		/*
		List<MillInfo> newMills = gameBoard.getMills();
		if (oldMills.size() != newMills.size()){
			//the turncolor got a new mill
			//turn is not ended
			oldState = gameState;
			gameState = GamePhase.RemovingStone;
			return;
		}
		*/
		
		
		turn++;
		Thread toStart = null;
		if (turnColor == Color.black) {
			turnColor = Color.white;
			toStart = new Thread(whitePlayer);
		} else {
			turnColor = Color.black;
			toStart = new Thread(blackPlayer);
		}
		
		printTurnInfo();
		toStart.start();
	}

	
	public GamePhase getGamePhase(){
		return gameState;
	}
	
	public Gamestate getState(){
		return gameBoard;
	}
	
	private void printTurnInfo(){
		System.out.println("Turn " + turn);
		System.out.println("Next Player " + turnColor);
		System.out.println(gameBoard);
		System.out.println();
	}
}
