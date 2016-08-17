package view;


import controller.Controller;

public abstract class AbstractPlayer implements IPlayer {
	protected Controller millController;
	
	@Override
	public void setController(Controller cont) {
		millController = cont;	
	}
}
