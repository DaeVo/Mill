package model;

import controller.Controller;

import java.awt.*;

/**
 * Created by Henry on 24.08.2016.
 */
public class Utils {
    public static String getColorName(Color c){
        if (c.equals(Color.black))
            return "Black";
        else if (c.equals( Color.white))
            return "White";
        return "";
    }

    public static boolean freeMoveAllowed(Controller millController, Color c){
        return millController.getState().getPieceCount(c) < 4 && millController.getGamePhase() == GamePhase.Moving;
    }
}
