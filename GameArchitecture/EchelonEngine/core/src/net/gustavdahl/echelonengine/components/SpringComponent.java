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
	
	float _springConstant = 5;
	float _dampConstant = 0.8f;
	Vector2 _anchor = new Vector2(0, 0);

	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();
		
		_body = Owner.GetComponent(PhysicsComponent.class);
		if (_body == null)
			System.out.println("ERROR - need to have a PhysicsComponent for Spring to work!");
		
		_anchor.set(Transform.Position);
	}

	@Override
	public void Update(float deltaTime)
	{
		System.out.println("spring");
		
		_body.AddForce(HookeSpring());
	}

	Vector2 HookeSpring()
	{
		Vector2 pos = new Vector2(0, 0);
		pos.set(Transform.Position);
		Vector2 displacement = pos.sub(_anchor);

		Vector2 damp = displacement.scl(-_springConstant);

		damp.add(_velocity.cpy().scl(-_dampConstant));

		// System.out.println(_anchor);
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
