package net.gustavdahl.echelonengine.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.systems.DebugSystem;

public class SpringComponent extends PhysicsComponent implements IDebugRenderable
{

	private PhysicsComponent _body;
	
	private float _springConstant = 5;
	private float _dampConstant = 2.5f;
	
	private float _anchorX, _anchorY;
	private float _tempPositionX, _tempPositionY;
	private float _springX, _springY;
	private float _dampX, _dampY;

	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();
		
		_body = Owner.GetComponent(PhysicsComponent.class);
		if (_body == null)
			System.out.println("ERROR - need to have a PhysicsComponent for Spring to work!");
		
		//_anchor.set(Transform.Position);
		_anchorX = Transform.PositionX;
		_anchorY = Transform.PositionY;
		
		_velocity = _body._velocity;
	}
	
	public SpringComponent SetDamp(float damp)
	{
		_dampConstant = damp;
		return this;
	}

	@Override
	public void Update(float deltaTime)
	{		
		_body.AddForce(HookeSpring());
		
		DebugSystem.AddDebugText("Euler: " + _body._eulerMethod + "\nMass: " + Float.toString(_body._mass)
				+ "\nDamping: " + _dampConstant,
				new Vector2(Transform.PositionX, Transform.PositionY));
	}

	Vector2 HookeSpring()
	{
		_dampX = _velocity.x * _dampConstant;
		_dampY = _velocity.y * _dampConstant;
		
		_springX = -_springConstant * (Transform.PositionX - _anchorX) - _dampX;
		_springY = -_springConstant * (Transform.PositionY - _anchorY) - _dampY;
		
		Vector2 damp = new Vector2(_springX, _springY);

		return damp;
	}
	
	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{
		Gdx.gl.glEnable(GL30.GL_BLEND);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(1,0,0,0.8f));
		shapeRenderer.circle(_anchorX, _anchorY, 5);
		
		shapeRenderer.setColor(new Color(0,1,0,0.8f));

		shapeRenderer.rectLine(_anchorX, _anchorY, Transform.PositionX,Transform.PositionY,5);

		shapeRenderer.end();

		Gdx.gl.glDisable(GL30.GL_BLEND);
		
	}
}
