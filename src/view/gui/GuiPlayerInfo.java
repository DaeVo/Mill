package view.gui;

import controller.Controller;
import model.Utils;
import sun.security.x509.IPAddressName;
import view.AbstractPlayer;
import view.IPlayer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Henry on 24.08.2016.
 */
public class GuiPlayerInfo extends JPanel implements Observer {
    private Color playerColor;
    private Controller millController;
    private JLabel remainingPieces;

    public GuiPlayerInfo(Controller cont, Color color){
        playerColor = color;
        millController = cont;

        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createTitledBorder(Utils.getColorName(playerColor)));

        remainingPieces = new JLabel("Remaining Pieces: 9");
        this.add(remainingPieces);
        this.setMaximumSize(this.getPreferredSize());

        update(null, null);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (getPlayer() != null) {
            remainingPieces.setText("Remaining Pieces: " + millController.getState().getPieceCount(playerColor));
        }
    }

    private IPlayer getPlayer(){
        if (playerColor == Color.black)
            return millController.getBlackPlayer();
        return millController.getWhitePlayer();
    }
}

