package model;


import java.awt.*;
import java.util.LinkedList;
import java.util.List;

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


     public boolean isNeighbour (Playfield dst){
         if(this.x  % 2 == 1) {
             if (Math.abs(this.y - dst.y) == 1 && this.x == dst.x)
                 return true;  // from outside to inside neighbours without diagonals
         }
         else if (this.x == 7 && dst.x == 0 && dst.y == this.y || dst.x == 7 && this.x == 0 && this.y == dst.y)
             return true; //special case if this.x or dst.x == 7
         else if (Math.abs(this.x - dst.x) == 1 && this.y == dst.y)
             return true;  //on the same level within 1 range of each other
         else {
             System.out.println("no Neighbour"); //remove after testing
             return false;
         }
         return false;
     }


    /*
    if the field whose piece is to be moved must not be empty, destination must be empty, destination must be a direct
     "allowed" neighbour of the field whose piece is to be moved.
     */

    public boolean move (Playfield dst) {
        if (this.isNeighbour(dst)){
            return moveFreely(dst);
        }
        return false;
    }

    public boolean moveFreely (Playfield dst) {
        if (dst.empty && !this.empty) {
            System.out.println(this + "succesfully moved to" + dst);
            this.conquerField(dst);
            return true;
        }
        System.out.println(this + " to "+ dst +  " is no legit move (either moving empty field, or dst isnt empty");
        return false;
    }



    /*
    assigns the piece of one field to another field while resetting the original field
     */
    public void conquerField(Playfield dst){
        dst.addPiece(this.piece);
        this.empty = true;
        this.piece = null;
    }

    /*
    ensures that piece is known to the field and vice versa
     */
    public void addPiece (Pieces piece) {
        this.piece = piece;
        piece.addPlayfield(this);
        this.empty = false;
    }

    public Point getPoint(){
        return new Point(x, y);
    }

    @Override
    public String toString() {
        String color;
        if (this.empty) color = " field is empty";
        else{
            if(piece.color == Color.black) color = " - black";
            else color = "- white";
        }

        return"("+x +
            ", "+y + ") "+
             color;
    }
}


