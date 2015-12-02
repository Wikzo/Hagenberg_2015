package net.gustavdahl.echelonengine.components;

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
	
	private Vector2 _newPosition = Vector2.Zero;
	final private float _mass = 1f;
	private Vector2 _force = new Vector2();
	private List<Vector2> _constantForces = new ArrayList<Vector2>(); 
	protected Vector2 _velocity;
	protected Vector2 _acceleration;
	
	final private Vector2 _terminalVelocity = new Vector2(0f, -100f);
	

	public PhysicsComponent()
	{
		DefaultSystem = PhysicsSystem.class;
		_velocity = new Vector2(0f, 0f);
		_acceleration = new Vector2(0f, 0f);
		
		
	}
	
	Vector2 hejsa = new Vector2(1,1);


	@Override
	public void Update(float deltaTime)
	{
		// https://stackoverflow.com/questions/33759145/libgdx-how-do-i-multiply-vector-with-scalar-without-modifing-original-vector?
		ApplyForces(deltaTime);
		
	}
	
	public void AddForce(Vector2 force)
	{
		// TODO: force modes
		// http://docs.unity3d.com/ScriptReference/ForceMode2D.html
		
		_force.add(force).scl(_mass);
	}
	
	public void AddConstantForce(Vector2 constantForce)
	{
		_constantForces.add(constantForce);
	}
	
	private void ApplyForces(float deltaTime)
	{
		// temp new position
		_newPosition.set(Transform.Position);
		
		// apply velocity
		_newPosition.mulAdd(_velocity, deltaTime);
		
		// apply constant forces
		for (Vector2 constantForce : _constantForces)
			_force.add(constantForce).scl(_mass);
		
		// acceleration
		_acceleration = _force.scl(1f / _mass);
		
		_velocity = _velocity.add(_acceleration.scl(deltaTime));
		
		Transform.Position.set(_newPosition);

		// terminal velocity
		/*if (_velocity.y < _terminalVelocity.y)
			_velocity.y = _terminalVelocity.y;*/
	}
	


	

}
