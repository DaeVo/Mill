package view;

		import java.util.Observable;
		import java.util.Observer;

		import controller.Controller;

public interface IPlayer extends Runnable {
	void setController(Controller cont);
}
