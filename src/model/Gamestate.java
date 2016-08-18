package model;
import java.awt.*;

/**
 * Created by Max on 16/08/2016.
 */
public class Gamestate {

    public boolean[] millPositions = new boolean[16];
    public Playfield[][] board = new Playfield[8][3];

    public Gamestate(Playfield[][] board) {
        this.board = board;
    }

    public int pieceCountWhite = 9;
    public int pieceCountBlack = 9;
    public int turnsNoMill = 0; // resets if mill happens, if >49 => tie

    public Pieces[] currentPieces = new Pieces[18];

    public void createPieces() {  //initializes 18 starting model.Pieces
        for (int i = 0; i < 18; i++) {
            Color color;
            if (i % 2 == 1)
                color = Color.black;
            else
                color = Color.white;
            currentPieces[i] = new Pieces(color);
        }
    }




    private String millType;
    private int millCount = 0;
    private int currentMillCount = 0;




    public boolean isMill(Playfield playfield1, Playfield playfield2, Playfield playfield3) {
        if (!playfield1.empty && !playfield2.empty && !playfield3.empty) {
            if (playfield1.piece.color == playfield2.piece.color && playfield1.piece.color == playfield3.piece.color)
                return true;
        }
        return false;
    }


    /*
    easier to read and understand than dyamic statements to check for mills
     */
    public void millCheck(){
        //top and left side mills
        for(int i = 0; i < 16; i++) {

        }
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


