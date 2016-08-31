package model;

import controller.Controller;

import java.awt.*;

/**
 * Created by Max on 24.08.2016.
 */
public class Utils {
    public static String getColorName(Color c){
        if (c.equals(Color.black))
            return "Black";
        else if (c.equals(Color.white))
            return "White";
        return "";
    }

    public static Color getOppositeColor(Color color){
        if (color.equals(Color.black))
            return Color.white;
        return Color.black;
    }

    /*
    helping method to check if a player may freely move onto any empty field.
     */
    public static boolean freeMoveAllowed(Controller millController, Color c){
        return millController.getState().getPieceCount(c) < 4 && millController.getGamePhase() == GamePhase.Moving;
    }
}
