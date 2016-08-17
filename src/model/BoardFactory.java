package model;

/**
 * Created by Max on 16/08/2016.
 * create or print a board.
 */
public class BoardFactory {
    public BoardFactory(){}
    /*
    creating the model.BoardFactory as 8x3 fields
     */
    public static Playfield[][] createBoard(){
        Playfield[][] board = new Playfield[8][3];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = new Playfield(true);
                board[i][j].x = i;
                board[i][j].y = j;
            }
        }return board;
    }

    public static void printBoard(Playfield[][] board) {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(board[i][j]);
            }
        }
    }
    public static void printPieces(Gamestate gs){
        for (int j = 0; j < 18; j++) {
           if(gs.currentPieces[j].field != null)
                System.out.println(gs.currentPieces[j].field);
        }
    }
}
