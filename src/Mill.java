/*
 * Created by Max on 16/08/2016.
 */
import java.util.*;
public final class Mill {
    public static void main(final String[] args) {
        Board boardObject = new Board();
        Playfield board[][];

        board = boardObject.createBoard();
        Gamestate gamestateObject = new Gamestate(board);
        gamestateObject.createPieces();




        board[1][1].addPiece(Gamestate.currentPieces[4]);

        int i = 0;
        while(i < 3) {  // todo: replace with  < 18 later and move to another class

                Scanner sc = new Scanner(System.in);
                System.out.println("zahl 0-8");
                String tmp = sc.next();
                Integer x = Integer.parseInt(tmp);
                System.out.println("zahl 0-3");
                tmp = sc.next();
                Integer y = Integer.parseInt(tmp);
            board[x][y].addPiece(Gamestate.currentPieces[i]);
            Gamestate.currentPieces[i].number = i;
            i++;
        }



        boardObject.printPieces();
        board[1][1].move(board[1][2]);
        board[1][1].move(board[0][1]);
        board[3][1].move(board[3][2]);
        board[4][0].move(board[4][2]);
        board[4][2].move(board[4][1]);

        System.out.println("---- . ----");

        boardObject.printPieces();

    }

}


