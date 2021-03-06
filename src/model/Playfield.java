package model;

import java.awt.*;



/**
 * Created by Max on 16/08/2016.
 * basic methods to structure the board and make movements (by real players) possible
 */

public class Playfield implements java.io.Serializable {

    public boolean empty;
    public Piece piece;
    /*
    so the field knows it's own array position
     */
    public int x;
    public int y;

    public Playfield(boolean empty) {
        this.empty = empty;
    }
    public Playfield(boolean empty, int x, int y) {
        this.empty = empty;
        this.x = x;
        this.y = y;
    }


    /*
    decides if a given field is a neighbour to another field
     */
    public boolean isNeighbour(Playfield dst) {
        if (this.x % 2 == 1) {
            if (Math.abs(this.y - dst.y) == 1 && this.x == dst.x)
                return true;  // from outside to inside neighbours without diagonals
        }
        if ((this.x == 7 && dst.x == 0 && dst.y == this.y) || (dst.x == 7 && this.x == 0 && this.y == dst.y))
            return true; //special case if this.x or dst.x == 7
        if (Math.abs(this.x - dst.x) == 1 && this.y == dst.y)
            return true;  //on the same level within 1 range of each other
        //System.out.println("no Neighbour"); //remove after testing
        return false;
    }



    /*
    moving to a neighbourfield if it's empty
     */
    public boolean move(Playfield dst) {
        if (this.isNeighbour(dst)) {
            return moveFreely(dst);
        }
        return false;
    }
    /*
    returns true if the destination is emtpy
     */
    public boolean moveFreely(Playfield dst) {
        if (dst.empty && !this.empty) {
            this.conquerField(dst);
            return true;
        }
        return false;
    }


    /*
    assigns the piece of one field to another field while resetting the original field
     */
    public void conquerField(Playfield dst) {
        dst.addPiece(this.piece);
        this.empty = true;
        this.piece = null;
    }

    /*
    ensures that piece is known to the field and vice versa
     */
    public void addPiece(Piece piece) {
        this.piece = piece;
        piece.addPlayfield(this);
        this.empty = false;
    }

    public Point getPoint() {
        return new Point(x, y);
    }

    public String shortString() {
        if (this.empty)
            return " ";
        else if (piece.color.equals(Color.black))
            return "B";
        else
            return "W";
    }

    @Override
    public String toString() {
        String color;
        if (this.empty)
            color = " field is empty";
        else {
            if (piece.color.equals(Color.black)) {
                color = "- black";
            } else {
                color = "- white";
            }
        }
        return String.format("(%d, %d) %s", x, y, color);
    }
}


