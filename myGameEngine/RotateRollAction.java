package myGameEngine;
import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.Degreef;
public class RotateRollAction extends AbstractInputAction{
private MyGame game;
	
	public RotateRollAction(MyGame g) {
		game = g;
	}
	public void performAction(float time, Event event) {
		
		game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).roll(Degreef.createFrom(2f));
	}
}
