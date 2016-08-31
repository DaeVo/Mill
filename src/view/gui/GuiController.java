package view.gui;

import controller.Controller;
import model.Utils;
import model.IPlayer;
import view.ai.SmartAi;
import view.ai.StupidAi;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static model.Utils.freeMoveAllowed;

/**
 * Created by Max on 24.08.2016.
 */
public class GuiController implements Observer {
    private JComboBox boxMode;
    private JButton btnStartGame;
    private JComboBox boxDifficulty;
    private JPanel controllerPanel;
    private JLabel lblNextTurn;
    private JLabel lblTodo;
    private Controller millController;

    public GuiController(Controller cont){
        millController = cont;
        btnStartGame.addActionListener(x -> start());
        controllerPanel.setMaximumSize(controllerPanel.getPreferredSize());
    }

    private void start(){
        String[] split = boxMode.getSelectedItem().toString().split("-");
        IPlayer whiteP = getMode(split[0]);
        whiteP.create(millController, Color.white);

        IPlayer blackP = getMode(split[1]);
        blackP.create(millController, Color.black);

        millController.startGame(whiteP, blackP);
    }

    private IPlayer getMode(String mode){
        if (mode.equals("Ai")) {
            String diff = boxDifficulty.getSelectedItem().toString();
            if (diff.startsWith("Stupid")) {
                return new StupidAi();
            } else if (diff.startsWith("Easy")) {
                return new SmartAi(2 * 1000);
            } else if (diff.startsWith("Medium")) {
                return new SmartAi(5 * 1000);
            } else if (diff.startsWith("Hard")) {
                return new SmartAi(10 * 1000);
            } else if (diff.startsWith("1"))
                return new SmartAi(15 * 1000);
            else if (diff.startsWith("2"))
                return new SmartAi(25 * 1000);
            else if (diff.startsWith("3"))
                return new SmartAi(30 * 1000);
            else if (diff.startsWith("4"))
                return new SmartAi(40 * 1000);
            else if (diff.startsWith("5"))
                return new SmartAi(60 * 1000);

        } else {
            millController.setSleep(100);
            return new GuiPlayer();
        }
        return null;
    }

    public JPanel getControllerPanel(){
        return controllerPanel;
    }

    @Override
    public void update(Observable o, Object arg) {
        lblNextTurn.setText("Next Turn: " + Utils.getColorName(millController.getTurnColor()));
        switch (millController.getGamePhase()){
            case Placing:
                lblTodo.setText("Place a stone");
                break;
            case RemovingStone:
                lblTodo.setText("Remove a stone");
                break;
            case Moving:
                if (!freeMoveAllowed(millController, millController.getTurnColor())) {
                    lblTodo.setText("Move a stone");
                } else {
                    lblTodo.setText("Move a stone freely");
                }
                break;
            case Exit:
                lblTodo.setText("End");
                break;
        }
    }
}
