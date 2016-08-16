/*
 * Created by Max on 16/08/2016.
 */
import java.util.*;
public final class Mühle {
    public static void main(final String[] args) {
        Board boardObject = new Board();
        Playfield board[][];

        board = boardObject.createBoard();
        Gamestate gamestateObject = new Gamestate(board);
        gamestateObject.createPieces();

        int i = 0;
        while(i < 3) {  // todo: replace with  < 18 later

                Scanner sc = new Scanner(System.in);
                System.out.println("zahl 0-8");
                String tmp = sc.next();
                Integer x = Integer.parseInt(tmp);
                System.out.println("zahl 0-3");
                tmp = sc.next();
                Integer y = Integer.parseInt(tmp);
            board[x][y].addPiece(Gamestate.currentPieces[i]);
            i++;
        }
        boardObject.printBoard(board);
        boardObject.printPieces();
    }

}


