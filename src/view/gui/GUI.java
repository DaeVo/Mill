package view.gui;

import controller.Controller;
import view.AbstractPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Henry on 23.08.2016.
 */
public class GUI extends JFrame {
    private Controller millController;

    public GUI(Controller cont, Observable observ){
        millController = cont;

        //Side Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        GuiController gc = new GuiController(millController);
        leftPanel.add(gc.getControllerPanel());

        //Info Boxes
        GuiPlayerInfo bi = new GuiPlayerInfo(millController, Color.black);
        GuiPlayerInfo wi = new GuiPlayerInfo(millController, Color.white);
        observ.addObserver(bi);
        observ.addObserver(wi);

        leftPanel.add(bi);
        leftPanel.add(wi);
        leftPanel.setSize(leftPanel.getMinimumSize());
        //Game Panel
        GUIBoard gBoard = new GUIBoard(millController);

        //Finish
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
        this.add(leftPanel);
        this.add(gBoard);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
    }

}
