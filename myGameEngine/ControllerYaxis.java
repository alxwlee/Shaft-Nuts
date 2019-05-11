package myGameEngine;
import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;
public class ControllerYaxis extends AbstractInputAction{
private Node d;
	
	public ControllerYaxis(Node n) {
		d = n;
	}
	
	@Override
	public void performAction(float time, Event event) {
	
		if (event.getValue() < -0.5){
			d.moveForward(0.3f);
			//game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).moveForward(game.getMoveSpeed());
			
		}
		else if (event.getValue() > 0.5) {
			
			d.moveBackward(0.3f);
			//game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).moveBackward(game.getMoveSpeed());
		}
			
			
		}
		
	}


