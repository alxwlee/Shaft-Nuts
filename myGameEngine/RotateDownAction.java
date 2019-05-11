package myGameEngine;

import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.Degreef;

public class RotateDownAction extends AbstractInputAction {

	private MyGame game;
	
	public RotateDownAction(MyGame g) {
		
		game = g;
	}
	
	public void performAction(float time, Event event) {
		
		game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).pitch(Degreef.createFrom(2.5f));	}
}