package net.gustavdahl.engine.components;

import javax.activation.UnsupportedDataTypeException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector;
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
		// http://www.wildbunny.co.uk/blog/2011/04/20/collision-detection-for-dummies/comment-page-1/
		if (other instanceof CircleCollider)
		{

			
			CircleCollider otherCircle = (CircleCollider) other;
			
			final Vector2 a = this.GetCenter();
			final Vector2 b = otherCircle.GetCenter();
			final float radiusA = this.Radius();
			final float radiusB = otherCircle.Radius();

			final double dst2 = a.dst2(b); // squared distance between origins
			final double r2 = Math.pow(radiusA + radiusB,2); // radii summed
			
			
			if (dst2 > r2) // no overlap
				return false;
			else // overlap
			{
				final double dst = Math.sqrt(dst2);
				final double r = Math.sqrt(r2);
				final double penetration = dst - r; // penetration amount
				final Vector2 n = (a.sub(b)).nor(); // normalized distance: N = (A-B) / ||A-B||
				final Vector2 penetrationVector = n.scl((float) penetration); // vector to push out
				
				//System.out.println("P: " + penetrationVector);
				
				// TODO: can only hit/move the next one in the loop from ColliderSystem!
				this.Owner.AddPosition(penetrationVector.scl(-1));
				otherCircle.Owner.AddPosition(penetrationVector);
				
				return true;
			}
				

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
