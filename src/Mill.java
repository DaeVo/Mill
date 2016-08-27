/*
 * Created by Max on 16/08/2016.
 */

import controller.Controller;
import model.GamePhase;
import view.IPlayer;
import view.ai.SmartAi;
import view.ai.StupidAi;
import view.gui.GUI;

import java.awt.*;

public final class Mill {
    public static final boolean DEBUG = true;

    public static void main(final String[] args) {
        Controller c = new Controller();


        new GUI(c);

        try {
            IPlayer whiteP = new SmartAi();
            whiteP.create(c, Color.white);

            IPlayer blackP = new StupidAi();
            blackP.create(c, Color.black);

            c.setSleep(50);
            c.startGame(whiteP, blackP);


            while (c.getGamePhase() != GamePhase.Exit) {
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


