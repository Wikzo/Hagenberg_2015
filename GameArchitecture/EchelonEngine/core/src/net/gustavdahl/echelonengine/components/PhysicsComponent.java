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

public class PhysicsComponent extends Component implements IDebugRenderable
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
	public void GetExternalReferences()
	{

		super.GetExternalReferences();
		
		_anchor.set(Transform.Position);
		
		//hejsa = new Vector2(2,2);
		hejsa.set(51,5);
	}

	@Override
	public void Update(float deltaTime)
	{

		// https://stackoverflow.com/questions/33759145/libgdx-how-do-i-multiply-vector-with-scalar-without-modifing-original-vector?
		ApplyForces(deltaTime);
		
		System.out.println("T: " + _anchor);
		
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
		
		_force.add(HookeSpring());
		
		// acceleration
		_acceleration = _force.scl(1f / _mass);
		
		_velocity = _velocity.add(_acceleration.scl(deltaTime));
		
		Transform.Position.set(_newPosition);

		// terminal velocity
		/*if (_velocity.y < _terminalVelocity.y)
			_velocity.y = _terminalVelocity.y;*/
	}
	
	float _springConstant = 5;
	float _dampConstant = 0.8f;
	Vector2 _anchor = new Vector2(0,0);
	
	Vector2 HookeSpring()
	{
		Vector2 pos = new Vector2(0,0);
		pos.set(Transform.Position);
		Vector2 displacement = pos.sub(_anchor);
		
		Vector2 damp = displacement.scl(-_springConstant);
		
		damp.add(_velocity.cpy().scl(-_dampConstant));
		
		//System.out.println(_anchor);
        return damp;
	}

	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{
		Gdx.gl.glEnable(GL30.GL_BLEND);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0,1,0,0.8f));

		shapeRenderer.rectLine(_anchor, Transform.Position, 2);

		shapeRenderer.end();

		Gdx.gl.glDisable(GL30.GL_BLEND);
		
	}

}
