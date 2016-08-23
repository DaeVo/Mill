package view.ai;

import model.Gamestate;

import java.awt.Point;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;
import java.util.List;
/**
 * Created by Max on 19/08/2016.
 * An implemtation of a Monte Carlo Tree Search algorithm to determine the SmartAI's next step.
 */
public class MCTS {



    public Node root; //global root Node
    Gamestate gametateObject = new Gamestate(null);
    /*
    initializes the tree
     */
    public void initializeMCTS(){
        root = new Node();
    }

    public int getRandomNumber(){
        Random rn = new Random();
        return rn.nextInt();
    }

    private Point randomMoveSource (Color color) { //gets a random piece to move this roun
        d
        List<Point> tmpList = new LinkedList<Point>();
        for (model.Pieces p : gametateObject.currentPieces) {
            if (color == p.color) {
                Point tmpPoint = new Point (p.field.x, p.field.y);
                tmpList.add(tmpPoint);
            }
        }
        return tmpList.get(getRandomNumber() % tmpList.size());
    }

    private Point selectRandomPlacing() {
        int randomValue = getRandomNumber();
        return gametateObject.legalPlacing.get(randomValue % gametateObject.legalPlacing.size());
    }

    private Point selectRandomMove() {
    }

    /*
    choses the best Node
     */
    public void selectMove() {
        //logic to select e.g. highest winrate or highest win count node
        // root = selectedNode
    }
}
