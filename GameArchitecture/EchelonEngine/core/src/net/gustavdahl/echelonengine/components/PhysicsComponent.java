package net.gustavdahl.echelonengine.components;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;

public class PhysicsComponent extends Component
{

	final protected Vector2 _gravity = new Vector2(0f, -9.82f);
	final protected float _mass = 1f;
	protected Vector2 _velocityVector;
	protected Vector2 _accelerationVector;
	final protected Vector2 _terminalVelocity = new Vector2(0f, -10f);
	final private Vector2 _force = new Vector2();
	
	/*float[] _oldPosition = {0f,0f};
	float[] _oldVelocity ={0f,0f};
	float[] _oldAcceleration = {0f,0f};

	float[] _newPosition = {0f,0f};
	float[] _newVelocity ={0f,0f};
	float[] _newAcceleration = {0f,0f};*/
	
	Vector2 _newPosition = Vector2.Zero;

	public PhysicsComponent()
	{
		DefaultSystem = PhysicsSystem.class;
		_velocityVector = new Vector2(0f, 0f);
		_accelerationVector = new Vector2(0f, 0f);
	}

	@Override
	public void Update(float deltaTime)
	{

		// https://stackoverflow.com/questions/33759145/libgdx-how-do-i-multiply-vector-with-scalar-without-modifing-original-vector?

	
		_newPosition.set(Transform.Position);
		
		_newPosition.mulAdd(_velocityVector, deltaTime);
		
		_force.set(_gravity).scl(_mass);
		
		_accelerationVector = _force.scl(1f / _mass);
		
		_velocityVector = _velocityVector.add(_accelerationVector.scl(deltaTime));
		
		Transform.Position.set(_newPosition);

		if (_velocityVector.y < _terminalVelocity.y)
			_velocityVector.y = _terminalVelocity.y;

		//System.out.println(_velocity);
	}
	
	public void AddForce(Vector2 force)
	{
		_force.add(force);
	}
	
	/*private void ApplyForces()
	{
		_force.set(_gravity).scl(_mass);
		_accelerationVector = _force.scl(1f / _mass);
		_velocityVector = _velocityVector.add(_accelerationVector.scl(deltaTime));
	}*/

}
