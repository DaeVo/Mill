package model;

/**
 * Created by Max on 18/08/2016.
 */
public class MillInfo {

    private Playfield playfield1;
    private Playfield playfield2;
    private Playfield playfield3;

    public MillInfo(Playfield playfield1, Playfield playfield2, Playfield playfield3){
        this.playfield1 = playfield1;
        this.playfield2 = playfield2;
        this.playfield3 = playfield3;
    }

    public boolean isMill() {
        if (!playfield1.empty && !playfield2.empty && !playfield3.empty) {
            if (playfield1.piece.color == playfield2.piece.color && playfield1.piece.color == playfield3.piece.color)
                return true;
        }return false;
    }
}
