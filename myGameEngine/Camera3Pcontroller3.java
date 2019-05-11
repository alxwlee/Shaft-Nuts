package myGameEngine;
import a3.MyGame;
import ray.input.InputManager;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.Camera;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
import ray.rml.Vector3f;
public class Camera3Pcontroller3//rotate controller clockwise
{ private Camera camera; //the camera being controlled
 private SceneNode cameraN; //the node the camera is attached to
 private SceneNode target; //the target the camera looks at
 private float cameraAzimuth; //rotation of camera around Y axis
 private float cameraElevation; //elevation of camera above target
 private float radias; //distance between camera and target
 private Vector3 targetPos; //target’s position in the world
 private Vector3 worldUpVec;
 
 
 public Camera3Pcontroller3(Camera cam, SceneNode camN, SceneNode targ, String controllerName, InputManager im)
{ 
	 
	 camera = cam;
	 cameraN = camN;
	 target = targ;
	 cameraAzimuth = 225.0f; // start from BEHIND and ABOVE the target
	 cameraElevation = 20.0f; // elevation is in degrees
	 radias = 8.0f;
	 worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
	 setupInput(im, controllerName);
	 updateCameraPosition();
}
 
 
 private void setupInput(InputManager im, String cn)
 { 
	 	Action orbitAAction = new OrbitAroundAction();
	 	im.associateAction(cn,
	 	net.java.games.input.Component.Identifier.Key.RIGHT, orbitAAction,
	 	InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	 	
	 	Action orbitAAction2 = new OrbitAroundAction2();
	 	im.associateAction(cn,
	 	net.java.games.input.Component.Identifier.Key.LEFT, orbitAAction2,
	 	InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	 	
	 	Action orbitElevation1Action = new OrbitElevation1Action();
	 	im.associateAction(cn,
	 	net.java.games.input.Component.Identifier.Key.UP, orbitElevation1Action,
	 	InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	 	
	 	Action orbitElevation2Action = new OrbitElevation2Action();
	 	im.associateAction(cn,
	 	net.java.games.input.Component.Identifier.Key.DOWN, orbitElevation2Action,
	 	InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	 	
	 	Action orbitRadias1Action = new OrbitRadias1Action();
	 	im.associateAction(cn,
	 	net.java.games.input.Component.Identifier.Key.Z, orbitRadias1Action,
	 	InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	 	
	 	Action orbitRadias2Action = new OrbitRadias2Action();
	 	im.associateAction(cn,
	 	net.java.games.input.Component.Identifier.Key.X, orbitRadias2Action,
	 	InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
 
	 	
 }
 public class OrbitAroundAction extends AbstractInputAction
 { 
	 
	 // Moves the camera around the target (changes camera azimuth).
	 public void performAction(float time, net.java.games.input.Event evt)
	 { 
		 float rotAmount;
		 if (evt.getValue() < -0.2)
		 { 
			 rotAmount=-0.8f; 
		 }
         else
         { 
        	 if (evt.getValue() > 0.2)
        	 { 
        		 rotAmount=0.8f; 
        	 }
        	 else
        	 { 
        		 rotAmount=0.0f; 	 
        	 }
         }
		 cameraAzimuth -= 0.8f;
		 //cameraAzimuth = cameraAzimuth % 360;
		 updateCameraPosition();
  } }
 
 public class OrbitAroundAction2 extends AbstractInputAction
 { 
	 
	 // Moves the camera around the target (changes camera azimuth).
	 public void performAction(float time, net.java.games.input.Event evt)
	 { 
		 float rotAmount;
		 if (evt.getValue() < -0.2)
		 { 
			 rotAmount=-0.8f; 
		 }
         else
         { 
        	 if (evt.getValue() > 0.2)
        	 { 
        		 rotAmount=0.8f; 
        	 }
        	 else
        	 { 
        		 rotAmount=0.0f; 	 
        	 }
         }
		 cameraAzimuth += 0.8f;
		 //cameraAzimuth = cameraAzimuth % 360;
		 updateCameraPosition();
  } }
 
 public class OrbitElevation1Action extends AbstractInputAction
 { 
	 public void performAction(float time, net.java.games.input.Event evt)
	 { 
		
		 cameraElevation += 0.8;
		 if ( cameraElevation > 80)
			 cameraElevation = 80;
		 updateCameraPosition();
  } }
 
 public class OrbitElevation2Action extends AbstractInputAction
 { 
	 public void performAction(float time, net.java.games.input.Event evt)
	 { 
		
		 cameraElevation -= 0.8;		 
		 if ( cameraElevation < 10)
			cameraElevation = 10;
		 updateCameraPosition();
  } }
 
 
 public class OrbitRadias1Action extends AbstractInputAction
 { 
	 // Moves the camera around the target (changes camera azimuth).
	 public void performAction(float time, net.java.games.input.Event evt)
	 { 
		
		 radias += -0.1;
		 if (radias < 3)
			 radias = 3;
		 updateCameraPosition();
  } }
 
 public class OrbitRadias2Action extends AbstractInputAction
 { 
	 public void performAction(float time, net.java.games.input.Event evt)
	 { 
		
		 radias += 0.1;
		 if (radias >10)
			 radias = 10;
		 updateCameraPosition();
	 }
	 
 }

 public void updateCameraPosition()
 { 
	 double theta = Math.toRadians(cameraAzimuth); // rot around target
	 double phi = Math.toRadians(cameraElevation); // altitude angle
	 double x = radias * Math.cos(phi) * Math.sin(theta);
	 double y = radias * Math.sin(phi);
	 double z = radias * Math.cos(phi) * Math.cos(theta);
	 cameraN.setLocalPosition(Vector3f.createFrom
			 ((float)x, (float)y, (float)z).add(target.getWorldPosition()));
	 cameraN.lookAt(target, worldUpVec);
  }
}