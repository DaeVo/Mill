package model;

/**
 * Created by Max on 16/08/2016.
 * create or print a board.
 */
public class BoardFactory implements java.io.Serializable{
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
        System.out.print(getBoardString(board));
    }

    public static String getBoardString(Playfield[][] board) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        String line1 = "%s - - %s - - %s %n";
        String line2 = "- %s - %s - %s - %n";
        String line3 = "- - %s %s %s - - %n";
        String line4 = "%s %s %s - %s %s %s %n";
        sb.append(String.format(line1, board[0][0].shortString(), board[1][0].shortString(), board[2][0].shortString()));
        sb.append(String.format(line2, board[0][1].shortString(), board[1][1].shortString(), board[2][1].shortString()));
        sb.append(String.format(line3, board[0][2].shortString(), board[1][2].shortString(), board[2][2].shortString()));
        sb.append(String.format(line4, board[7][0].shortString(), board[7][1].shortString(), board[7][2].shortString(), board[3][2].shortString(), board[3][1].shortString(), board[3][0].shortString()));
        sb.append(String.format(line3, board[6][2].shortString(), board[5][2].shortString(), board[4][2].shortString()));
        sb.append(String.format(line2, board[6][1].shortString(), board[5][1].shortString(), board[4][1].shortString()));
        sb.append(String.format(line1, board[6][0].shortString(), board[5][0].shortString(), board[4][0].shortString()));
        return sb.toString();
    }

    public static void printPieces(Gamestate gs){
        for (Pieces p : gs.currentPieces){
            System.out.println(p.field);
        }
    }
}
