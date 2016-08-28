package model;
import java.awt.Point;

/**
 * Created by Max on 19/08/2016.
 * stores the src and dst of a movement to create Move objects that can be stored in the Node
 */
public class Move implements java.io.Serializable {
    public Move(Point src, Point dst){this.src = src; this.dst = dst;}
    public Point src = null;  //null while placing
    public Point dst = null;

    @Override
    public String toString() {
        String mode = "";
        if (src != null && dst == null){
            mode = "remove";
        } else if (src == null && dst != null) {
            mode = "placing";
        } else {
            mode = "move";
        }

        return String.format("Move - Type %s Src %s  Dst %s", mode, src, dst);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Move other = (Move) obj;
        if (dst == null) {
            if (other.dst != null)
                return false;
        } else if (!dst.equals(other.dst))
            return false;
        if (src == null) {
            if (other.src != null)
                return false;
        } else if (!src.equals(other.src))
            return false;
        return true;
    }
}
