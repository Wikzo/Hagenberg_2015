package net.gustavdahl.engine.components;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.systems.PhysicsSystem;

public abstract class Collider extends PhysicsComponent
{

	private Vector2 _center;
	
	public Collider()
	{
		super();
		
		DefaultSystem = PhysicsSystem.SystemName;
	}
	
	
	public Vector2 GetCenter()
	{
		return new Vector2(Transform.Position.x, Transform.Position.y);
	}

}
