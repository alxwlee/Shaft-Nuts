package myGameEngine;

import ray.rage.scene.controllers.AbstractController;
import ray.rage.scene.Node;
import ray.rage.scene.controllers.*;
import ray.rml.*;
public class NodeControllers extends AbstractController{
	private float scaleRate = .003f; // growth per second
	private float cycleTime = 2000.0f; // default cycle time
	private float totalTime = 0.0f;
	private float direction = 1.0f;
	@Override
	protected void updateImpl(float elapsedTimeMillis) {
		totalTime += elapsedTimeMillis;
		float scaleAmt = 1.0f + direction * scaleRate;
	
		if (totalTime > cycleTime)
		{ 
			direction = -direction;
			totalTime = 0.0f;
		}
		for (Node n : super.controlledNodesList)
		{ 
			n.moveUp(direction);
		}
			
	}
}
