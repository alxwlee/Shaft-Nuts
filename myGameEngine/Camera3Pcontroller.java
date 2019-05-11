package myGameEngine;
import java.awt.Component;

import net.java.games.input.Event;
import net.java.games.input.Component.Identifier;
import ray.input.InputManager;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.Camera;
import ray.rage.scene.SceneNode;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class Camera3Pcontroller {
	private Camera camera; // the camera being controlled
	private SceneNode cameraN; // the node the camera is attached to
	private SceneNode target; // the target the camera looks at
	private float cameraAzimuth; // rotation of camera around Y axis
	private float cameraElevation; // elevation of camera above target
	private float radias; // distance between camera and target
	private Vector3 targetPos; // target’s position in the world
	private Vector3 worldUpVec;
	
	public Camera3Pcontroller(Camera cam, SceneNode camN,SceneNode targ, String controllerName, InputManager im) {
		camera = cam;    
		cameraN = camN;    
		target = targ;    
		cameraAzimuth = 225.0f;// start from BEHIND and ABOVE the target    
		cameraElevation = 20.0f;// elevation is in degrees    
		radias = 2.0f;    
		worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);    
		setupInput(im, controllerName);    
		updateCameraPosition();
	}
	public void updateCameraPosition() {
		double theta = Math.toRadians(cameraAzimuth); // rot around target
		double phi = Math.toRadians(cameraElevation); // altitude angle
		double x = 1 * radias * Math.cos(phi) * Math.sin(theta);
		double y = radias * Math.sin(phi);
		double z = 1 * radias * Math.cos(phi) * Math.cos(theta);
		cameraN.setLocalPosition(Vector3f.createFrom
				((float) x, (float) y, (float) z).add(target.getWorldPosition()));
		cameraN.lookAt(target, worldUpVec);
	}
	private void setupInput(InputManager im, String cn)  {
		if (cn != null) {
		Action orbitAAction = new OrbitAroundAction();    
		im.associateAction(cn,          
				net.java.games.input.Component.Identifier.Axis.RX,  orbitAAction,          
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		//  similar input set up for OrbitRadiasAction, OrbitElevationAction  
		// OrbitRadiasAction
		Action orbitBAction = new OrbitRadiasAction();
		im.associateAction(cn,          
				net.java.games.input.Component.Identifier.Axis.RY,  orbitBAction,          
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// OrbitElevationAction
		Action orbitCAction = new OrbitElevationAction1();
		im.associateAction(cn,          
				net.java.games.input.Component.Identifier.Button._1,  orbitCAction,          
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		Action orbitDAction = new OrbitElevationAction2();
		im.associateAction(cn,          
				net.java.games.input.Component.Identifier.Button._2,  orbitDAction,          
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
		
	}
	private class OrbitAroundAction extends AbstractInputAction{
		// Moves the camera around the target (changes camera azimuth).
		public void performAction(float time, net.java.games.input.Event evt)
		{
			float rotAmount;      
			if (evt.getValue() < -0.4 )        
			{ rotAmount=-0.9f; }          
			else          
			{ if (evt.getValue() > 0.4)    
				{ rotAmount=0.9f; }          
				else          
				{ rotAmount=0.0f; }      
				}       
			cameraAzimuth += rotAmount;      
			cameraAzimuth = cameraAzimuth % 360;  
			updateCameraPosition();  
			}}
	private class OrbitRadiasAction extends AbstractInputAction{
		public void performAction(float time, net.java.games.input.Event evt)
		 {
			 if (evt.getValue() < -0.4 && radias > 1)
			 {
				 radias -= 0.1f;
			 }
			 else if (evt.getValue() > 0.4f && radias < 5)
			 {
				 radias += 0.1f;
			 }
			 else if (radias <= 1) {
				 radias = 1.1f;
			 }
			 else if (radias >=5) {
				 radias = 4.9f;
			 }
			 updateCameraPosition();
	}}
	private class OrbitElevationAction1 extends AbstractInputAction{
		public void performAction(float time, net.java.games.input.Event evt)
		 {
			 if (evt.getValue() >0 && cameraElevation <= 50.0f){
				 cameraElevation += 1.0f;
				 cameraElevation = cameraElevation % 360;
				 updateCameraPosition();
			 
		 }
	}
	}
		private class OrbitElevationAction2 extends AbstractInputAction{
			public void performAction(float time, net.java.games.input.Event evt) {
			if (evt.getValue() > 0.0 && cameraElevation >= 2.0f) {
				 cameraElevation -= 1.0f;
				 cameraElevation = cameraElevation % 360;
				 updateCameraPosition();
	
}
} 
		}}

