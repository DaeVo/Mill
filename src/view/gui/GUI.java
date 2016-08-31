package view.gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Max on 23.08.2016.
 */
public class GUI extends JFrame {
    private Controller millController;

    public GUI(Controller cont){
        millController = cont;

        //Side Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        GuiController gc = new GuiController(millController);
        leftPanel.add(gc.getControllerPanel());
        leftPanel.add(new JLabel(""));
        millController.addObserver(gc);

        //Info Boxes
        GuiPlayerInfo bi = new GuiPlayerInfo(millController, Color.black);
        GuiPlayerInfo wi = new GuiPlayerInfo(millController, Color.white);
        millController.addObserver(bi);
        millController.addObserver(wi);

        leftPanel.add(bi);
        leftPanel.add(wi);
        leftPanel.setSize(leftPanel.getMinimumSize());

        //Game Panel
        GUIBoard gBoard = new GUIBoard(millController);
        millController.addObserver(gBoard);

        //Finish
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
        this.add(leftPanel);
        this.add(gBoard);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
    }

}
