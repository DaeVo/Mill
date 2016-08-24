/*
 * Created by Max on 16/08/2016.
 */

import controller.Controller;
import controller.GamePhase;
import model.BoardFactory;
import model.Gamestate;
import model.Playfield;
import view.IPlayer;
import view.ai.SmartAi;
import view.ai.StupidAi;
import view.human.ConsoleView;

public final class Mill {
    public static void main(final String[] args) {
        Playfield board[][] = BoardFactory.createBoard();
            Gamestate gamestate = new Gamestate(board);
            Controller c = new Controller(gamestate);

            try {
                IPlayer p1 = new SmartAi();
                IPlayer p2 = new StupidAi();

                c.startGame(p1, p2);

                while (c.getGamePhase() != GamePhase.Exit) {
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
        }
    }
}


