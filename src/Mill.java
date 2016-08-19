/*
 * Created by Max on 16/08/2016.
 */

import controller.Controller;
import model.BoardFactory;
import model.Gamestate;
import model.Playfield;
import view.IPlayer;
import view.ai.StupidAi;
import view.human.ConsoleView;

public final class Mill {
    public static void main(final String[] args) {
        Playfield board[][] = BoardFactory.createBoard();
        Gamestate gamestate = new Gamestate(board);
        Controller c = new Controller(gamestate);

        try {
            IPlayer p1 = new ConsoleView();
            IPlayer p2 = new StupidAi();

            c.startGame(p1, p2);

            while (true) {
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        board[1][1].addPiece(gamestate.currentPieces[4]);

        int i = 0;
        while(i < 3) {  // todo: replace with  < 18 later and move to another class
                Scanner sc = new Scanner(System.in);
                System.out.println("zahl 0-8");
                String tmp = sc.next();
                Integer x = Integer.parseInt(tmp);
                System.out.println("zahl 0-3");
                tmp = sc.next();
                Integer y = Integer.parseInt(tmp);
            board[x][y].addPiece(gamestate.currentPieces[i]);
            gamestate.currentPieces[i].number = i;
            i++;
        }

        BoardFactory.printPieces(gamestate);
        board[1][1].move(board[1][2]);
        board[1][1].move(board[0][1]);
        board[3][1].move(board[3][2]);
        board[4][0].move(board[4][2]);
        board[4][2].move(board[4][1]);

        System.out.println("---- . ----");
        BoardFactory.printPieces(gamestate);
        */
    }
}


