package controller;

import model.*;
import view.IPlayer;
import view.ai.DummyPlayer;
import view.ai.Move;

import java.awt.*;
import java.util.Observable;
import java.util.SimpleTimeZone;

import static model.GamePhase.Exit;
import static model.GamePhase.Placing;

public class Controller extends Observable implements java.io.Serializable {
    private IPlayer blackPlayer;
    private IPlayer whitePlayer;
    private Thread oldThread;
    private int sleepTime = 0;
    private boolean simulation = false;

    private Gamestate gameBoard;


    public Controller() {
        gameBoard = new Gamestate();
    }

    public Controller(Gamestate gs) {
        gameBoard = gs;
        oldThread = null;
        simulation = true;
    }

    public void startGame(IPlayer whiteP, IPlayer blackP) {
        //notfiy first player
        setWhitePlayer(whiteP);
        setBlackPlayer(blackP);

        //reset everything
        gameBoard = new Gamestate();
        gameBoard.turn = 1;
        gameBoard.toPlace = 2 * 9;
        gameBoard.lastMillTurn = 0;
        gameBoard.turnColor = Color.white;
        gameBoard.gamePhase = Placing;
        printTurnInfo();

        oldThread = new Thread(whitePlayer);
        oldThread.start();

        setChanged();
        notifyObservers();
    }

    public void setBlackPlayer(IPlayer bp) {
        blackPlayer = bp;
    }

    public void setWhitePlayer(IPlayer wp) {
        whitePlayer = wp;
    }

    public IPlayer getBlackPlayer() {
        return blackPlayer;
    }

    public IPlayer getWhitePlayer() {
        return whitePlayer;
    }

    public void setSleep(int s) {
        sleepTime = s;
    }




    public boolean place(Point p) {
        Piece piece = new Piece(gameBoard.turnColor);
        if (!gameBoard.board[p.x][p.y].empty) {
            System.out.println("please enter a field that is not occupied by another piece, yet.");
            return false;
        }
        gameBoard.currentPieces.add(piece);
        gameBoard.board[p.x][p.y].addPiece(piece);
        gameBoard.board[p.x][p.y].toString();

        gameBoard.toPlace--;
        gameBoard.currentMove = new Move(null, p);
        if (gameBoard.toPlace == 0)
            gameBoard.gamePhase = GamePhase.Moving;
        endTurn();
        return true;
    }


    public boolean move(Point src, Point dst) {
        if (!isValidMove(src, dst))
            return false;

        boolean success = gameBoard.board[src.x][src.y].move(gameBoard.board[dst.x][dst.y]);
        if (success) {
            gameBoard.currentMove = new Move(src, dst);
            endTurn();
        }
        return success;
    }

    public boolean moveFreely(Point src, Point dst) {
        if (!isValidMove(src, dst))
            return false;

        boolean success = gameBoard.board[src.x][src.y].moveFreely(gameBoard.board[dst.x][dst.y]);
        if (success) {
            gameBoard.currentMove = new Move(src, dst);
            endTurn();
        }
        return success;
    }

    private boolean isValidMove(Point src, Point dst){
        Playfield srcField = gameBoard.board[src.x][src.y];
        Playfield dstField = gameBoard.board[dst.x][dst.y];

        if (!srcField.piece.color.equals(getTurnColor()))
            return false;
        if (!dstField.empty)
            return false;
        return true;
    }

    public boolean removeStone(Point p) {
        Piece oldPiece = gameBoard.board[p.x][p.y].piece;

        if (oldPiece == null || oldPiece.color == gameBoard.turnColor)
            return false;
        if (gameBoard.isInMill(oldPiece))
            return false;

        gameBoard.currentPieces.remove(oldPiece);
        gameBoard.board[p.x][p.y].conquerField(new Playfield(false));

        gameBoard.gamePhase = gameBoard.oldState;
        gameBoard.currentMove = new Move (p, null);
        endTurn();
        return true;
    }


    private void endTurn() {
        String infoText = null;
        setChanged();

        //Wind/Draw
          if (!gameBoard.isLegalMoveAvailable(Color.white)) {
            gameBoard.gameEnd = GameEnd.BlackWon;
        }
        else if(!gameBoard.isLegalMoveAvailable(Color.black)){
            gameBoard.gameEnd = GameEnd.WhiteWon;
        }
        if (gameBoard.gamePhase != Placing && (Color.black.equals(winCheck()) || Color.white.equals(winCheck()))) {
            infoText = "Game ends in a victory for " + Utils.getColorName(winCheck()) + "!";

            if (Color.black.equals(winCheck()))
                gameBoard.gameEnd = GameEnd.BlackWon;
            else
                gameBoard.gameEnd = GameEnd.WhiteWon;
        } else if (drawCheck()) {
            infoText = "Game ends in a draw!";
            gameBoard.gameEnd = GameEnd.Draw;

        }

        if (infoText != null){
            gameBoard.gamePhase = Exit;
            if (!simulation) System.out.println(infoText);
            if (!simulation) System.out.printf("Remaining pieces: Black %d, White %d %n", gameBoard.getPieceCount(Color.black), gameBoard.getPieceCount(Color.white));
            notifyObservers(infoText);
            return;
        }

        //Mill/End Turn
        if (gameBoard.millCheckCompare()) {
            //the turncolor got a new mill
            //turn is not ended
            gameBoard.oldState = gameBoard.gamePhase;
            gameBoard.gamePhase = GamePhase.RemovingStone;
            gameBoard.lastMillTurn = gameBoard.turn;
        } else {

            gameBoard.turn++;
            if (gameBoard.turnColor.equals(Color.black)) {
                gameBoard.turnColor = Color.white;
            } else {
                gameBoard.turnColor = Color.black;
            }
            gameBoard.turnHistory.add(BoardFactory.getBoardString(gameBoard.board));
        }

        if (simulation)
            return;

        printTurnInfo();

        new Thread(() -> {
            try {
                //Wait for last turn to finish
                oldThread.join();
                //Set actual turn as last turn
                oldThread = Thread.currentThread();

                //Sleep
                Thread.sleep(sleepTime);

                //Start actual turn
                if (gameBoard.turnColor.equals(Color.black)) {
                    blackPlayer.run();
                } else {
                    whitePlayer.run();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();

        notifyObservers();
    }




    private Color winCheck() {
        if (gameBoard.getPieceCount(Color.black) < 3) return Color.white;
        else if (gameBoard.getPieceCount(Color.white) < 3) return Color.black;
        else return null;
    }

    private boolean drawCheck() {
        //Abbort rule 50 round no mill
        if (gameBoard.turn - gameBoard.lastMillTurn > 50) {
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
        return gameBoard.gamePhase;
    }

    public Gamestate getState() {
        return gameBoard;
    }

    public Color getTurnColor() {
        return gameBoard.turnColor;
    }

    public IPlayer getTurnPlayer() {
        if (gameBoard.turnColor.equals(Color.white))
            return getWhitePlayer();
        return getBlackPlayer();
    }

    private void printTurnInfo() {
        BoardFactory.printBoard(gameBoard.board);
        System.out.println(" ======================================== Turn " + gameBoard.turn);
        System.out.println("Next Player " + Utils.getColorName(gameBoard.turnColor));
        System.out.println();
    }

    public Controller deepCopy() {
        try {
            Gamestate gsCopy = null;
            if (gameBoard != null)
                gsCopy = gameBoard.deepCopy();

            Controller cCopy = new Controller(gsCopy);

            //these fields are copied, oldThread remains null
            cCopy.setWhitePlayer(new DummyPlayer());
            cCopy.setBlackPlayer(new DummyPlayer());
            cCopy.setSleep(sleepTime);

            return cCopy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


