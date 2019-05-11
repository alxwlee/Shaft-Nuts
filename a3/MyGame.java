// Client

package a3;

import java.awt.*;

import java.awt.geom.AffineTransform;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import myGameEngine.*;

import net.java.games.input.Event;

import java.lang.Math;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ray.input.GenericInputManager;
import ray.input.InputManager;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.networking.IGameConnection.ProtocolType;
import ray.rage.*;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.Projection;
import ray.rage.scene.controllers.*;
import ray.rage.util.BufferUtil;
import ray.rage.util.Configuration;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.rendersystem.states.*;
import ray.rage.rendersystem.states.RenderState.Type;
import ray.rage.rendersystem.shader.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.Invocable;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import net.java.games.input.Event;

import ray.physics.PhysicsEngine;
import ray.physics.PhysicsObject;
import ray.physics.PhysicsEngineFactory;

public class MyGame extends VariableFrameRateGame {
	
	//variables
	private Camera3Pcontroller orbitController1; 
	private Camera3Pcontroller3 orbitController2;
	private Action moveFwdActD1, moveFwdActD2;

	private boolean connected = false;
	private Camera camera;
	
	//HUD vars
	float elapsTime = 0.0f;
	String elapsTimeStr, counterStrA,counterStrB, dispStrA,dispStrB ;
	int elapsTimeSec, counterA, counterB = 0;
	float d1, d2, d3, dx, d4;
	float d1b, d2b, d3b, dxb, d4b;
	boolean p1v, p2v, p3v = false;
	
	//Declare Entities
	Entity dolphinE;
	Entity dolphinEb;
	
	Entity planetE1;
	Entity planetE2;
	Entity planetE3;
	Entity ufoE;
	Entity hexE;
	
	//Declare render system
	GL4RenderSystem rs;
	
	//Declare Scene Nodes
	
	private SceneNode 			dolphinN;
	
	private SceneNode			dolphinNb;
	
	private SceneNode 			cameraN;	
	
	private SceneNode 			planetN1;
	private SceneNode 			planetN2;
	private SceneNode 			planetN3;
	private SceneNode			povN;
	private SceneNode           activeNode;
	private SceneNode			cubeN;
		
	private SceneNode           cameraPositionNode;
	private SceneNode           cameraYawNode;
	private SceneNode           cameraPitchNode;
	private SceneNode           cameraRollNode;
	
	private SceneNode           dolphinPositionNode;
	private SceneNode           dolphinYawNode;
	private SceneNode           dolphinPitchNode;
	private SceneNode           dolphinRollNode;
	
	private SceneNode           dolphinPositionNodeb;
	private SceneNode           dolphinYawNodeb;
	private SceneNode           dolphinPitchNodeb;
	private SceneNode           dolphinRollNodeb;
	
	//Declare Input Manager
	private InputManager im;
	
	
	private Action moveForwardAction,
					rotateLeftAction, rotateRightAction,
				   rotateUpAction, rotateDownAction,
				  moveBackwardAction,
				     moveRightAction,
					  moveLeftAction,
				       cameraToggleAction, 
				       rotateRollAction, rotateRollnegAction, 
				       gamepadXaxe, gamepadYaxe, 
				       gamepadRX, gamepadRY;
	
	public char cMode='c';
	
	private float speed = 0.1f;

	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private boolean isClientConnected;
	private Vector<UUID> gameObjectsToRemove;	
	private File scriptFile1;
	private File scriptFile2;
	private ScriptEngine jsEngine;
	private long fileLastModifiedTime = 0;
	
	
	//physics variables
	private SceneNode ball1Node, ball2Node, gndNode;
	
	private final static String GROUND_E = "Ground";
	private final static String GROUND_N = "GroundNode";
	private PhysicsEngine physicsEng; 
	private PhysicsObject ball1PhysObj, ball2PhysObj, gndPlaneP;
	private boolean running = false;
	
	public MyGame() {
		super();
	}
	public MyGame(String serverAddr, String sPort, String arg3)
	{ 
		super();
		this.serverAddress = serverAddr;
		this.serverPort = Integer.parseInt(sPort);
		this.serverProtocol = ProtocolType.UDP;
	}
	
	public static void main(String[] args) {
		
		MyGame game = new MyGame("130.86.65.49", "80", "");
		
		 ScriptEngineManager factory = new ScriptEngineManager();
		 String scriptFileName = "hello.js";
		 // get a list of the script engines on this platform
		 //List<ScriptEngineFactory> list = factory.getEngineFactories();
		 List<ScriptEngineFactory> list = factory.getEngineFactories();
		 System.out.println("Script Engine Factories found:");
		 for (ScriptEngineFactory f : list)
		 { System.out.println(" Name = " + f.getEngineName()
		 + " language = " + f.getLanguageName()
		 + " extensions = " + f.getExtensions());
		 }
		 // get the JavaScript engine
		 ScriptEngine jsEngine = factory.getEngineByName("js");
		 // run the script
		 game.executeScript(jsEngine, scriptFileName);		
		
		try {
			game.startup();
			game.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			game.exit();
			game.shutdown();
		}
	}

	 private void executeScript(ScriptEngine engine, String scriptFileName)
	 {
	 try
	 { FileReader fileReader = new FileReader(scriptFileName);
	 engine.eval(fileReader); //execute the script statements in the file
	 fileReader.close();
	 }
	 catch (FileNotFoundException e1)
	 { System.out.println(scriptFileName + " not found " + e1); }
	 catch (IOException e2)
	 { System.out.println("IO problem with " + scriptFileName + e2); }
	 catch (ScriptException e3)
	 { System.out.println("ScriptException in " + scriptFileName + e3); }
	 catch (NullPointerException e4)
	 { System.out.println ("Null ptr exception in " + scriptFileName + e4); }
	 }	
	
	//Set-up the window size
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		rs.createRenderWindow(new DisplayMode(1920, 1080, 24, 60), false);
	}
	  //  now we add setting up viewports in the window  
	protected void setupWindowViewports(RenderWindow rw)  { 
		rw.addKeyListener(this);    
		Viewport topViewport = rw.getViewport(0);    
		topViewport.setDimensions(.01f, .01f, .99f, .99f);// B,L,W,H    
		topViewport.setClearColor(new Color(1.0f, .7f, .7f));  
		
		
//		Viewport botViewport = rw.createViewport(.01f, .01f, .99f, .49f);    
//		botViewport.setClearColor(new Color(.5f, 1.0f, .5f));  
		
		
		} 
	
	@Override
	protected void setupCameras(SceneManager sm, RenderWindow rw) {
		
				  
		//code from notes
		SceneNode rootNode = sm.getRootSceneNode();
		camera = sm.createCamera("MainCamera",
										Projection.PERSPECTIVE);
		rw.getViewport(0).setCamera(camera);
		SceneNode cameraN =          
				rootNode.createChildSceneNode("MainCameraNode");    
		cameraN.attachObject(camera);    
		camera.setMode('n');    
		camera.getFrustum().setFarClipDistance(1000.0f); 
		
/*		
		Camera camera2 = sm.createCamera("MainCamera2",Projection.PERSPECTIVE);    
		rw.getViewport(1).setCamera(camera2);    
		SceneNode cameraN2 =        
				rootNode.createChildSceneNode("MainCamera2Node");    
		cameraN2.attachObject(camera2);    
		camera2.setMode('n');    
		camera2.getFrustum().setFarClipDistance(1000.0f);
		*/
		
		
	}
	protected void setupOrbitCameras(Engine engine, SceneManager sm) {
		SceneNode playerNodeA = sm.getSceneNode("myDolphinNode");
		SceneNode playerCnodeA = sm.getSceneNode("MainCameraNode");
		Camera cameraA = sm.getCamera("MainCamera");
		
		//String controlNameA = im.getKeyboardName();
		String controlNameA = "PC/AT Enhanced PS/2 Keyboard (101/102-Key)";
		
		orbitController2 = new Camera3Pcontroller3(cameraA, playerCnodeA, playerNodeA, controlNameA, im);
		
		/*
		SceneNode playerNodeB = sm.getSceneNode("myDolphinbNode");
		SceneNode playerCnodeB = sm.getSceneNode("MainCamera2Node");
		Camera cameraB = sm.getCamera("MainCamera2");
		String controlNameB = im.getKeyboardName();
		orbitController2 = new Camera3Pcontroller3(cameraB, playerCnodeB, playerNodeB, controlNameB, im);
		*/
	}

	private void setupNetworking()
	{ 
		/*
		NetworkingServer app =
				new NetworkingServer(80, "UDP");
		*/
		gameObjectsToRemove = new Vector<UUID>();
		isClientConnected = false;
		try
		{
			protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
		} 
		catch (UnknownHostException e)
		{ 
			e.printStackTrace();
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
		if (protClient == null)
		{ 
			System.out.println("missing protocol host"); 
		}
		else
		{ // ask client protocol to send initial join message
			//to server, with a unique identifier for this client
			protClient.sendJoinMessage();
		}
	}
	
	
	protected void processNetworking(float elapsTime, SceneManager sm)
	{ 
		// Process packets received by the client from the server
		if (protClient != null)
			protClient.processPackets();
		// remove ghost avatars for players who have left the game
		Iterator<UUID> it = gameObjectsToRemove.iterator();
		while(it.hasNext())
		{
			sm.destroySceneNode(it.next().toString());
		}
		gameObjectsToRemove.clear();
	}
	

	public void addGhostAvatarToGameWorld(GhostAvatar avatar)throws IOException
	{ 
		SceneManager sm = this.getEngine().getSceneManager();
		if (avatar != null)
		{ 
			//Entity ghostE = sm.createEntity("ghost" + avatar.getID().toString(), "dolphinHighPoly.obj");//
			Entity ghostE = sm.createEntity("ghost" + avatar.getID().toString(), "acorn.obj");
			
			
			ghostE.setPrimitive(Primitive.TRIANGLES);
			SceneNode ghostN = sm.getRootSceneNode().
			createChildSceneNode(avatar.getID().toString());
			ghostN.attachObject(ghostE);
			ghostN.setLocalPosition(avatar.getPosition());
			avatar.setNode(ghostN);
			avatar.setEntity(ghostE);
			//avatar.setPosition(node’s position... maybe redundant);
		} 
	}
	
	public void removeGhostAvatarFromGameWorld(GhostAvatar avatar)
	{ 
		if(avatar != null) gameObjectsToRemove.add(avatar.getID());
	}
	
	
	private class SendCloseConnectionPacketAction extends AbstractInputAction
	{ 
		// for leaving the game... need to attach to an input device
		//public void performAction(float time, Event evt)
		@Override
		public void performAction(float time,Event evt)
		{ 
			if(protClient != null && isClientConnected == true)
			{ 
				protClient.sendByeMessage();
			} 
		}

		/*@Override
		public void performAction(float arg0, net.java.games.input.Event arg1) {
			// TODO Auto-generated method stub
			
		} */
	}
		
	public void setupScript1( SceneManager sm)
	{
		
		//File scriptFile1 = null;
		
		long time = 0;;
		
		//ScriptEngine jsEngine;
		ScriptEngineManager factory = new ScriptEngineManager();
		java.util.List<ScriptEngineFactory> list = factory.getEngineFactories();
		jsEngine = factory.getEngineByName("js");
		// use spin speed setting from the first script to initialize dolphin rotation
		scriptFile1 = new File("InitParams.js");
		//scriptFile1.setLastModified(time );
		this.runScript(scriptFile1);
		
		//remove line below to make the default speed the speed in the java script
		fileLastModifiedTime = scriptFile1.lastModified(); 
		
		//this.speed = (double)jsEngine.get("speed");
		
		
		//rc = new RotationController(Vector3f.createUnitVectorY(),
		//((Double)(jsEngine.get("spinSpeed"))).floatValue());
		//rc.addNode(dolphinN);
		//sm.addController(rc);
		// add the light specified in the second script to the game world
		
		
		scriptFile2 = new File("CreateLight.js");
		jsEngine.put("sm", sm);
		this.runScript(scriptFile2);
		SceneNode plightNode2 =
		sm.getRootSceneNode().createChildSceneNode("plightNode2");
		plightNode2.attachObject((Light)jsEngine.get("plight"));
	}
	
	private void runScript(File scriptFile)
	{ 
		try
		{ FileReader fileReader = new FileReader(scriptFile);
		jsEngine.eval(fileReader);
		fileReader.close();
		}
		catch (FileNotFoundException e1)
		{ 	
			System.out.println(scriptFile + " not found " + e1); 
		}
			catch (IOException e2)
		{ 
				System.out.println("IO problem with " + scriptFile + e2); 
		}
				catch (ScriptException e3)
	 { System.out.println("Script Exception in " + scriptFile + e3); }
	 catch (NullPointerException e4)
	 { System.out.println ("Null ptr exception reading " + scriptFile + e4); }
	 }
	
		
	
	@Override
	protected void setupScene(Engine engine, SceneManager sm) throws IOException {
        setupScript1(sm);
		setupNetworking();
		setupSkyBox(engine, sm);
		sm.getAmbientLight().setIntensity(new Color(.05f, .05f, .05f));
		
		setupDolphin(engine, sm);
		
        setupPlanets(engine, sm);
        setupAxis(engine, sm);
        setupCube(engine, sm);
        
        setupGroundPlane(engine, sm);
        
        //activeNode = this.getEngine().getSceneManager().getSceneNode("MainCameraNode");
        
        Light plight = sm.createLight("testLamp1", Light.Type.POINT);
		plight.setAmbient(new Color(.5f, .5f, .5f));
        plight.setDiffuse(new Color(.7f, .7f, .7f));
		plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        plight.setRange(10f);
        
        Light dlight = sm.createLight("Directional Light", Light.Type.DIRECTIONAL);
        dlight.setAmbient(new Color(.8f, .8f, .8f));
        dlight.setDiffuse(new Color(.7f, .7f, .7f));
		dlight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        dlight.setRange(20f);
        
		SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
        plightNode.attachObject(plight);
        plightNode.attachObject(dlight);
        
        RotationController rc = new RotationController(Vector3f.createUnitVectorY(), .02f);
        rc.addNode(planetN1);
        rc.addNode(planetN2);
        rc.addNode(planetN3);
        
        //StretchController sc = new StretchController();
        //sc.addNode(planetN1);
        //sc.addNode(planetN2);
        //sc.addNode(planetN3);
        
        //sm.addController(sc);
        sm.addController(rc);
        //NodeControllers nc1 = new NodeControllers();
        //nc1.addNode(planetN1);
        //sm.addController(nc1);
        setupInputs();
        setupOrbitCameras(engine, sm);
		
		//terrain
		
		//terrain
		Tessellation tessE = sm.createTessellation("tessE", 6);
		tessE.setSubdivisions(8f);
		SceneNode tessN = sm.getRootSceneNode().createChildSceneNode("tessN");
		tessN.attachObject(tessE); 
		tessN.scale(100, 30, 100); 
		tessE.setHeightMap(this.getEngine(), "scribble.jpeg");
		tessE.setTexture(this.getEngine(), "green.jpg");
		
		
// physics
		SceneNode rootNode = sm.getRootSceneNode();
		// Ball 1 
		Entity ball1Entity = sm.createEntity("ball1", "acorn.obj"); 
		ball1Node = rootNode.createChildSceneNode("Ball1Node");
		ball1Node.attachObject(ball1Entity);
		ball1Node.setLocalPosition(5, 8, 2);
		// Ball 2
		
		Entity ball2Entity = sm.createEntity("ball2", "acorn.obj");
		ball2Node = rootNode.createChildSceneNode("Ball2Node");
		ball2Node.attachObject(ball2Entity);
		ball2Node.setLocalPosition(-3, 5,-2);
		// Ground plane
		Entity groundEntity = sm.createEntity(GROUND_E,  "cube.obj");
		gndNode = rootNode.createChildSceneNode(GROUND_N);
		gndNode.attachObject(groundEntity);
		gndNode.setLocalPosition(0, 0, -3);
		Texture texG = engine.getTextureManager().getAssetByPath("brown.jpeg");
		TextureState texStateG = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		texStateG.setTexture(texG);
		FrontFaceState faceStateG = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
		groundEntity.setRenderState(texStateG);
		groundEntity.setRenderState(faceStateG);
		
		initPhysicsSystem();
		createRagePhysicsWorld();
		System.out.println("Press SPACE to start the physics engine!");
	}

	private void createRagePhysicsWorld() {
		float mass = 1.0f;
		float up[] = {0,1,0};
		double[] temptf; 
		
		temptf = toDoubleArray(gndNode.getLocalTransform().toFloatArray());
		gndPlaneP = physicsEng.addStaticPlaneObject(physicsEng.nextUID(), temptf, up, 0.0f);
		gndPlaneP.setBounciness(1.0f);
		gndNode.scale(100f, .05f, 100f);
		gndNode.setLocalPosition(0, 0, 0);
		gndNode.setPhysicsObject(gndPlaneP);
		temptf = toDoubleArray(ball1Node.getLocalTransform().toFloatArray());
		ball1PhysObj = physicsEng.addSphereObject(physicsEng.nextUID(),
				mass, temptf, 0.0f);
		ball1PhysObj.setBounciness(1.0f);
		ball1Node.setPhysicsObject(ball1PhysObj);
		temptf = toDoubleArray(ball2Node.getLocalTransform().toFloatArray());
		ball2PhysObj = physicsEng.addSphereObject(physicsEng.nextUID(),
				mass, temptf, 0.0f);
		ball2PhysObj.setBounciness(0.60f);
		ball2Node.setPhysicsObject(ball2PhysObj);
		
		// can also set damping, friction, etc.
	}
	private void initPhysicsSystem() {
		String engine = "ray.physics.JBullet.JBulletPhysicsEngine";
		float[] gravity = {0, -3f, 0};
		physicsEng = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEng.initSystem();
		physicsEng.setGravity(gravity);
		
	}
	@Override
	protected void update(Engine engine) {
		rs = (GL4RenderSystem) engine.getRenderSystem();
		rs.getRenderWindow().getHeight();
		elapsTime += engine.getElapsedTimeMillis();
		elapsTimeSec = Math.round(elapsTime/1000.0f);
		elapsTimeStr = Integer.toString(elapsTimeSec);
		float time = engine.getElapsedTimeMillis();
		
		//d4 = getDistanceBetween(dolphinN, cubeN);

		
		counterStrA = Integer.toString(counterA);
	//	counterStrB = Integer.toString(counterB);
	//	dispStrB = "Time = " + elapsTimeStr + "     Planet Discovered = " + counterStrB;
	//	rs.setHUD(dispStrB, 15, 15);		
		dispStrA = "Time = " + elapsTimeStr + "     Planet Discovered = " + counterStrA;
		rs.setHUD2(dispStrA, 15, rs.getRenderWindow().getViewport(0).getActualBottom() + 5);
		im.update(elapsTime);
		
	
		d1 = getDistanceBetween(dolphinN, planetN1);
		d2 = getDistanceBetween(dolphinN, planetN2);
		d3 = getDistanceBetween(dolphinN, planetN3);
		
//		d1b = getDistanceBetween(dolphinNb, planetN1);
//		d2b = getDistanceBetween(dolphinNb, planetN2);
//		d3b = getDistanceBetween(dolphinNb, planetN3);
		if (d1 <= 1.0f && p1v==false){counterA++; p1v = true;
		FloatUpController xyz = new FloatUpController();
		xyz.addNode(planetN1);
		engine.getSceneManager().addController(xyz);
		}
		if (d2 <= 1.3f && p2v==false){counterA++; p2v = true;
		FloatUpController abc = new FloatUpController();
		abc.addNode(planetN2);
		engine.getSceneManager().addController(abc);
		}
		if (d3 <= 2.0f && p3v==false){counterA++; p3v = true;
		FloatUpController xyzq = new FloatUpController();
		xyzq.addNode(planetN3);
		engine.getSceneManager().addController(xyzq);
		//physics update
		}
		if (running){
			Matrix4 mat;
			physicsEng.update(time);
			for (SceneNode s : engine.getSceneManager().getSceneNodes())
			{   
				if (s.getPhysicsObject() != null)
				{   
					mat = Matrix4f.createFrom(toFloatArray(s.getPhysicsObject().getTransform()));
					s.setLocalPosition(mat.value(0,3),mat.value(1,3),mat.value(2,3));
				}
			}
		}
		
	/*	
		if (d1b <= 1.0f && p1v==false){counterB++; p1v = true;
		StretchController ab1 = new StretchController();
		ab1.addNode(planetN1);
		engine.getSceneManager().addController(ab1);
;
		}
		if (d2b <= 1.3f && p2v==false){counterB++; p2v = true;
		StretchController abx1 = new StretchController();
		abx1.addNode(planetN2);
		engine.getSceneManager().addController(abx1);
}
		if (d3b <= 2.0f && p3v==false){counterB++; p3v = true;
		StretchController yab1 = new StretchController();
		yab1.addNode(planetN3);
		engine.getSceneManager().addController(yab1);
}
*/
		
		
		//orbitController1.updateCameraPosition();
		orbitController2.updateCameraPosition();
		processNetworking(elapsTime, engine.getSceneManager());
		//fileLastModifiedTime = scriptFile1.lastModified(); 
		//run script
		//System.out.println("scriptFile1.lastModified(): " + scriptFile1.lastModified());
		long modTime = (long) scriptFile1.lastModified();
		if (modTime > fileLastModifiedTime)
		{ 
			System.out.println("Accessing script file");
		this.runScript(scriptFile1);
		this.speed = (Float)jsEngine.get("speed");
		fileLastModifiedTime = modTime;
		if (this.speed > 1){this.speed = 1;}
		//this.setSpeed(((Double)(jsEngine.get("speed")));
		}
		
	}
	
	
	
	protected void setupCube(Engine engine, SceneManager sm) throws IOException { 
		ManualObject cube = sm.createManualObject("Cube");
		
		 ManualObjectSection cfront = cube.createManualSection("cubeFront");   
		 ManualObjectSection cback = cube.createManualSection("cubeBack");
	     ManualObjectSection cleft = cube.createManualSection("cubeLeft");
	     ManualObjectSection cright = cube.createManualSection("cubeRight");
	     ManualObjectSection ctop = cube.createManualSection("cubeTop");
	     ManualObjectSection cbottom = cube.createManualSection("cubeBottom");

		cube.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		//////////Vertices
		float[] verticesFront = new float[] {
				-1f,  1f, 1f,
	            -1f, -1f, 1f,
	             1f, -1f, 1f,
	             1f,  1f, 1f
		};
		
		cfront.setVertexBuffer(BufferUtil.directFloatBuffer(verticesFront));
		//
		float[] verticesBack = new float[] {
				-1f,  1f, 1f,
	            -1f, -1f, 1f,
	             1f, -1f, 1f,
	             1f,  1f, 1f
		};
		
		cfront.setVertexBuffer(BufferUtil.directFloatBuffer(verticesBack));
		//
		float[] verticesLeft = new float[] {
				-1f,  1f, 1f,
	            -1f, -1f, 1f,
	             1f, -1f, 1f,
	             1f,  1f, 1f
		};
		
		cfront.setVertexBuffer(BufferUtil.directFloatBuffer(verticesLeft));
				//
		float[] verticesRight = new float[] {
				-1f,  1f, 1f,
	            -1f, -1f, 1f,
	             1f, -1f, 1f,
	             1f,  1f, 1f
		};
		
		cfront.setVertexBuffer(BufferUtil.directFloatBuffer(verticesRight));
					//
		float[] verticesTop = new float[] {
				-1f,  1f, 1f,
	            -1f, -1f, 1f,
	             1f, -1f, 1f,
	             1f,  1f, 1f
		};
		
		cfront.setVertexBuffer(BufferUtil.directFloatBuffer(verticesTop));
										//bottom
		float[] verticesBottom = new float[] {
				-1f,  1f, 1f,
	            -1f, -1f, 1f,
	             1f, -1f, 1f,
	             1f,  1f, 1f
		};
		
		cfront.setVertexBuffer(BufferUtil.directFloatBuffer(verticesBottom));
		
		///Textures
		
		float[] texcoords = new float[] { 
				0, 1,
	            0, 0,
	            1, 0,
	            1, 1

		};
		cube.setTextureCoordBuffer(BufferUtil.directFloatBuffer(texcoords));
		float [] normalFNT = new float[] { 0, 0, 1 };
		float [] normalBCK = new float[] { 0, 0, 1 };
		float [] normalLFT = new float[] { 0, 0, 1 };
		float [] normalRHT = new float[] { 0, 0, 1 };
		float [] normalTOP = new float[] { 0, 0, 1 };
		float [] normalBTM = new float[] { 0, 0, 1 };
		cfront.setNormalsBuffer(BufferUtil.directFloatBuffer(normalFNT));
        cback.setNormalsBuffer(BufferUtil.directFloatBuffer(normalBCK));
        cleft.setNormalsBuffer(BufferUtil.directFloatBuffer(normalLFT));
        cright.setNormalsBuffer(BufferUtil.directFloatBuffer(normalRHT));
        ctop.setNormalsBuffer(BufferUtil.directFloatBuffer(normalTOP));
        cbottom.setNormalsBuffer(BufferUtil.directFloatBuffer(normalBTM));
        

		
		int[] indices = new int[] {0, 1, 2, 
								0, 2, 3};
		cube.setIndexBuffer(BufferUtil.directIntBuffer(indices));
		
		Material mat = engine.getMaterialManager().createManualAsset("CubeMaterial");
		Texture tex = engine.getTextureManager().getAssetByPath("earth-day.jpeg");
		TextureState texState = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		texState.setTexture(tex);
		
		FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
		
		ZBufferState zstate = (ZBufferState) sm.getRenderSystem().createRenderState(Type.ZBUFFER);
		
		cube.setDataSource(DataSource.INDEX_BUFFER);
		cube.setRenderState(texState);
		cube.setRenderState(faceState);
	    cube.setRenderState(zstate);
	    cube.setMaterial(mat);
	    
		cubeN = sm.getRootSceneNode().createChildSceneNode("FlatEarthNode");
		cubeN.attachObject(cube);
		
	}
	
	private void setupGroundPlane(Engine eng, SceneManager sm) throws IOException{
		//creates 2 triangle planes
		ManualObject tri1 = sm.createManualObject("t1");
    	ManualObjectSection triSec1 = tri1.createManualSection("t1" + "Section");
    	tri1.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
    	
    	ManualObject tri2 = sm.createManualObject("t2");
    	ManualObjectSection triSec2 = tri2.createManualSection("t2" + "Section");
    	tri2.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));

    	float[] vertices1 = new float[]{
    		150.0f, 0.0f, 0.0f, 
    		-150.0f, 0.0f, 0.0f, 
    		0.0f, 0.0f, 150.0f, 
    	};
    	float[] vertices2 = new float[]{
        		-150.0f, 0.0f, 0.0f, 
        		150.0f, 0.0f, 0.0f, 
        		0.0f, 0.0f, -150.0f, 
        	};
        	
 
    	
    	float[] texcoords = new float[]{
    
    		 0.0f, 0.0f, 1.0f, 
    		 1.0f, 0.0f, 1.0f
    		 
    	};
    	float[] normals = new float[]{ 
    		 0.0f, 1.0f, 1.0f, 
    		 0.0f, 1.0f, 1.0f, 
    		 0.0f, 1.0f, 1.0f,
    	};
    	int[] indices = new int[] { 0,1,2 };
    	
    	FloatBuffer vertBuf1 = BufferUtil.directFloatBuffer(vertices1);
    	FloatBuffer vertBuf2 = BufferUtil.directFloatBuffer(vertices2);
    	FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
    	FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
    	IntBuffer   indexBuf = BufferUtil.directIntBuffer(indices);
    	
    	triSec1.setVertexBuffer(vertBuf1);
    	triSec2.setVertexBuffer(vertBuf2);
    	triSec1.setTextureCoordsBuffer(texBuf);
    	triSec1.setNormalsBuffer(normBuf);
    	triSec1.setIndexBuffer(indexBuf);
    	triSec2.setTextureCoordsBuffer(texBuf);
    	triSec2.setNormalsBuffer(normBuf);
    	triSec2.setIndexBuffer(indexBuf);
    	
    	
    	Texture tex1 = eng.getTextureManager().getAssetByPath("brown.jpeg");
    	TextureState textState1 = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
    	textState1.setTexture(tex1);
    	Texture tex2 = eng.getTextureManager().getAssetByPath("brown.jpeg");//red
    	TextureState textState2 = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
    	textState2.setTexture(tex2);
    	
    	FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
    	faceState.setVertexWinding(FrontFaceState.VertexWinding.COUNTER_CLOCKWISE);
    	tri1.setDataSource(DataSource.INDEX_BUFFER);
    	tri1.setRenderState(textState1);
    	tri1.setRenderState(faceState);
    	
    	tri2.setDataSource(DataSource.INDEX_BUFFER);
    	tri2.setRenderState(textState2);
    	tri2.setRenderState(faceState);
    	
    	SceneNode tri1Node = sm.getRootSceneNode().createChildSceneNode("aNode");
	    tri1Node.attachObject(tri1);
	    SceneNode tri2Node = sm.getRootSceneNode().createChildSceneNode("bNode");
	    tri2Node.attachObject(tri2);
		
	}

	private void setupAxis(Engine eng, SceneManager sm) throws IOException {
		ManualObject vecX = sm.createManualObject("VecX");
		ManualObjectSection vecSecX = vecX.createManualSection("VecSectionX");
		vecX.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		
		ManualObject vecY = sm.createManualObject("VecY");
		ManualObjectSection vecSecY = vecX.createManualSection("VecSectionY");
		vecX.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		
		ManualObject vecZ = sm.createManualObject("VecZ");
		ManualObjectSection vecSecZ = vecX.createManualSection("VecSectionZ");
		vecX.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
        
        float[] verticesX = new float[] { 
    			-500.0f, 0.0f, 0.0f,
    			500.0f, 0.0f, 0.0f, 
		};
        float[] verticesY = new float[] { 
        		0.0f, -500.0f, 0.0f,
        		0.0f, 500.0f, 0.0f,
		};
        float[] verticesZ = new float[] { 
        		0.0f, 0.0f, -500.0f,
        		0.0f, 0.0f, 500.0f,
		};     
		
		int[] indicesX = new int[] { 0,1 };
		int[] indicesY = new int[] { 0,1 };
		int[] indicesZ = new int[] { 0,1 };
		
		vecSecX.setPrimitive(Primitive.LINES);
		vecSecY.setPrimitive(Primitive.LINES);
		vecSecZ.setPrimitive(Primitive.LINES);
		
		FloatBuffer vertBufX = BufferUtil.directFloatBuffer(verticesX);
		IntBuffer indexBufX = BufferUtil.directIntBuffer(indicesX);
		FloatBuffer vertBufY = BufferUtil.directFloatBuffer(verticesY);
		IntBuffer indexBufY = BufferUtil.directIntBuffer(indicesY);
		FloatBuffer vertBufZ = BufferUtil.directFloatBuffer(verticesZ);
		IntBuffer indexBufZ = BufferUtil.directIntBuffer(indicesZ);
		
		vecSecX.setVertexBuffer(vertBufX);
		vecSecX.setIndexBuffer(indexBufX);
		vecSecY.setVertexBuffer(vertBufY);
		vecSecY.setIndexBuffer(indexBufY);
		vecSecZ.setVertexBuffer(vertBufZ);
		vecSecZ.setIndexBuffer(indexBufZ);
		
		Material matX = sm.getMaterialManager().getAssetByPath("default.mtl");
	    matX.setEmissive(Color.BLUE);
	    Texture texX = sm.getTextureManager().getAssetByPath(matX.getTextureFilename());
	    TextureState tstateX = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
	    tstateX.setTexture(texX);
	    vecSecX.setRenderState(tstateX);
	    vecSecX.setMaterial(matX);
	    
	    SceneNode vecXNode = sm.getRootSceneNode().createChildSceneNode("XNode");
	    vecXNode.attachObject(vecX);
	    
	    Material matY = sm.getMaterialManager().getAssetByPath("default2.mtl");
	    matY.setEmissive(Color.RED);
	    Texture texY = sm.getTextureManager().getAssetByPath(matY.getTextureFilename());
	    TextureState tstateY = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
	    tstateY.setTexture(texY);
	    vecSecY.setRenderState(tstateY);
	    vecSecY.setMaterial(matY);
	    
	    SceneNode vecYNode = sm.getRootSceneNode().createChildSceneNode("YNode");
	    vecYNode.attachObject(vecY);
	    
	    Material matZ = sm.getMaterialManager().getAssetByPath("default3.mtl");
	    matZ.setEmissive(Color.YELLOW);
	    Texture texZ = sm.getTextureManager().getAssetByPath(matZ.getTextureFilename());
	    TextureState tstateZ = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
	    tstateZ.setTexture(texZ);
	    vecSecZ.setRenderState(tstateZ);
	    vecSecZ.setMaterial(matZ);
	    
	    SceneNode vecZNode = sm.getRootSceneNode().createChildSceneNode("ZNode");
	    vecZNode.attachObject(vecZ);
	}
	
	
	
	
	private void setupDolphin(Engine engine, SceneManager sm) throws IOException {
		
		
		//dolphinE = sm.createEntity("myDolphin", "dolphinHighPoly.obj");
		dolphinE = sm.createEntity("myDolphin", "acorn.obj");
		dolphinE.setPrimitive(Primitive.TRIANGLES);
		dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
		dolphinN.moveForward(0.0f);
		dolphinN.attachObject(dolphinE); 
		
		Texture tex1 = engine.getTextureManager().getAssetByPath("acorn.png");
		TextureState texState1 = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		texState1.setTexture(tex1);
		FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
	
		//dolphinEb = sm.createEntity("myDolphinb", "dolphinHighPoly.obj");
		//dolphinEb = sm.createEntity("myDolphinb", "acorn.obj");
		//dolphinEb.setPrimitive(Primitive.TRIANGLES);
		//dolphinNb = sm.getRootSceneNode().createChildSceneNode(dolphinEb.getName() + "Node");
		//dolphinNb.moveForward(0.0f);
		//dolphinNb.moveRight(0.0f);
		//dolphinNb.attachObject(dolphinEb); 
		dolphinE.setRenderState(texState1);
		//dolphinE.setRenderState(faceState);
		dolphinN.setLocalScale(0.59f, 0.59f, 0.59f);
		
		
	}
	
	
	
	private void setupPlanets(Engine engine, SceneManager sm) throws IOException {
		SceneNode pN = sm.getRootSceneNode().createChildSceneNode("myPN");
		planetE1 = sm.createEntity("myPlanet", "acorn.obj");
		planetE1.setPrimitive(Primitive.TRIANGLES);
		//planetN1 = sm.getRootSceneNode().createChildSceneNode(planetE1.getName() + "Node");
		planetN1 = pN.createChildSceneNode("PNode");
		SceneNode cubenode = sm.getRootSceneNode().createChildSceneNode("myCubeN");
		
		Entity cb1 = sm.createEntity("satel", "cube.obj");
		Texture texG = engine.getTextureManager().getAssetByPath("brown.jpeg");
		TextureState texStateG = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		texStateG.setTexture(texG);
		FrontFaceState faceStateG = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
		cb1.setRenderState(texStateG);
		cb1.setRenderState(faceStateG);
		
		cb1.setPrimitive(Primitive.TRIANGLES);
		Texture tex1 = engine.getTextureManager().getAssetByPath("acorn.png");
		TextureState texState1 = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		texState1.setTexture(tex1);
		FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
		planetE1.setRenderState(texState1);
		planetE1.setRenderState(faceState);
		
		planetN1.moveForward(getRandomNumber());
		
		planetN1.moveRight(getRandomNumber());
		planetN1.attachObject(planetE1);
		planetN1.attachObject(cb1);
		planetN1.rotate(Degreef.createFrom(90f), Vector3f.createUnitVectorX());
		
		
		////
		planetE2 = sm.createEntity("myPlanet2", "acorn.obj");
		planetE2.setPrimitive(Primitive.TRIANGLES);
		planetN2 = sm.getRootSceneNode().createChildSceneNode(planetE2.getName() + "Node");
		
		
		Texture tex2 = engine.getTextureManager().getAssetByPath("acorn.png");
		TextureState texState2 = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		texState2.setTexture(tex2);
		FrontFaceState faceState2 = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
		planetE2.setRenderState(texState2);
		planetE2.setRenderState(faceState2);
		
		planetN2.moveRight(getRandomNumber());
		//planetN2.moveUp(4f);
		planetN2.moveForward(getRandomNumber());
		planetN2.attachObject(planetE2); 
		planetN2.rotate(Degreef.createFrom(90f), Vector3f.createUnitVectorX());
	///
		planetE3 = sm.createEntity("myPlanet3", "acorn.obj");
		planetE3.setPrimitive(Primitive.TRIANGLES);
		planetN3 = sm.getRootSceneNode().createChildSceneNode(planetE3.getName() + "Node");
		
		
		Texture tex3 = engine.getTextureManager().getAssetByPath("acorn.png");
		TextureState texState3 = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		texState3.setTexture(tex3);
		FrontFaceState faceState3 = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
		planetE3.setRenderState(texState3);
		planetE3.setRenderState(faceState3);
		
		//planetN3.moveDown(-5f);
		planetN3.moveForward(getRandomNumber());
		planetN3.moveRight(getRandomNumber());
		planetN3.attachObject(planetE3); 
		planetN3.rotate(Degreef.createFrom(90f), Vector3f.createUnitVectorX());
		cubenode.setLocalPosition(3f,0f,0f);
		planetN3.setLocalScale(2.0f, 2.0f, 2.0f);
		planetN2.setLocalScale(1.3f, 1.3f, 1.3f);
		planetN1.setLocalScale(4.f, 4f, 4.0f);
		
		
	}
 
	public void setupInputs() { 
		im = new GenericInputManager();
		//default for computers in lab
		//String kbName = im.getKeyboardName();
		
		String gpName = im.getFirstGamepadName();

		//laptop only
		String kbName = "PC/AT Enhanced PS/2 Keyboard (101/102-Key)";
		System.out.println(kbName);
		//cameraToggleAction = new CameraModeToggleAction(this);
		
		//Movement 
		
		moveForwardAction = new MoveForwardAction(this, protClient, dolphinN);
		moveBackwardAction = new MoveBackwardAction(this, protClient, dolphinN);
		moveRightAction = new MoveRightAction(this, protClient, dolphinN);
		moveLeftAction = new MoveLeftAction(this, protClient, dolphinN);
		 
		//rotateUpAction = new RotateUpAction(this); 
		rotateLeftAction = new RotateLeftAction(this, protClient);
		rotateRightAction = new RotateRightAction(this, protClient);
		//rotateDownAction = new RotateDownAction(this);
		
		//rotateRollAction = new RotateRollAction(this);
		//rotateRollnegAction = new RotateRollnegAction(this);
		
		gamepadXaxe = new ControllerXaxis(dolphinN);
		gamepadYaxe = new ControllerYaxis(dolphinN);
		//gamepadRX = new ControllerRXaxis(this);
		//gamepadRY = new ControllerRYaxis(this);
//		Action gRot1 = new RotateLeftAction(dolphinN);						// temporarily commented out
//		Action gRot2 = new RotateRightAction(dolphinN);							
		

// default keyboard
		//attach or detatch camera to dolphin node
				//im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.SPACE, cameraToggleAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// movements		
				im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.W, moveForwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.D, moveLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.S, moveBackwardAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A, moveRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
				im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.Q, rotateLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.E, rotateRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				
//////////////////////////////////////////////////////////////////////////////				
				// game pad controller /
				
				if(gpName != null) {
				//im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._2, cameraToggleAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// movements		
				im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.X, gamepadXaxe, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.Y, gamepadYaxe, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		//rotations
				
		//		im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._3, gRot1, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		//		im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._4, gRot2, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			///
				//
				/*/ extra activity
				im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._5, rotateRollnegAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(gpName, net.java.games.input.Component.Identifier.Button._6, rotateRollAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				*///
				}
	}
	
	
	
	public SceneNode getActiveNode() {
		return activeNode;
	}
	
	public void setActiveNode(SceneNode a) {
		activeNode = a;
	}

	public void setMoveSpeed(float s) {
		this.speed = s;
	}
	
	public float getMoveSpeed () {
		return this.speed;
	}
	
	
	public float getRandomNumber(){
		float max = 15;
		float min = -15;
		float x = (float) (Math.random() * ((max - min) + 1)) + min;
		return x;
	}
	public float getDistanceBetween(SceneNode a, SceneNode b) {
		float ax = a.getWorldPosition().x();
		float ay = a.getWorldPosition().y();
		float az = a.getWorldPosition().z();
		
		float bx = b.getWorldPosition().x();
		float by = b.getWorldPosition().y();
		float bz = b.getWorldPosition().z();
		
		return (float)Math.sqrt((double)(ax-bx)*(ax-bx) + (ay-by)*(ay-by) + (az-bz)*(az-bz));
	}

	
	public float distanceTo(Vector3 a, Vector3 b) {
		float ax = a.x();
		float ay = a.y();
		float az = a.z();
		float bx = b.x();
		float by = b.y();
		float bz = b.z();
		
		return (float)Math.sqrt((ax-bx)*(ax-bx) + (ay-by)*(ay-by) + (az-bz)*(az-bz));
	}	

	public Camera getCamera() {
		return camera;
	}


		public char getCameraMode() {
			return cMode;
		}
		public void setCameraMode(char newMode) {
			cMode = newMode;
		}
		
		public void setIsConnected(boolean connected)
		{
			this.connected = connected;
		}

		
		 private void setupSkyBox(Engine engine, SceneManager sm) throws IOException {
		        Configuration conf = engine.getConfiguration();
		        TextureManager textureMgr = engine.getTextureManager();

		        //textureMgr.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path"));
		        textureMgr.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path"));
		        //textureMgr.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path = assets/skyboxes/oga/heaven"));
		        //Texture front = textureMgr.getAssetByPath("assets.skyboxes.oga.asteroids.front.png");
		        
		        /*Texture front = textureMgr.getAssetByPath("front.png");
		        Texture back = textureMgr.getAssetByPath("back.png");
		        Texture left = textureMgr.getAssetByPath("left.png");
		        Texture right = textureMgr.getAssetByPath("right.png");
		        Texture top = textureMgr.getAssetByPath("top.png");
		        Texture bottom = textureMgr.getAssetByPath("bottom.png");*/
		        
		        Texture front = textureMgr.getAssetByPath("front.jpeg");
		        Texture back = textureMgr.getAssetByPath("back.jpeg");
		        Texture left = textureMgr.getAssetByPath("left.jpeg");
		        Texture right = textureMgr.getAssetByPath("right.jpeg");
		        Texture top = textureMgr.getAssetByPath("top.jpeg");
		        Texture bottom = textureMgr.getAssetByPath("bottom.jpeg");
		        //textureMgr.setBaseDirectoryPath(conf.valueOf("assets.textures.path"));
		        textureMgr.setBaseDirectoryPath(conf.valueOf("assets.textures.path"));

		        // cubemap textures must be flipped up-side-down to face inward; all
		        // textures must have the same dimensions, so any image height will do
		        AffineTransform xform = new AffineTransform();
		        xform.translate(0, front.getImage().getHeight());
		        xform.scale(1d, -1d);

		        front.transform(xform);
		        back.transform(xform);
		        left.transform(xform);
		        right.transform(xform);
		        top.transform(xform);
		        bottom.transform(xform);

		        
		        SkyBox sb = sm.createSkyBox("SkyBox");
		        sb.setTexture(front, SkyBox.Face.FRONT);
		        sb.setTexture(back, SkyBox.Face.BACK);
		        sb.setTexture(left, SkyBox.Face.LEFT);
		        sb.setTexture(right, SkyBox.Face.RIGHT);
		        sb.setTexture(top, SkyBox.Face.TOP);
		        sb.setTexture(bottom, SkyBox.Face.BOTTOM);
		        sm.setActiveSkyBox(sb);
		    }
						
		
		public Vector3 getPlayerPosition()
		{ 
			SceneManager sm = this.getEngine().getSceneManager();
			SceneNode dolphinN = sm.getSceneNode("myDolphinNode");
			return dolphinN.getWorldPosition();
		}
		public void updateVerticalPosition()
		{
			SceneNode dolphinN =
					this.getEngine().getSceneManager().
					getSceneNode("myDolphinNode");
			SceneNode tessN =
					this.getEngine().getSceneManager().
					getSceneNode("tessN");
			Tessellation tessE = ((Tessellation) tessN.getAttachedObject("tessE"));
			
			Vector3 worldAvatarPosition = dolphinN.getWorldPosition();
			Vector3 localAvatarPosition = dolphinN.getLocalPosition();
			
			Vector3 newAvatarPosition = Vector3f.createFrom(
					localAvatarPosition.x(),
					tessE.getWorldHeight(
							worldAvatarPosition.x(),
							worldAvatarPosition.z()),
					
					localAvatarPosition.z()
					); 
			dolphinN.setLocalPosition(newAvatarPosition);	
		}		
		private float[] toFloatArray(double[] arr){   
			if (arr == null) return null;
			int n = arr.length;
			float[] ret = new float[n];
			for (int i = 0; i < n; i++){   ret[i] = (float)arr[i];} 
			return ret;} 
		private double[] toDoubleArray(float[] arr){   
			if (arr == null) return null;
			int n = arr.length;
			double[] ret = new double[n];
			for (int i = 0; i < n; i++){   ret[i] = (double)arr[i];} 
			return ret;} 

		//example
		public void keyPressed(KeyEvent e){   switch (e.getKeyCode())    {   case KeyEvent.VK_SPACE:System.out.println("Starting Physics!");running = true;break;} super.keyPressed(e);} 
}

