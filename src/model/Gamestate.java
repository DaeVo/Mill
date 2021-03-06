package model;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;



/**
 * Created by Max on 16/08/2016.
 */
public class Gamestate implements java.io.Serializable {
    public HashSet<Piece> currentMillPieces = new HashSet<Piece>();  //must be updated after every turn
    public boolean[] millPositions = new boolean[16];

    public int[] newMillCheck = new int[16];
    public int[] oldMillCheck = new int[16];

    public List<Point> legalPlacing = new LinkedList<Point>();
    public Playfield[][] board = new Playfield[8][3];
    public List<Piece> currentPieces = new LinkedList<>();
    public Map<Playfield, List<Playfield>> playfieldNeighbors = new HashMap<>();
    public Map<Playfield, List<Point>> legalMoves = new HashMap<>();

    public int turn;
    public int toPlace;
    public int lastMillTurn;
    public Color turnColor;
    public GamePhase gamePhase;
    public GamePhase oldState;
    public GameEnd gameEnd;
    public List<Integer> turnHistory = new LinkedList<>();

    public Gamestate() {
        board = BoardFactory.createBoard();
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

    /*
    returns how many closed mills are currently on the field
     */
    public int getMillCheckCount(int[] count) {
        int m = 0;
        for (int i = 0; i < 16; i++) {
            m += count[i];
        }
        return m;
    }
    /*
    helping method to check if currently closed mills are more closed mills than in the move before (to trigger a removeStone)
     */
    public void millToInt() {
        for (int i = 0; i < 16; i++) {
            if (millPositions[i]) newMillCheck[i] = 1;
            else newMillCheck[i] = 0;
        }
    }

    /*
    takes 3 arguments of playfield which are in legal mill positions and checks if they re all the same color.
     */
    public boolean isMill(Playfield playfield1, Playfield playfield2, Playfield playfield3) {
        if (!playfield1.empty && !playfield2.empty && !playfield3.empty) {
            if (playfield1.piece.color.equals(playfield2.piece.color) && playfield1.piece.color.equals(playfield3.piece.color)) {
                currentMillPieces.add(playfield1.piece);
                currentMillPieces.add(playfield2.piece);
                currentMillPieces.add(playfield3.piece);
                return true;
            }
        }
        return false;
    }

    /*
    returns all pieces currently in a mill (must not be removed, unless the player has only 3 pieces left.
     */
    public boolean isInMill(Piece piece){
        return (currentMillPieces.contains(piece));
    }

    /*
    returns the number of pieces in a mill.
     */
    public byte getMillPieceCount(Color color){
        byte count = 0;
        for (Piece p : currentMillPieces){
            if (p.color.equals(color))
                count++;
        }
        return count;
    }

    /*
    returns the number of pieces from one player
     */
    public byte getPieceCountColor(Color color) {
        byte count = 0;
            for (Piece p : currentPieces) {
                if(p.color.equals(color))
                    count++;
            }
        return count;
    }
    /*
    clears the list of pieces currently in a mill.
     */
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
    easier to read and understand and change than dyamic statements to check for mills
    stores which of the possible 16 mill positions are currently active.
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

    /*
    returns the first empty playfield (was useful for the first super simple ai version to test things, might come in handy again one day, who knows.
     */
    public Point findFreePlayfield() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].empty)
                    return new Point(i, j);
            }
        }
        return null;
    }

    /*
    creates a map of all fields that are neighbourfields.
     */
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
        for (Piece p : currentPieces) {
            if (p.field != null && p.color.equals(color))
                count++;
        }
        return count;
    }

    /*
    updates the hashmap to store all legal moves for each playfield on the board.
     */
    public void updateLegalMoves() { // clear HashMap every turn.
        for (Piece p : currentPieces) {
            LinkedList<Point> legalMoveList = new LinkedList<Point>();
            Playfield tmpField = p.field;
            for (Playfield neighbourfield : getNeighbours(tmpField)) {
                if (neighbourfield.empty) {
                    Point tmpPoint = new Point(neighbourfield.x, neighbourfield.y);
                    legalMoveList.add(tmpPoint);
                }
            }
            legalMoves.put(p.field, legalMoveList);
        }
    }

    /*
    creates a list with legal moves.
     */
    public List<Move> getLegalMoveList(Color turnColor){
        List<Move> resultList = new LinkedList<>();
        for (Playfield field : legalMoves.keySet()){
            if (field.empty || !field.piece.color.equals(turnColor))
                continue;

            List<Point> tmpMoveList = legalMoves.get(field);
            for (Point toPoint : tmpMoveList){
                resultList.add(new Move(field.getPoint(), toPoint));
            }
        }
        return resultList;
    }

    /*
    method to update all legal moves if there are less than 4 pieces for the player that is moving
    and store them in a HashMap
     */
    public void updateFreeMovementLegalMoves() { //
        for (Piece p : currentPieces) {
            LinkedList<Point> legalMoveList = new LinkedList<>();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 3; j++) {
                    Playfield tmpField = board[i][j];
                    if (tmpField.empty) {
                        Point tmp = new Point(tmpField.x, tmpField.y);
                        legalMoveList.add(tmp);
                    }
                }
            }
            legalMoves.put(p.field, legalMoveList);
        }
    }

    /*
    checks if there is a legal move available to perform.
     */
    public boolean isLegalMoveAvailable (Color color) {
        updateLegalMoves();
        for (Piece p : currentPieces) {
            if (p.color.equals(color)) {
                if (legalMoves.get(p.field).size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }
    /*
    updates a list that contains legal fields during the placing phase
     */
    public void updateLegalPlacing() {
        Playfield tmpField;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 3; j++) {
                tmpField = board[i][j];
                if (tmpField.empty) {
                    Point tmpPoint = new Point(tmpField.x, tmpField.y);
                    legalPlacing.add(tmpPoint);
                }
            }
        }
    }
    /*
    deep copy for the simulations
     */
    public Gamestate deepCopy() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Gamestate) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return BoardFactory.getBoardString(board);
    }
}