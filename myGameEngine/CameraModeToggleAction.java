package myGameEngine;

import a3.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.SceneManager;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class CameraModeToggleAction extends AbstractInputAction {
	private MyGame game;
	
	private char mode;
	private SceneManager sm;
	private Vector3 dock;
	


	public CameraModeToggleAction(MyGame g) {
		
		game = g;
		sm = game.getEngine().getSceneManager();
		dock = Vector3f.createFrom(0.0f, 0.3f, 0.0f);
	}

	public void performAction(float time, Event event) { 
		
		mode = game.getCameraMode();
		if(mode == 'n') {
			//disabled change to camera mode "c"
			/*
			sm.getSceneNode("POVNode").detachAllChildren();
			sm.getSceneNode("MainCameraNode").setLocalRotation(sm.getSceneNode("myDolphinNode").getLocalRotation());
			sm.getSceneNode("MainCameraNode").setLocalPosition(sm.getSceneNode("myDolphinNode").getLocalPosition().add(dock));
			game.setActiveNode(sm.getSceneNode("MainCameraNode"));
			
			mode = 'c';
			game.setCameraMode(mode);
			*/
		
		}
		else {
			
			sm.getSceneNode("POVNode").attachChild(game.getEngine().getSceneManager().getSceneNode("MainCameraNode"));
			sm.getSceneNode("MainCameraNode").setLocalRotation(sm.getSceneNode("myDolphinNode").getLocalRotation());
			sm.getSceneNode("MainCameraNode").setLocalPosition(0.0f, 0.0f, 0.0f);
			game.setActiveNode(sm.getSceneNode("myDolphinNode"));
			mode = 'n';
			game.setCameraMode(mode);
			
			}
		System.out.println(mode);
		
	}
	
}
