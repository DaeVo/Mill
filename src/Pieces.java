/**
 * Created by Max on 16/08/2016.
 */
public class Pieces {

    public Playfield field;
    public Color color;
    int number;
    public Pieces(Color color){
        this.color = color;
    }

    public void addPlayfield(Playfield field){
        this.field = field;
    }

    enum Color {
        white, black
    }
}
