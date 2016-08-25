package model;

import java.awt.*;

/**
 * Created by Henry on 24.08.2016.
 */
public class Utils {
    public static String getColorName(Color c){
        if (c == Color.black)
            return "Black";
        else if (c == Color.white)
            return "White";
        return "";
    }
}
