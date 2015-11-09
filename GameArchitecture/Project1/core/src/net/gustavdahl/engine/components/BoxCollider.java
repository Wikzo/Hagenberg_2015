package net.gustavdahl.engine.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoxCollider extends Collider implements IDebugRenderable
{

	protected float _width;
	protected float _height;
	protected Rectangle _bounds;

	public BoxCollider(float width, float height)
	{
		super();

		_width = width;
		_height = height;

	}

	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{

		Gdx.gl.glEnable(GL30.GL_BLEND);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(_currentColor);
		shapeRenderer.box(Transform.Position.x/2, Transform.Position.y/2, 0, _width, _height, 1);
		shapeRenderer.end();

	}

	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();

		_bounds = new Rectangle(Transform.Position.x - _width / 2, Transform.Position.y - _height / 2, _width, _height);
	}

}
