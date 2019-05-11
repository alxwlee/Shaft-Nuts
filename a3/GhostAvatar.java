package a3;

import java.util.UUID;

import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;

public class GhostAvatar
{ 
	private UUID id;
	private SceneNode node;
	private Entity entity;
	private Vector3 position;
	public GhostAvatar(UUID id, Vector3 position)
	{ 
		this.id = id;
		this.position = position;
	}
// accessors and setters for id, node, entity, and position
//. . .
	public UUID getID()
	{
		return id;
	}
	public void setPosition(Vector3 position)
	{
		this.position = position;
	}
	
	public Vector3 getPosition()
	{
		return this.position;
	}
	
	public void setNode(SceneNode node)
	{
		this.node = node;
	}
	
	public SceneNode getNode()
	{
		return this.node;
	}
	
	public void setEntity(Entity entity)
	{
		this.entity = entity;
	}
}