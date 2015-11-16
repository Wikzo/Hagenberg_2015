package net.gustavdahl.engine.components;

import javax.activation.UnsupportedDataTypeException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.systems.DebugSystem;
import net.gustavdahl.engine.systems.MyAssetManager;
import net.gustavdahl.engine.systems.ServiceLocator;

public class CircleCollider extends Collider implements IDebugRenderable
{

	protected float _radius = 10f;

	public CircleCollider(float radius)
	{
		super();
		_radius = radius;
	}

	/*
	 * public void CheckCircleCollision(CircleCollider other) { float distance =
	 * GetCenter().dst(other.GetCenter());
	 * 
	 * //float distance = other._radius;
	 * 
	 * if (distance <= this._radius + other._radius) _currentColor =
	 * _collisionColor; else _currentColor = _normalColor;
	 * 
	 * //return false; }
	 */

	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{
		Gdx.gl.glEnable(GL30.GL_BLEND);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(_currentDebugColor);
		shapeRenderer.circle(Transform.Position.x, Transform.Position.y, Radius());

		shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);

	}
	
	protected float Radius()
	{
		return _radius * Transform.Scale.x;
	}

	@Override
	public boolean IsHit(ICollider other)
	{
		if (other instanceof CircleCollider)
		{
			CircleCollider otherCircle = (CircleCollider) other;

			final float dst2 = this.GetCenter().dst(otherCircle.GetCenter());
			final float r2 = this.Radius() + otherCircle.Radius();

			if (dst2 > r2)
				return false;
			else
				return true;

		}
		
		if (other instanceof BoxCollider)
		{


		}

		return false;
	}

	@Override
	public void Update(float deltaTime)
	{
		// TODO Auto-generated method stub

	}

}
