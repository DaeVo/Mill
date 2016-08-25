package controller;

import model.*;
import view.IPlayer;

import java.awt.*;
import java.io.*;

import static controller.GamePhase.Exit;
import static controller.GamePhase.Placing;

public class Controller implements java.io.Serializable {
    private IPlayer blackPlayer;
    private IPlayer whitePlayer;
    private Thread oldThread;
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
        setBlackPlayer(bp);
        setWhitePlayer(wp);

        turn = 1;
        turnColor = Color.black;
        gamePhase = Placing;
        printTurnInfo();

        oldThread = new Thread(blackPlayer);
        oldThread.start();
    }

    public void setBlackPlayer(IPlayer bp) {
        blackPlayer = bp;
        blackPlayer.create(this, Color.black);
    }

    public void setWhitePlayer(IPlayer wp) {
        whitePlayer = wp;
        whitePlayer.create(this, Color.white);
    }

    public IPlayer getBlackPlayer() {
        return blackPlayer;
    }

    public IPlayer getWhitePlayer() {
        return whitePlayer;
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

        if (gamePhase != Placing && (gameBoard.getPieceCount(Color.black) < 3 || gameBoard.getPieceCount(Color.white) < 3)) {
            System.out.println("Game ends with a victory!");
            System.out.printf("Remaining pieces: Black %d, White %d %n", gameBoard.getPieceCount(Color.black), gameBoard.getPieceCount(Color.white));
            gamePhase = Exit;
            return;
        }
        if (drawCheck()) {
            System.out.println("Game ends with a draw!");
            System.out.printf("Remaining pieces: Black %d, White %d %n", gameBoard.getPieceCount(Color.black), gameBoard.getPieceCount(Color.white));
            gamePhase = Exit;
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


        new Thread(() -> {
            try {
                //Wait for last turn to finish
                oldThread.join();
                //Set actual turn as last turn
                oldThread = Thread.currentThread();

                //Start actual turn
                if (turnColor == Color.black) {
                    blackPlayer.run();
                } else {
                    whitePlayer.run();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();



    }


    private boolean drawCheck() {
        //Abbort rule 50 round no mill
        if (turn - lastMillTurn > 50) {
            System.out.println("50 turns without a Mill - game ends in a draw");
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
            System.out.println("the exact same situation happened 3times.");
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
        System.out.println("Next Player " + Utils.getColorName(turnColor));
        System.out.println();
    }

    public Controller deepCopy() {
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


