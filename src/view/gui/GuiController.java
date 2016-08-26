package view.gui;

import controller.Controller;
import model.Utils;
import view.IPlayer;
import view.ai.DummyPlayer;
import view.ai.SmartAi;
import view.ai.StupidAi;
import view.human.ConsoleView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Henry on 24.08.2016.
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
        IPlayer p1 = getMode(split[0]);
        IPlayer p2 = getMode(split[1]);

        millController.startGame(p1, p2);
    }

    private IPlayer getMode(String mode){
        if (mode.equals("Ai")){
            String diff = boxDifficulty.getSelectedItem().toString();
            if (diff.startsWith("Stupid")){
                return new StupidAi();
            } else {
                return new SmartAi();
            }
        } else {
            millController.setSleep(0);
            return new GuiPlayer();
        }
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
                lblTodo.setText("Move a stone");
                break;
            case Endgame:
                lblTodo.setText("Move a stone freely");
                break;
            case Exit:
                lblTodo.setText("End");
                break;
        }
    }
}
