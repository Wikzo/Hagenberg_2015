package net.gustavdahl.echelonengine.components;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;

public class PhysicsComponent extends Component
{

	final protected Vector2 _gravity = new Vector2(0f, -9.82f);
	final protected float _mass = 1f;
	protected Vector2 _velocity;
	protected Vector2 _acceleration;
	final protected Vector2 _terminalVelocity = new Vector2(0f, -10f);
	final private Vector2 _force = new Vector2();

	public PhysicsComponent()
	{
		DefaultSystem = PhysicsSystem.class;
		_velocity = new Vector2(0f, 0f);
		_acceleration = new Vector2(0f, 0f);
	}

	@Override
	public void Update(float deltaTime)
	{

		// https://stackoverflow.com/questions/33759145/libgdx-how-do-i-multiply-vector-with-scalar-without-modifing-original-vector?

		/*
		 * UNITY WAY: transform.position += velocity*Time.fixedDeltaTime;
		 * 
		 * Vector3 force = Physics.gravity*GetComponent<Rigidbody>().mass;
		 * acceleration = force/GetComponent<Rigidbody>().mass;
		 * 
		 * velocity = velocity + acceleration*Time.fixedDeltaTime;
		 */

		Transform.Position.mulAdd(_velocity, deltaTime);
		_force.set(_gravity).scl(_mass);
		_acceleration = _force.scl(1f / _mass);
		_velocity = _velocity.add(_acceleration.scl(deltaTime));

		if (_velocity.y < _terminalVelocity.y)
			_velocity.y = _terminalVelocity.y;

		//System.out.println(_velocity);
	}

}
