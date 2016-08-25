package view.gui;

import controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Henry on 23.08.2016.
 */
public class GUIBoard extends JPanel {
    private Controller millController;
    private JLabel imageLabel;
    private List<StonePosition> positions;
    private final int CLICK_TOLERANCE = 10;

    public GUIBoard(Controller cont) {
        millController = cont;
        positions = new LinkedList<>();
        this.setLayout(new FlowLayout());

        try {
            String path = "board.jpg";
            File file = new File(path);
            BufferedImage image = ImageIO.read(file);
            imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    boardClick(e);
                }
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });

            this.add(imageLabel);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.setVisible(true);


    }

    public void boardClick(MouseEvent e){
        System.out.println(e);


        for (StonePosition sp : positions){
            if (e.getPoint().distance(sp.pos) < CLICK_TOLERANCE){
                System.out.println(sp.stone);
            }
        }
    }

    public void createList(){
        positions.add(new StonePosition(new Point(33,33), new Point(0,0)));
    }

    private class StonePosition{
        public Point pos;
        public Point stone;

        public StonePosition(Point p, Point s){
            pos = p;
            stone = s;
        }
    }

}

