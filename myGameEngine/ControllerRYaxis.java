package myGameEngine;
import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rml.*;
public class ControllerRYaxis extends AbstractInputAction{
	private MyGame game;
	
	public ControllerRYaxis(MyGame g) {
		game = g;
	}
	
	@Override
	public void performAction(float time, Event event) {
	
		if (event.getValue() < -0.5){
			//Rotate Up
			game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).pitch(Degreef.createFrom(-2.5f));
			}
		else if (event.getValue() > 0.5) {
			//Rotate Down
			game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).pitch(Degreef.createFrom(2.5f));	
		}
			
			
		}
		
	}
