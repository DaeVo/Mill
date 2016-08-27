package model;

import java.awt.*;
import java.io.Serializable;

import controller.Controller;

public interface IPlayer extends Runnable, Serializable {
	void create(Controller cont, Color playerColor);
	Color getColor();
	Controller getController();
	void turnInfo(Color moveColor, Move move);
}
