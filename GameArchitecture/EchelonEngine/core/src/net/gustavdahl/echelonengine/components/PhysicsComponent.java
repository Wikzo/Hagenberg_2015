package net.gustavdahl.echelonengine.components;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;

public class PhysicsComponent extends Component
{

	final public static Vector2 GravityForce = new Vector2(0f, -90.82f);
	protected float _mass = 2f;
	private Vector2 _force = new Vector2();
	private List<Vector2> _constantForces = new ArrayList<Vector2>();
	protected Vector2 _velocity;
	protected Vector2 _acceleration;

	final private Vector2 _terminalVelocity = new Vector2(0f, -100f);

	protected EulerMethod _eulerMethod = EulerMethod.Explicit;

	public PhysicsComponent SetMass(float mass)
	{
		_mass = mass;
		return this;
	}

	public PhysicsComponent SetEulerMethod(EulerMethod method)
	{
		this._eulerMethod = method;
		return this;
	}

	public PhysicsComponent()
	{
		DefaultSystem = PhysicsSystem.class;
		_velocity = new Vector2(0f, 0f);
		_acceleration = new Vector2(0f, 0f);

	}

	Vector2 hejsa = new Vector2(1, 1);

	@Override
	public void Update(float deltaTime)
	{
		// https://stackoverflow.com/questions/33759145/libgdx-how-do-i-multiply-vector-with-scalar-without-modifing-original-vector?
		ApplyForces(deltaTime);

		//DebugSystem.AddDebugText("Euler: " + _eulerMethod + "\nMass: " + Float.toString(_mass), new Vector2(Transform.PositionX, Transform.PositionY));

	}

	public void AddForce(Vector2 force)
	{
		// TODO: force modes
		// http://docs.unity3d.com/ScriptReference/ForceMode2D.html

		_force.add(force).scl(1f / _mass);
	}

	public void AddConstantForce(Vector2 constantForce)
	{
		_constantForces.add(constantForce);
	}

	Vector2 ComputeAllForces()
	{
		// apply constant forces
		for (Vector2 constantForce : _constantForces)
			_force.add(constantForce).scl(_mass);

		// acceleration
		//_acceleration = _force.scl(1f / _mass);
		
		return _force.scl(1f / _mass);
	}

	private void ApplyForces(float deltaTime)
	{
		// https://stackoverflow.com/questions/33759145/libgdx-how-do-i-multiply-vector-with-scalar-without-modifing-original-vector?noredirect=1#comment55292813_33759145

		float tempX = Transform.PositionX;
		float tempY = Transform.PositionY;
		
		switch (_eulerMethod)
		{
		case Explicit:
			// explicit Euler (inaccurate)

			// apply old velocity to new position

			tempX += _velocity.x * deltaTime;
			tempY += _velocity.y * deltaTime;
			
			// calculate forces
			_acceleration = ComputeAllForces();

			// add all forces to the velocity
			_velocity = _velocity.add(_acceleration.scl(deltaTime));

			// apply new position

			Transform.PositionX = tempX;
			Transform.PositionY = tempY;
			
			break;
		case Midpoint:
			// midpoint euler (more accurate/stable, little more expensive)
			
			 // midpoint euler (more accurate/stable, little more expensive)

			// UNITY WAY:
            /*var halfPosition = transform.position + Time.fixedDeltaTime/2.0f*velocity;
            var halfVelocity = velocity + Time.fixedDeltaTime/2.0f*ComputeForce(transform.position)/Mass;

            transform.position = transform.position + Time.fixedDeltaTime*halfVelocity;
            velocity = velocity + Time.fixedDeltaTime*ComputeForce(halfPosition)/Mass;*/
            
			// TODO: needs to be moved to SpringComponent or similar
			/*
            float halfPositionX, halfPositionY;
            halfPositionX = Transform.PositionX + deltaTime/2f * _velocity.x;
            halfPositionY = Transform.PositionY + deltaTime/2f * _velocity.y;
            
            float halfVelocityX, halfVelocityY;
            halfVelocityX = _velocity.x + deltaTime/2f * ComputeForce(Transform.PositionX) / _mass;
            halfVelocityY = _velocity.Y + deltaTime/2f * ComputeForce(Transform.PositionY) / _mass;
            
            Transform.PositionX = Transform.PositionX + deltaTime * halfVelocityX;
            Transform.PositionY = Transform.PositionX + deltaTime * halfVelocityY;
            
            _velocity.x = _velocity.x + deltaTime * ComputeForce(halfPositionX) / _mass;
            _velocity.y = _velocity.y + deltaTime * ComputeForce(halfPositionY) / _mass;
            */
			
			break;
		case Modified:
			break;
		case SemiImplicit:
			// semi-implicit euler (inaccurate)
			// "forget" the newPosition buffer
			// update velocity, then update position

			// calculate all forces
			_acceleration = ComputeAllForces();

			// calculate new velocity
			_velocity = _velocity.add(_acceleration.scl(deltaTime));

			// add new velocity to position
			//Transform.Position.mulAdd(_velocity, deltaTime);
			Transform.PositionX += _velocity.x * deltaTime;
			Transform.PositionY += _velocity.y * deltaTime;

			break;
		default:
			break;

		}

		// terminal velocity
		/*
		 * if (_velocity.y < _terminalVelocity.y) _velocity.y =
		 * _terminalVelocity.y;
		 */
	}

}
