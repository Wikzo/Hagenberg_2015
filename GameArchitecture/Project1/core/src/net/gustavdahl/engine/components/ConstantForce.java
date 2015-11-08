package net.gustavdahl.engine.components;

import com.badlogic.gdx.math.Vector2;

public class ConstantForce extends PhysicsComponent
{
	private Vector2 _velocity;
	private float _angularVelocity;

	public ConstantForce(Vector2 velocity, float angularVelocity)
	{
		_velocity = velocity;
		_angularVelocity = angularVelocity;
		
		CanHaveMultipleComponentsOfThisType = false;
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
