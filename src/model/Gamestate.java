package model;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Max on 16/08/2016.
 */
public class Gamestate implements java.io.Serializable {

    public HashSet<Pieces> currentMillPieces = new HashSet<Pieces>();  //must be updated after every turn
    /*
    stores which of the 16 possible mills is currently active
     */
    public boolean[] millPositions = new boolean[16];

    /*
    arrays to help calculating if a new mill has been created in this turn
     */
    public int[] newMillCheck = new int[16];
    public int[] oldMillCheck = new int[16];

    public Playfield[][] board = new Playfield[8][3];
    public List<Pieces> currentPieces = new LinkedList<>();
    public Map<Playfield, List<Playfield>> playfieldNeighbors = new HashMap<>();
    public Map<Playfield, List<Point>> legalMoves = new HashMap<>();
    public List<String> turnHistory = new LinkedList<>();

    public Gamestate(Playfield[][] board) {
        this.board = board;
        millPositionsInitializer();
        createNeighbors();
    }

    /*
    initializes the array so no mills occur before the game starts
     */
    public void millPositionsInitializer() {  // call at gamestart
        for (int i = 0; i < 16; i++) {
            newMillCheck[i] = 0;
            oldMillCheck[i] = 0;
            millPositions[i] = false;
        }
    }

    public int getMillCheckCount(int[] count) {
        int m = 0;
        for (int i = 0; i < 16; i++) {
            m += count[i];
        }
        return m;
    }

    public void millToInt() {
        for (int i = 0; i < 16; i++) {
            if (millPositions[i]) newMillCheck[i] = 1;
            else newMillCheck[i] = 0;
        }
    }


    public boolean isMill(Playfield playfield1, Playfield playfield2, Playfield playfield3) {
        if (!playfield1.empty && !playfield2.empty && !playfield3.empty) {
            if (playfield1.piece.color == playfield2.piece.color && playfield1.piece.color == playfield3.piece.color) {
                currentMillPieces.add(playfield1.piece);
                currentMillPieces.add(playfield2.piece);
                currentMillPieces.add(playfield3.piece);
                System.out.println("MillList:: " + currentMillPieces);
                return true;
            }
        }
        return false;
    }

    public void clearMillList() {
        currentMillPieces.clear();
    }

    /*
    compares the old mill situation to the new one and figures out if a new mill has been created or not.
     */
    public boolean millCheckCompare() {
        clearMillList();
        millCheck();
        millToInt();
        int oldMillCheckCount = getMillCheckCount(oldMillCheck);
        int newMillCheckCount = getMillCheckCount(newMillCheck);

        if (newMillCheckCount > oldMillCheckCount) {
            oldMillCheck = Arrays.copyOf(newMillCheck, newMillCheck.length);
            return true; //a mill got created by using a piece that wasn't in a mill before
        } else if (oldMillCheckCount == newMillCheckCount) { //same amount of mills, but different distribution, meaning a piece left a mill and entered a new mill
            for (int i = 0; i < 16; i++) {
                if (!millPositions[i] && oldMillCheck[i] == 1 || millPositions[i] && oldMillCheck[i] == 0) {
                    oldMillCheck = Arrays.copyOf(newMillCheck, newMillCheck.length);
                    return true;
                }
            }
        }
        oldMillCheck = Arrays.copyOf(newMillCheck, newMillCheck.length);
        return false;
    }

    /*
    easier to read and understand than dyamic statements to check for mills
    sets a booleana to 1 if a mill position happens.
     */
    public void millCheck() {

        //top and left side mills
        millPositions[0] = isMill(board[0][0], board[1][0], board[2][0]);
        millPositions[1] = isMill(board[0][0], board[6][0], board[7][0]);
        millPositions[2] = isMill(board[0][1], board[1][1], board[2][1]);
        millPositions[3] = isMill(board[0][1], board[6][1], board[7][1]);
        millPositions[4] = isMill(board[0][2], board[1][2], board[2][2]);
        millPositions[5] = isMill(board[0][2], board[6][2], board[7][2]);

        //right and bottom side mills
        millPositions[6] = isMill(board[4][0], board[3][0], board[2][0]);
        millPositions[7] = isMill(board[4][0], board[5][0], board[6][0]);
        millPositions[8] = isMill(board[4][1], board[3][1], board[2][1]);
        millPositions[9] = isMill(board[4][1], board[5][1], board[6][1]);
        millPositions[10] = isMill(board[4][2], board[3][2], board[2][2]);
        millPositions[11] = isMill(board[4][2], board[5][2], board[6][2]);

        //"connections" from outer to inner circles
        millPositions[12] = isMill(board[7][0], board[7][1], board[7][2]);
        millPositions[13] = isMill(board[5][0], board[5][1], board[5][2]);
        millPositions[14] = isMill(board[3][0], board[3][1], board[3][2]);
        millPositions[15] = isMill(board[1][0], board[1][1], board[1][2]);
    }


    public Point findFreePlayfield() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].empty)
                    return new Point(i, j);
            }
        }
        return null;
    }
    
    private void createNeighbors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                Playfield base = board[i][j];

                for (int i2 = 0; i2 < 8; i2++) {
                    for (int j2 = 0; j2 < 3; j2++) {
                        Playfield toCompare = board[i2][j2];
                        if (base.isNeighbour(toCompare)) {
                            if (!playfieldNeighbors.containsKey(base))
                                playfieldNeighbors.put(base, new LinkedList<>());

                            playfieldNeighbors.get(base).add(toCompare);
                        }
                    }
                }
            }
        }
    }
    public List<Playfield> getNeighbours(Playfield field) {
        return playfieldNeighbors.get(field);
    }

    public int getPieceCount() {
        return getPieceCount(Color.black) + getPieceCount(Color.white);
    }

    public int getPieceCount(Color color) {
        int count = 0;
        for (Pieces p : currentPieces) {
            if (p.field != null && p.color == color)
                count++;
        }
        return count;
    }

    /*
    updates the hashmap to store all legal moves for each playfield on the board.
     */
    public void createLegalMoves() { // clear HashMap every turn.
        for (Pieces p : currentPieces) {
            LinkedList<Point> legalMoveList = new LinkedList<Point>();
             Playfield tmpField = p.field;
            for (Playfield neighbourfield : getNeighbours(tmpField)) {
                if(neighbourfield.empty) {
                    Point tmpPoint = new Point(neighbourfield.x, neighbourfield.y);
                    legalMoveList.add(tmpPoint);
                }
            }
            if(!legalMoves.containsKey(p)) legalMoves.put(p.field, new LinkedList<>());
            legalMoves.put(p.field, legalMoveList);
        }
    }





    public void freeMovementLegalMoves (){ // < than 4 pieces.

    }
}