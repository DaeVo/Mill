/**
 * Created by Max on 16/08/2016.
 */
public class Playfield {

    public boolean empty;
    public Pieces piece;
    /*
    so the field knows it's own array position
     */
    public int x;
    public int y;

    public Playfield(boolean empty) {
        this.empty = empty;
    }

    public void addPiece (Pieces piece) {
        this.piece = piece;
        piece.addPlayfield(this);
        this.empty = false;
    }
    @Override
    public String toString() {
        String color;
        if (this.empty) color = " field is empty";
        else{
            if(piece.color == Pieces.Color.black) color = " - black";
            else color = "- white";
        }

        return"("+x +
            ", "+y + ") "+
             color;
    }
}


