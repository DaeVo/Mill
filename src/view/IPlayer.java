package view;

import java.awt.*;

		import controller.Controller;

public interface IPlayer extends Runnable {
	void create(Controller cont, Color playerColor);
}
