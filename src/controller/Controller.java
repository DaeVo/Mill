package controller;

import model.BoardFactory;
import model.Gamestate;
import model.Pieces;
import model.Playfield;
import view.IPlayer;

import java.awt.*;
import java.io.*;

import static controller.GamePhase.Placing;

public class Controller implements java.io.Serializable {
    private IPlayer blackPlayer;
    private IPlayer whitePlayer;
    private Gamestate gameBoard;

    private int turn;
    private int toPlace = 2 * 9;
    private int lastMillTurn = 0;
    private Color turnColor;
    private GamePhase gamePhase;
    private GamePhase oldState;


    public Controller(Gamestate gs) {
        gameBoard = gs;
    }

    public void startGame(IPlayer bp, IPlayer wp) {
        //notfiy first playwer
        blackPlayer = bp;
        whitePlayer = wp;
        blackPlayer.create(this, Color.black);
        whitePlayer.create(this, Color.white);

        turn = 1;
        turnColor = Color.black;
        gamePhase = Placing;
        printTurnInfo();

        new Thread(blackPlayer).start();
    }

    public boolean place(Point p) {

        Pieces piece = new Pieces(turnColor);
        if (!gameBoard.board[p.x][p.y].empty) {
            System.out.println("please enter a field that is not occupied by another piece, yet.");
            return false;
        }
        gameBoard.currentPieces.add(piece);
        gameBoard.board[p.x][p.y].addPiece(piece);

        toPlace--;
        if (toPlace == 0)
            gamePhase = GamePhase.Moving;
        endTurn();
        return true;
    }


    public boolean move(Point src, Point dst) {
        boolean success = gameBoard.board[src.x][src.y].move(gameBoard.board[dst.x][dst.y]);

        if (success)
            endTurn();
        return success;
    }

    public boolean moveFreely(Point src, Point dst) {
        boolean success = gameBoard.board[src.x][src.y].moveFreely(gameBoard.board[dst.x][dst.y]);

        if (success)
            endTurn();
        return success;
    }

    public boolean removeStone(Point p) {
        Pieces oldPiece = gameBoard.board[p.x][p.y].piece;

        if (oldPiece == null || oldPiece.color == turnColor)
            return false;
        if (gameBoard.isInMill(oldPiece))
            return false;

        gameBoard.currentPieces.remove(oldPiece);
        gameBoard.board[p.x][p.y].conquerField(new Playfield(false));

        gamePhase = oldState;
        endTurn();
        return true;
    }


    private void endTurn() {
        Thread toStart = null;

        if (gamePhase != Placing && (gameBoard.getPieceCount(Color.black) < 3 || gameBoard.getPieceCount(Color.white) < 3)) {
            System.out.println("Game ends with a victory!");
            System.out.printf("Remaining pices: Black %d, White %d", gameBoard.getPieceCount(Color.black), gameBoard.getPieceCount(Color.white));
            return;
        }
        if (drawCheck()) {
            System.out.println("Game ends with a draw!");
            System.out.printf("Remaining pices: Black %d, White %d", gameBoard.getPieceCount(Color.black), gameBoard.getPieceCount(Color.white));
            return;
        }


        if (gameBoard.millCheckCompare()) {
            //the turncolor got a new mill
            //turn is not ended
            oldState = gamePhase;
            gamePhase = GamePhase.RemovingStone;
            lastMillTurn = turn;
        } else {

            turn++;
            if (turnColor == Color.black) {
                turnColor = Color.white;
            } else {
                turnColor = Color.black;
            }
            gameBoard.turnHistory.add(BoardFactory.getBoardString(gameBoard.board));
        }

        printTurnInfo();

        if (turnColor == Color.black) {
            toStart = new Thread(blackPlayer);
        } else {
            toStart = new Thread(whitePlayer);
        }


        toStart.start();
    }

    private boolean drawCheck() {
        //Abbort rule 50 round no mill
        if (turn - lastMillTurn > 50) {
            return true;
        }

        //3 times the same setup
        String actualBoard = BoardFactory.getBoardString(gameBoard.board);
        int count = 0;
        for (String board : gameBoard.turnHistory) {
            if (actualBoard.equals(board))
                count++;
        }
        if (count >= 2) {
            return true;
        }

        return false;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public Gamestate getState() {
        return gameBoard;
    }

    private void printTurnInfo() {
        BoardFactory.printBoard(gameBoard.board);
        System.out.println(" ======================================== Turn " + turn);
        if (turnColor == Color.black)
            System.out.println("Next Player Black");
        else
            System.out.println("Next Player White");
        System.out.println();
    }

    public Controller deepCopy () {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Controller) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}


