package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.echelonengine.entities.Entity;

public class EntityManager
{
	private List<Entity> _entities;

	public EntityManager()
	{
		_entities = new ArrayList<Entity>();
	}

	// TODO: messenger system to dynamically add/remove entities
	public void AddEntity(Entity e)
	{
		_entities.add(e);
	}
	
	public int GetEntityCount()
	{
		return _entities.size();
	}

	public void RemoveEntity()
	{
	}

	public void RemoveAllEntities()
	{
	}

}
