package view.gui;

import controller.Controller;
import model.GamePhase;
import model.Playfield;
import model.IPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Max on 23.08.2016.
 */
public class GUIBoard extends JPanel implements Observer {
    private Controller millController;
    BufferedImage image;
    private JLabel imageLabel;
    private List<StonePosition> positions;
    private final int STONE_RADIUS = 50;

    private StonePosition sourceStone;

    public GUIBoard(Controller cont) {
        millController = cont;
        positions = new LinkedList<>();
        this.setLayout(new FlowLayout());

        try {
            String path = "board.jpg";
            File file = new File(path);
            image = ImageIO.read(file);
            imageLabel = new JLabel(new ImageIcon(image));

            imageLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    boardClick(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

            this.add(imageLabel);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.setVisible(true);
        createList();
    }


    public void boardClick(MouseEvent e) {
        //End or not started
        if (millController.getGamePhase() == GamePhase.Exit || millController.getGamePhase() == null)
            return;


        for (StonePosition sp : positions) {
            if (e.getPoint().distance(sp.pos) < STONE_RADIUS) {
                Playfield field = millController.getState().board[sp.stone.x][sp.stone.y];
                IPlayer player = millController.getTurnPlayer();
                System.out.println("Clicked: " + sp.stone + " Player: " + player);


                //Not our turn
                if (!(player instanceof GuiPlayer))
                    return;

                if (millController.getGamePhase() == GamePhase.Placing) {
                    millController.place(sp.stone);
                } else if (millController.getGamePhase() == GamePhase.RemovingStone) {
                    millController.removeStone(sp.stone);
                } else {
                    //1. select a source
                    //2. select a dest
                    if (sourceStone == null) {
                        if (field.empty)
                            return;

                        sourceStone = sp;
                        sp.highlighted = true;
                        System.out.println("Selected the stone " + sp.stone);
                    } else {
                        if (millController.getGamePhase() == GamePhase.Moving) {
                            millController.move(sourceStone.stone, sp.stone);
                        } else {
                            millController.moveFreely(sourceStone.stone, sp.stone);
                        }
                        System.out.println("Tried movement to  " + sp.stone);
                        sourceStone.highlighted = false;
                        sourceStone = null;
                    }
                }
                break;
            }
        }

        paintStones();
    }

    @Override
    public void update(Observable o, Object arg) {
        paintStones();

        //end game string
        if (arg instanceof String) {
            JOptionPane.showMessageDialog(null, arg);
        }
    }

    public void paintStones() {
        BufferedImage img2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = img2.createGraphics();

        g.drawImage(image, 0, 0, null);

        for (StonePosition sp : positions) {
            Playfield field = millController.getState().board[sp.stone.x][sp.stone.y];
            //drawing is from top left, our pos is in center, we need to fix that
            Point center = new Point(sp.pos.x - (STONE_RADIUS / 2), sp.pos.y - (STONE_RADIUS / 2));

            if (!field.empty) {
                g.setColor(field.piece.color);
                g.fillOval(center.x, center.y, STONE_RADIUS, STONE_RADIUS);
                if (sp.highlighted) {
                    g.setStroke(new BasicStroke(5));
                    g.setColor(Color.yellow);
                    g.drawOval(center.x, center.y, STONE_RADIUS + 3, STONE_RADIUS + 3);
                }
            }
        }
        imageLabel.setIcon(new ImageIcon(img2));
        imageLabel.revalidate();
    }

    public void createList() {
        positions.add(new StonePosition(new Point(33, 33), new Point(0, 0)));
        positions.add(new StonePosition(new Point(404, 33), new Point(1, 0)));
        positions.add(new StonePosition(new Point(764, 33), new Point(2, 0)));

        positions.add(new StonePosition(new Point(141, 141), new Point(0, 1)));
        positions.add(new StonePosition(new Point(404, 141), new Point(1, 1)));
        positions.add(new StonePosition(new Point(655, 141), new Point(2, 1)));

        positions.add(new StonePosition(new Point(253, 253), new Point(0, 2)));
        positions.add(new StonePosition(new Point(404, 253), new Point(1, 2)));
        positions.add(new StonePosition(new Point(548, 253), new Point(2, 2)));

        //mid left
        positions.add(new StonePosition(new Point(33, 406), new Point(7, 0)));
        positions.add(new StonePosition(new Point(141, 406), new Point(7, 1)));
        positions.add(new StonePosition(new Point(253, 406), new Point(7, 2)));

        //mid right
        positions.add(new StonePosition(new Point(548, 406), new Point(3, 2)));
        positions.add(new StonePosition(new Point(655, 406), new Point(3, 1)));
        positions.add(new StonePosition(new Point(764, 406), new Point(3, 0)));


        positions.add(new StonePosition(new Point(253, 544), new Point(6, 2)));
        positions.add(new StonePosition(new Point(404, 544), new Point(5, 2)));
        positions.add(new StonePosition(new Point(548, 544), new Point(4, 2)));

        positions.add(new StonePosition(new Point(141, 657), new Point(6, 1)));
        positions.add(new StonePosition(new Point(404, 657), new Point(5, 1)));
        positions.add(new StonePosition(new Point(655, 657), new Point(4, 1)));

        positions.add(new StonePosition(new Point(33, 766), new Point(6, 0)));
        positions.add(new StonePosition(new Point(404, 766), new Point(5, 0)));
        positions.add(new StonePosition(new Point(764, 766), new Point(4, 0)));
    }


    private class StonePosition {
        public Point pos;
        public Point stone;
        public boolean highlighted;

        public StonePosition(Point p, Point s) {
            pos = p;
            stone = s;
        }


    }

}

