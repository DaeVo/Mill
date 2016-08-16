/**
 * Created by Max on 16/08/2016.
 * create or print a board.
 */
public class Board {
    public Board(){}
    /*
    creating the Board as 8x3 fields
     */
    public Playfield[][] createBoard(){
        Playfield[][] board = new Playfield[8][3];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = new Playfield(true);
                board[i][j].x = i;
                board[i][j].y = j;

            }
        }return board;
    }

    public void printBoard(Playfield[][] board) {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(board[i][j]);
            }
        }
    }
    public void printPieces(){
        for (int j = 0; j < 18; j++) {
           if(Gamestate.currentPieces[j].field != null)
                System.out.println(Gamestate.currentPieces[j].field);
        }
    }
}
