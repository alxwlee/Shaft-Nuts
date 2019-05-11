package myGameEngine;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
public class FloatUpController extends AbstractController{

	
	private float scaleRate = .003f; // growth per second
	private float cycleTime = 2000.0f; // default cycle time
	private float totalTime = 0.0f;
	private float direction = 1.0f;
	
	float velocity = 0.5f;
	int iterations = 0;
	
	@Override
	protected void updateImpl(float elapsedTimeMillis)
	{ 
		totalTime += elapsedTimeMillis;
	
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
