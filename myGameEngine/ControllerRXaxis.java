package myGameEngine;
import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;
import ray.rml.Degreef;

public class ControllerRXaxis extends AbstractInputAction{
private Node d;
	
	
	public ControllerRXaxis (Node n) {
		d = n;
	}
	@Override
	public void performAction(float time, Event event) {
		if (event.getValue() < -0.5){
			//YAW Rotateleft
			
			//game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).yaw(Degreef.createFrom(3f));
			}
		else if (event.getValue() > 0.5) {
			//YAW Rotateright
			//game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).yaw(Degreef.createFrom(-3f));
		}
	}
}
