package model;

        import java.awt.*;

/**
 * Created by Max on 16/08/2016.
 */
public class Piece implements java.io.Serializable {
    public Playfield field;
    public Color color;
    public Piece(Color color){
        this.color = color;
    }

    /*
    adds a playfield to the piece, so the piece knows where it's standing.
     */
    public void addPlayfield(Playfield field){
        this.field = field;
    }

    @Override
    public String toString() {
        return "" + this.field;
    }
}
