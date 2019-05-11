package myGameEngine;

import a3.ProtocolClient;
import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3f;

public class RotateLeftAction extends AbstractInputAction {

	private MyGame game;
	private ProtocolClient protClient;
	
	public RotateLeftAction(MyGame g, ProtocolClient p) {
		game = g;
		protClient = p;
	}
	
	public void performAction(float time, Event event) {
		
		Vector3f tempv;
		if(game.getCamera().getMode() == 'n')
		{
			game.getEngine().getSceneManager().getSceneNode("myDolphinNode").yaw(Degreef.createFrom(3f));
			protClient.sendRotateMessage("left");
		}
		else
		{
			tempv = game.getCamera().getFd();
			Angle a = Degreef.createFrom(5.0f);
			//tempv.rotate(a, game.camera.getUp());
			game.getCamera().setFd((Vector3f)tempv.rotate(a, game.getCamera().getUp()).normalize());
			
			
			tempv = game.getCamera().getRt();
			 a = Degreef.createFrom(5.0f);
			//tempv.rotate(a, game.camera.getUp());
			game.getCamera().setRt((Vector3f)tempv.rotate(a, game.getCamera().getUp()).normalize());
		}
		
	}
}