package view.gui;

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
    private IPlayer player;
    private JLabel remainingPieces;

    public GuiPlayerInfo(IPlayer p){
        player = p;
        this.setBorder(BorderFactory.createTitledBorder(Utils.getColorName(p.getColor())));
        remainingPieces = new JLabel("");
        this.add(remainingPieces);

        this.setLayout(new FlowLayout());

        update(null, null);
    }


    @Override
    public void update(Observable o, Object arg) {
        remainingPieces.setText("Remaining Pieces: " + player.getController().getState().getPieceCount(player.getColor()));
    }
}

