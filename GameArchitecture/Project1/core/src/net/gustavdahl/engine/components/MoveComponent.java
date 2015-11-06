package net.gustavdahl.engine.components;

import com.badlogic.gdx.math.Vector2;

public class MoveComponent extends PhysicsComponent
{
	private Vector2 _velocity;
	private float _angularVelocity;

	public MoveComponent(Vector2 velocity, float angularVelocity)
	{
		_velocity = velocity;
		_angularVelocity = angularVelocity;
	}

	@Override
	public void Update(float deltaTime)
	{
		super.Update(deltaTime);
		
		Move(deltaTime);
		Rotate(deltaTime);
	}
	
	public void Move(float deltaTime)
	{
		Transform.Position.mulAdd(_velocity, deltaTime);
		
	}

	public void Rotate(float deltaTime)
	{
		Transform.Rotation += _angularVelocity * deltaTime;
	}
	
	

}