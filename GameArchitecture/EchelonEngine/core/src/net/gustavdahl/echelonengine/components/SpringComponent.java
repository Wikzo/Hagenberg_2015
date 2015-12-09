package net.gustavdahl.echelonengine.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class SpringComponent extends ForceComponent implements IDebugRenderable
{

	private float _springConstant = 5;
	private float _dampConstant = 2.5f;

	private float _anchorX, _anchorY;
	private float _tempPositionX, _tempPositionY;
	private float _springX, _springY;
	private float _dampX, _dampY;

	public SpringComponent(PhysicsBody b)
	{
		super(b);

	}
	
	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();
		
		_body = Owner.GetComponent(PhysicsBody.class);
		if (_body == null)
			System.out.println("ERROR - need to have a PhysicsComponent for Spring to work!");
		
		//_anchor.set(Transform.Position);
		_anchorX = Transform.PositionX;
		_anchorY = Transform.PositionY;
		
	}

	@Override
	protected Vector2 CalculateForce()
	{
		_dampX = _body._velocity.x * _dampConstant;
		_dampY = _body._velocity.y * _dampConstant;

		_springX = -_springConstant * (Transform.PositionX - _anchorX) - _dampX;
		_springY = -_springConstant * (Transform.PositionY - _anchorY) - _dampY;

		Vector2 damp = new Vector2(_springX, _springY);

		return damp;
	}

	public SpringComponent SetDamp(float damp)
	{
		_dampConstant = damp;
		return this;
	}

	public SpringComponent SetSpringConstant(float constant)
	{
		_springConstant = constant;

		return this;
	}

	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{
		Gdx.gl.glEnable(GL30.GL_BLEND);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(1, 0, 0, 0.8f));
		shapeRenderer.circle(_anchorX, _anchorY, 5);

		shapeRenderer.setColor(new Color(0, 1, 0, 0.8f));

		shapeRenderer.rectLine(_anchorX, _anchorY, Transform.PositionX, Transform.PositionY, 5);

		shapeRenderer.end();

		Gdx.gl.glDisable(GL30.GL_BLEND);

	}

}
