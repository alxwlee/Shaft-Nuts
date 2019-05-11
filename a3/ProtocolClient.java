package a3;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.Vector;

import javax.vecmath.Tuple3f;
//import javax.vecmath.Vector3f;

import graphicslib3D.Vector3D;
import ray.networking.client.GameConnectionClient;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

//RAGE client side protocol: 	 //  UDP example protocol 

public class ProtocolClient extends GameConnectionClient
{
	private MyGame game;
	private UUID id;
	private Vector<GhostAvatar> ghostAvatars;
	
	public ProtocolClient(InetAddress remAddr, int remPort,
			ProtocolType pType, MyGame game) throws IOException
	{ 
		super(remAddr, remPort, pType);
		this.game = game;
		this.id = UUID.randomUUID();
		this.ghostAvatars = new Vector<GhostAvatar>();
	}
	
	@Override
	protected void processPacket(Object msg)
	{ 
		//String strMessage = (String)message;
		String strMessage = (String)msg;
		String[] messageTokens = strMessage.split(",");
		if(messageTokens.length > 0)
		{
			if(messageTokens[0].compareTo("join") == 0) // receive “join”
			{ // format: join, success or join, failure
				if(messageTokens[1].compareTo("success") == 0)
				{   System.out.println("Client Connected Successfully!");
					game.setIsConnected(true);
					sendCreateMessage(game.getPlayerPosition());
				}
				if(messageTokens[1].compareTo("failure") == 0)
				{
					game.setIsConnected(false);
				}
			} 
		}
		if(messageTokens[0].compareTo("bye") == 0) // receive “bye”
		{ // format: bye, remoteId
			UUID ghostID = UUID.fromString(messageTokens[1]);
			removeGhostAvatar(ghostID);
		}
		if ((messageTokens[0].compareTo("dsfr") == 0 ) // receive “dsfr”
				|| (messageTokens[0].compareTo("create")==0))
		{ // format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z
			UUID ghostID = UUID.fromString(messageTokens[1]);
			Vector3 ghostPosition = Vector3f.createFrom(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
			try
			{ 
				createGhostAvatar(ghostID, ghostPosition);
			} 
			catch (IOException e)
			{ System.out.println("error creating ghost avatar");
			} 
		}
		if(messageTokens[0].compareTo("wsds") == 0) // rec. “create…”
		{ // etc….. 
			
		}
		if(messageTokens[0].compareTo("wsds") == 0) // rec. “wants…”
		{ // etc….. 
	
		}
		if(messageTokens[0].compareTo("move") == 0) // rec. “move...”
		{ // etc….. 
			System.out.println("I'm being told to move a ghost avatar");
			
			Vector3 newghostPosition = Vector3f.createFrom( 
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
			
			game.getEngine().getSceneManager().getSceneNode(messageTokens[1]).setLocalPosition(newghostPosition);
		}
		
		if(messageTokens[0].compareTo("rot") == 0) // rec. “move...”
		{ // etc….. 
			System.out.println("I'm being told to rotate a ghost avatar");
			
			float rotAmt =0;
			if(messageTokens[2].compareTo("right") == 0)
				rotAmt = -3f;
			
			if(messageTokens[2].compareTo("left") == 0)
				rotAmt = 3f;
			
			//System.out.println(arg0);
			//game.getEngine().getSceneManager().getSceneNode("myDolphin2Node").moveForward(game.getMoveSpeed());
			//game.getEngine().getSceneManager().getSceneNode("myDolphinNode").setLocalPosition(newghostPosition);
			game.getEngine().getSceneManager().getSceneNode(messageTokens[1]).yaw(Degreef.createFrom(rotAmt));
		}
	} 
	
/////////////////////////////////	
	public void removeGhostAvatar(UUID ghostID) 
	{
		
	}
	
	public void sendJoinMessage() // format: join, localId
	{ 
		try
		{ 
			sendPacket(new String("join," + id.toString()));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void sendCreateMessage(Vector3 pos)
	{ 
		// format: (create, localId, x,y,z)
		try
		{ 
			System.out.println("Create message sent from client");
			String message = new String("create," + id.toString());
			//message += "," + pos.getX()+"," + (pos).getY() + "," + pos.getZ();
			message += "," + pos.x()+"," + (pos).y() + "," + pos.z();
			sendPacket(message);
		}
		catch (IOException e) 	
		{ 
			e.printStackTrace();
		} 
		
		
	}
	
	public void sendByeMessage()
	{ // etc….. 
		
	}
	public void sendDetailsForMessage(UUID remId, Vector3D pos)
	{ // etc….. 
		
	}
	public void sendMoveMessage(Vector3 pos)
	{ // etc….. 
		try
		 {  System.out.println("Move message sent from client");
		  //String message = new String("move," + id.toString() + ",F");
		  String message = new String("move," + id.toString());
		  //message += "," + pos.getX()+"," + (pos).getY() + "," + pos.getZ();
		  message += "," + pos.x()+"," + (pos).y() + "," + pos.z();
		  sendPacket(message);
		  
		 }
		catch (IOException e)  
		{ 
		 e.printStackTrace();
		} 
	}
	
	public void sendRotateMessage(String direction)//float rotAmount
	{ // etc….. 
		
		SceneNode sn= game.getEngine().getSceneManager().getSceneNode("myDolphinNode");
		//sn.
		
		try
		 {  System.out.println("Rotate message sent from client");
		  //String message = new String("move," + id.toString() + ",F");
		  String message = new String("rot," + id.toString() + "," + direction);
		  //message += "," + pos.getX()+"," + (pos).getY() + "," + pos.getZ();
		  //message += "," + pos.x()+"," + (pos).y() + "," + pos.z();
		  sendPacket(message);
		  
		 }
		catch (IOException e)  
		{ 
		 e.printStackTrace();
		} 
	}
	
	public void createGhostAvatar(UUID id, Vector3 position) throws IOException
	{
		System.out.println("Creating ghost avatar for new client" );
		GhostAvatar ga = new GhostAvatar(id,position);
		this.ghostAvatars.add(ga);
		game.addGhostAvatarToGameWorld(ga);
		
	}
	/*public class GhostAvatar
	{ 
		private UUID id;
		private SceneNode node;
		private Entity entity;
		public GhostAvatar(UUID id, Vector3 position)
		{ 
			this.id = id;
		}
		public UUID getID()
		{
			return id;
		}
		
		public void setNode(SceneNode node)
		{
			this.node = node;
		}
		
		public void setEntity(Entity entity)
		{
			this.entity = entity;
		}
	// accessors and setters for id, node, entity, and position
	//. . .
	}*/
}
//Also need functions to instantiate ghost avatar, remove a ghost avatar,
//look up a ghost in the ghost table, update a ghost’s position, and
//accessors as needed.