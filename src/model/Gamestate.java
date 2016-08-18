package model;
import java.awt.*;
import java.util.*;

/**
 * Created by Max on 16/08/2016.
 */
public class Gamestate {

    public HashSet<Pieces> currentMillPieces = new HashSet<Pieces>();  //must be emptied after every turn
    /*
    saves which of the 16 possible mills is currently active
     */
    public boolean[] millPositions = new boolean[16];

    /*
    arrays to help calculating if a new mill has been created in this turn
     */
    public int[] newMillCheck = new int[16];
    public int[] oldMillCheck = new int[16];

    public int pieceCountWhite = 0;
    public int pieceCountBlack = 0;
    public Playfield[][] board = new Playfield[8][3];
    public Pieces[] currentPieces = new Pieces[18];
    public int turnsNoMill = 0; // resets if mill happens, if >49 => tie

    public Gamestate(Playfield[][] board) {
        this.board = board;
    }

    /*
    create 18 pieces and updates the piece count for each player
     */
    public void createPieces() {
        for (int i = 0; i < 18; i++) {
            Color color;
            if (i % 2 == 1) {
                color = Color.black;
                pieceCountBlack++;
            }
            else {
                color = Color.white;
                pieceCountWhite++;
            }
            currentPieces[i] = new Pieces(color);
        }
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
        for(int i = 0; i < 16; i++){
            m += count[i];
        }return m;
    }

    public void millToInt(){
        for(int i = 0; i < 16; i++){
            if(millPositions[i]) newMillCheck[i] = 1;
            else newMillCheck[i] = 0;
        }
    }


    public boolean isMill(Playfield playfield1, Playfield playfield2, Playfield playfield3) {
        if (!playfield1.empty && !playfield2.empty && !playfield3.empty) {
            if (playfield1.piece.color == playfield2.piece.color && playfield1.piece.color == playfield3.piece.color) {
                currentMillPieces.add(playfield1.piece);
                currentMillPieces.add(playfield2.piece);
                currentMillPieces.add(playfield3.piece);
                System.out.println("LISTE: " + currentMillPieces);
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
        millToInt();
        int oldMillCheckCount = getMillCheckCount(oldMillCheck);
        int newMillCheckCount = getMillCheckCount(newMillCheck);

        if(newMillCheckCount > oldMillCheckCount){
            oldMillCheck = Arrays.copyOf(newMillCheck, newMillCheck.length);
            return true; //a mill got created by using a piece that wasn't in a mill before
        }
        else if (oldMillCheckCount == newMillCheckCount){ //same amount of mills, but different distribution, meaning a piece left a mill and entered a new mill
            for (int i = 0; i < 16; i++) {
                if (!millPositions[i] && oldMillCheck[i] == 1 || millPositions[i] && oldMillCheck[i] == 0){
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
    public void millCheck(){

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
}


