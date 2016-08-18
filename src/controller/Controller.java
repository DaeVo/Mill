package controller;

import model.BoardFactory;
import model.Gamestate;
import model.Pieces;
import model.Playfield;
import view.IPlayer;

import java.awt.*;

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
		blackPlayer.create(this, Color.black);
		whitePlayer.create(this, Color.white);
		
		turn = 1;
		turnColor = Color.black;
		gameState = GamePhase.Placing;
		printTurnInfo();
		
		new Thread(blackPlayer).start();
	}
	
	public void place(Point p){
		Pieces piece = new Pieces(turnColor);
		gameBoard.currentPieces.add(piece);
		gameBoard.board[p.x][p.y].addPiece(piece);

		if (gameBoard.getPieceCount() == 18)
			gameState = GamePhase.Moving;
		endTurn();
	}
	
	
	public boolean move(Point src, Point dst){
		boolean success = gameBoard.board[src.x][src.y].move(gameBoard.board[dst.x][dst.y]);

		if(success)
			endTurn();
		return success;
	}

	public boolean moveFreely(Point src, Point dst){
		boolean success = gameBoard.board[src.x][src.y].moveFreely(gameBoard.board[dst.x][dst.y]);

		if(success)
			endTurn();
		return success;
	}

	public boolean removeStone(Point p){
		Pieces oldPiece = gameBoard.board[p.x][p.y].piece;

		if (oldPiece == null || oldPiece.color == turnColor)
			return false;
		//TODO: IN MILL CHECK

		gameBoard.currentPieces.remove(oldPiece);
		gameBoard.board[p.x][p.y].conquerField(new Playfield(false));

		gameState = oldState;
		endTurn();
		return true;
	}
	
	
	private void endTurn(){
		Thread toStart = null;

		//if (gameBoard.millCheck()){
		if (0 == 1){
			//the turncolor got a new mill
			//turn is not ended
			oldState = gameState;
			gameState = GamePhase.RemovingStone;
			return;
		} else {

			turn++;

			if (turnColor == Color.black) {
				turnColor = Color.white;
			} else {
				turnColor = Color.black;
			}
		}

		printTurnInfo();

		if (turnColor == Color.black) {
			toStart = new Thread(blackPlayer);
		} else {
			toStart = new Thread(whitePlayer);
		}
		toStart.start();
	}

	
	public GamePhase getGamePhase(){
		return gameState;
	}
	
	public Gamestate getState(){
		return gameBoard;
	}
	
	private void printTurnInfo(){
		BoardFactory.printBoard(gameBoard.board);
		System.out.println(" ======================================== Turn " + turn);
		if (turnColor == Color.black)
			System.out.println("Next Player Black");
		else
			System.out.println("Next Player White");
		System.out.println();
	}
}
