/**
 * Created by Max on 16/08/2016.
 */
public class Gamestate {

    Playfield[][] board = new Playfield[8][3];

    public Gamestate(Playfield[][] board) {
        this.board = board;
    }

    public static int pieceCountWhite = 9;
    public static int pieceCountBlack = 9;
    public static int turnsNoMill = 0; // resets if mill happens, if >49 => tie

    public static Pieces[] currentPieces = new Pieces[18];

    public void createPieces() {  //initializes 18 starting Pieces
        for (int i = 0; i < 18; i++) {
            Pieces.Color color;
            if (i % 2 == 1) color = Pieces.Color.black;
            else color = Pieces.Color.white;
            currentPieces[i] = new Pieces(color);
        }
    }
}


