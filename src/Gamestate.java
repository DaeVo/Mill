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





    public Integer millCount = 0;  // number of pieces currently inside a mill (first index of currentMills)
    String millType;
    Integer[][] currentMills = new Integer[18][2];   //maximum theoretical amount of pieces in mills = 18
    private void currentPiecesMill(int i, int j) {  //array of indices for fields in mill.
        int t = 0;
        int z = 0;
        switch (millType) {
            case "inward":
                for (t = 0; t < 3; t++) {
                    currentMills[millCount][0] = i;
                    currentMills[millCount][1] = t;
                    board[i][t].piece.millCount = millCount;
                    millCount++;;
                }
                break;
            case "up": z = 0;
                break;
            case "right": z = 2;
                break;
            case "down": z = 4;
                for (t = z; t < z + 3; t++) {
                    currentMills[millCount][0] = t;
                    currentMills[millCount][1] = j;
                    board[t][j].piece.millCount = millCount;
                    millCount++;
                }
                break;
            case "left": z = 6;
                for (t = z; t < z + 2; t++) {
                    currentMills[millCount][0] = t;
                    currentMills[millCount][1] = j;
                    board[t][j].piece.millCount = millCount;
                    millCount++;
                }
                currentMills[millCount][0] = 0;
                currentMills[millCount][1] = j;
                board[t][j].piece.millCount = millCount;
                millCount++;
                break;

        }
    }


    public boolean millCheck() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                if (!board[i][j].empty) {
                    Pieces.Color color = board[i][j].piece.color;
                    if (i % 2 == 1) {   //to check if its one of the four legit outwards to inward rows
                        if ((board[i][0].piece.color == board[i][1].piece.color) // 3pieces of the same color from outwards to inwards
                                && (board[i][0].piece.color == board[i][2].piece.color)){
                           millType = "inward";
                            return true;
                        }
                    }if (i >= 0 && i < 3) { //upper quarter of the playfield
                        if ((board[0][j].piece.color == board[1][j].piece.color) //3 pieces of the same color next to each other  on the upper quarter
                                && (board[0][j].piece.color == board[2][j].piece.color)) {
                            millType = "up";
                            return true;
                        }
                    }if (i >= 2 && i < 5) {  // right quarter
                        if ((board[2][j].piece.color == board[3][j].piece.color) //3 pieces of the same color next to each other  on the right quarter
                                && (board[2][j].piece.color == board[4][j].piece.color)) {
                            millType = "right";
                            return true;
                        }
                    }if (i >= 4 && i < 7) {
                        if ((board[4][j].piece.color == board[5][j].piece.color) //3 pieces of the same color next to each other  on the lower quarter
                                && (board[4][j].piece.color == board[6][j].piece.color)) {
                            millType = "down";
                            return true;
                        }
                    }if (i >= 6) {
                        if ((board[6][j].piece.color == board[7][j].piece.color) //3 pieces of the same color next to each other  on the left quarter
                                && (board[6][j].piece.color == board[0][j].piece.color)) {
                            millType = "left";
                            return true;
                        }
                    }
                }
            }
        }return false;
    }
}


