package net.gustavdahl.engine.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.systems.MyAssetManager;
import net.gustavdahl.engine.systems.ServiceLocator;

public class CircleCollider extends Collider implements IDebugRenderable
{

	private float _radius = 10f;
	private Color _noCollision = new Color(0.7f, 0.7f, 0.8f, 0.5f);
	private Color _collision = new Color(1f, 0f ,0f, 0.5f);
	private Color _currentColor;

	public CircleCollider(float radius)
	{
		super();
		_radius = radius;
		_currentColor = _noCollision;
	}

	public void CheckCircleCollision(CircleCollider other)
	{
		float distance = GetCenter().dst(other.GetCenter());

		//float distance = other._radius;
		
		if (distance <= this._radius + other._radius)
			_currentColor = _collision;
		else
			_currentColor = _noCollision;

		//return false;
	}

	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{

		Gdx.gl.glEnable(GL30.GL_BLEND);

		/*if (!_projectionMatrixSet)
		{
			_circleRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
			_projectionMatrixSet = true;
		}*/
		
		//System.out.println("circle");
		
		//spriteBatch.draw(ServiceLocator.AssetManager.DummyTexture, Transform.Position.x, Transform.Position.y);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(_currentColor);
		shapeRenderer.circle(Transform.Position.x, Transform.Position.y, _radius);
		shapeRenderer.end();

	}

}
