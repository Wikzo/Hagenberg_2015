package net.gustavdahl.engine.components;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.systems.DebugSystem;
import net.gustavdahl.engine.systems.PhysicsSystem;

public class PhysicsComponent extends Component
{
	
	final protected Vector2 _gravity = new Vector2(0f, -9.82f);
	final protected float _mass = 1f;
	protected Vector2 _velocity;
	protected Vector2 _acceleration;
	final protected Vector2 _terminalVelocity = new Vector2(0f, -10f);

	public PhysicsComponent()
	{
		DefaultSystem = PhysicsSystem.class;
		_velocity = new Vector2(0f,0f);
		_acceleration = new Vector2(0f,0f);
	}
	
	

	@Override
	public void Update(float deltaTime)
	{
		// Transform is a class containing the a Vector2 called Position
		Transform.Position.add(_velocity.scl(deltaTime));
		
		Vector2 force = _gravity.scl(_mass);
		_acceleration = force.scl(1f/_mass);	
		_velocity = _velocity.add(_acceleration.scl(deltaTime));
		
		System.out.println(_velocity);
	}
	

	
	void ApplyForce(Vector2 force)
	{
		_velocity.add(force);
	}

}
