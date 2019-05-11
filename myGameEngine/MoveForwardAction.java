package myGameEngine;

import a3.ProtocolClient;
import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;
import ray.rml.Vector3;
import ray.rml.Vector3f;


public class MoveForwardAction extends AbstractInputAction {
	private Node avN;
	private MyGame game;
	private ProtocolClient protClient;
	
	public MoveForwardAction(MyGame g,ProtocolClient p, Node n ) {
		game = g;
		protClient = p;
		avN= n;
	}
	
	@Override
	public void performAction(float time, Event event) {
		/*
		if(game.getCamera().getMode() == 'n')
		{
		//game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName() + "Position").moveForward(game.getMoveSpeed());
		game.getEngine().getSceneManager().getSceneNode("myDolphinNode").moveForward(game.getMoveSpeed());
		//game.getEngine().getSceneManager().getSceneNode("myDolphin2Node").getWorldPosition()
		//protClient.sendMoveMessage(game.getEngine().getSceneManager().getSceneNode("myDolphin2Node").getWorldPosition());
		protClient.sendMoveMessage( game.getPlayerPosition());
		//game.getEngine().getSceneManager().getSceneNode(game.getActiveNode().getName()).moveBackward(game.getMoveSpeed());
		
		}
		else if(game.distanceTo(game.getCamera().getPo(), game.getEngine().getSceneManager().getSceneNode("myDolphin2Node").getLocalPosition()) <= 10)
		{
			
			Vector3f v = game.getCamera().getFd();
			Vector3f p = game.getCamera().getPo();
			Vector3f p1 =
			 (Vector3f) Vector3f.createFrom(0.01f*v.x(), 0.01f*v.y(), 0.01f*v.z());
			Vector3f p2 = (Vector3f) p.add((Vector3)p1);
			game.getCamera().setPo((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			
			//game.getEngine().getSceneManager().getSceneNode("cameraPositionNode").moveForward(game.getMoveSpeed());	
			
			//game.camera.setPo((Vector3f)game.getEngine().getSceneManager().getSceneNode("cameraPositionNode").getLocalPosition());
			
		}
		*/
		//game.camera.setMode('n');
		avN.moveForward(0.5f);
		game.updateVerticalPosition();
	}
}
