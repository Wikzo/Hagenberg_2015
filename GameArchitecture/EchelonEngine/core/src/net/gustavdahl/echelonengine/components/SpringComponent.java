package net.gustavdahl.echelonengine.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class SpringComponent extends PhysicsComponent implements IDebugRenderable
{

	private PhysicsComponent _body;
	
	private float _springConstant = 5;
	private float _dampConstant = 0.8f;
	
	private Vector2 _anchor = new Vector2(0, 0);
	private Vector2 _tempPosition = new Vector2(0,0);

	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();
		
		_body = Owner.GetComponent(PhysicsComponent.class);
		if (_body == null)
			System.out.println("ERROR - need to have a PhysicsComponent for Spring to work!");
		
		_anchor.set(Transform.Position);
		
		_velocity = _body._velocity;
	}

	@Override
	public void Update(float deltaTime)
	{		
		_body.AddForce(HookeSpring());
	}

	Vector2 HookeSpring()
	{
		_tempPosition.set(Transform.Position);
		Vector2 displacement = _tempPosition.sub(_anchor);

		Vector2 damp = displacement.scl(-_springConstant);

		damp.add(_velocity.cpy().scl(-_dampConstant));

		return damp;
	}
	
	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{
		Gdx.gl.glEnable(GL30.GL_BLEND);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(1,0,0,0.8f));
		shapeRenderer.circle(_anchor.x, _anchor.y, 5);
		
		shapeRenderer.setColor(new Color(0,1,0,0.8f));

		shapeRenderer.rectLine(_anchor, Transform.Position, 2);

		shapeRenderer.end();

		Gdx.gl.glDisable(GL30.GL_BLEND);
		
	}
}
