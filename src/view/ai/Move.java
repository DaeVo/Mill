package view.ai;
import java.awt.Point;

/**
 * Created by Max on 19/08/2016.
 * stores the src and dst of a movement to create Move objects that can be stored in the Node
 */
public class Move {
    public Move(Point src, Point dst){this.src = src; this.dst = dst;}
    public Point src;  //null while placing
    public Point dst;
}
