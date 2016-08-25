package view;


import controller.Controller;

import java.awt.*;

public abstract class AbstractPlayer implements IPlayer {
	protected Controller millController;
	protected Color myColor;
	
	@Override
	public void create(Controller cont, Color playerColor) {
		millController = cont;
		myColor = playerColor;
	}

	public Color getColor(){
		return myColor;
	}

	public Controller getController(){
		return millController;
	}
}
