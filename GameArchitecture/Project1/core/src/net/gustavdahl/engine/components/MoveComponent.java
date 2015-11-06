package net.gustavdahl.engine.components;

import com.badlogic.gdx.math.Vector2;

public class MoveComponent extends Component implements IMovable
{

	/*
	 * public var position:Point; public var rotation:Number; public var
	 * velocity:Point; public var angularVelocity:Number;
	 */

	private Vector2 _velocity;
	private float _angularVelocity;

	public MoveComponent(Vector2 velocity, float angularVelocity)
	{
		_velocity = velocity;
		_angularVelocity = angularVelocity;
	}

	@Override
	public void Move(float deltaTime)
	{
		Transform.Position.mulAdd(_velocity, deltaTime);
		Transform.Rotation += _angularVelocity * deltaTime;
	}

}
