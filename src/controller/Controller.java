package controller;

import model.BoardFactory;
import model.Gamestate;
import model.Pieces;
import model.Playfield;
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
		Pieces piece = new Pieces(turnColor);
		gameBoard.currentPieces.add(piece);
		gameBoard.board[p.x][p.y].addPiece(piece);

		if (gameBoard.getPieceCount() == 18)
			gameState = GamePhase.Moving;
		endTurn();
	}
	
	
	public void move(Point src, Point dst){
		gameBoard.board[src.x][src.y].move(gameBoard.board[dst.x][dst.y]);

		endTurn();
	}

	public void moveFreely(Point src, Point dst){
		gameBoard.board[src.x][src.y].moveFreely(gameBoard.board[dst.x][dst.y]);

		endTurn();
	}

	public void removeStone(Point p){
		Pieces oldPiece = gameBoard.board[p.x][p.y].piece;
		gameBoard.currentPieces.remove(oldPiece);
		gameBoard.board[p.x][p.y].conquerField(new Playfield(false));

		gameState = oldState;
		endTurn();
	}
	
	
	private void endTurn(){
		Thread toStart = null;

		if (gameBoard.millCheck()){
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





		if (turnColor == Color.black) {
			toStart = new Thread(blackPlayer);
		} else {
			toStart = new Thread(whitePlayer);
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
		BoardFactory.printBoard(gameBoard.board);
		System.out.println();
	}
}
