package view.gui;

import controller.Controller;
import view.AbstractPlayer;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Henry on 23.08.2016.
 */
public class GUI extends JFrame {
    private Controller millController;

    public GUI(Controller cont, Observable observ){
        millController = cont;
        JPanel leftPanel = new JPanel();

        GuiController gc = new GuiController(millController);
        leftPanel.add(gc.getControllerPanel());

        GuiPlayerInfo bi = new GuiPlayerInfo(millController.getBlackPlayer());
        GuiPlayerInfo wi = new GuiPlayerInfo(millController.getWhitePlayer());
        observ.addObserver(bi);
        observ.addObserver(wi);

        leftPanel.add(bi);
        leftPanel.add(wi);

        this.add(leftPanel);
    }


}
