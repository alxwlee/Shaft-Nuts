package myGameEngine;
import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;

public class ControllerXaxis extends AbstractInputAction {
	private Node d;
	
	
	public ControllerXaxis (Node n) {
		d = n;
	}
	@Override
	public void performAction(float time, Event event) {
		if (event.getValue() < -0.5){
			d.moveRight(0.3f);
			//game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).moveRight(game.getMoveSpeed());
			}
		else if (event.getValue() > 0.5) {
			d.moveLeft(0.3f);
			//game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).moveLeft(game.getMoveSpeed());
		}
	}
}
