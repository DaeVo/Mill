package model;

import java.awt.*;

/**
 * Created by Max on 16/08/2016.
 */
public class Pieces {

    public Playfield field;
    public Color color;
    public Integer millCount = null;
    public int number;
    public Pieces(Color color){
        this.color = color;
    }

    public void addPlayfield(Playfield field){
        this.field = field;
    }
}
