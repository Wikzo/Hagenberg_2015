package net.gustavdahl.echelonengine.components.physics;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.systems.PhysicsSystem;

public class ConstantForce extends PhysicsBody
{
	private Vector2 _velocity;
	private float _angularVelocity;

	public ConstantForce(Vector2 velocity, float angularVelocity)
	{
		_velocity = velocity;
		_angularVelocity = angularVelocity;
		
		CanHaveMultipleComponentsOfThisType = false;
		DefaultSystem = PhysicsSystem.class;
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
		Transform.PositionX += _velocity.x * deltaTime;
		Transform.PositionY += _velocity.y * deltaTime;
		
	}

	public void Rotate(float deltaTime)
	{
		Transform.Rotation += _angularVelocity * deltaTime;
	}
	
	

}
