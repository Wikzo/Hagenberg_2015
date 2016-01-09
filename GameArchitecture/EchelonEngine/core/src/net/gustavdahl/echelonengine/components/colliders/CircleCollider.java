package net.gustavdahl.echelonengine.components.colliders;

import javax.activation.UnsupportedDataTypeException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.components.visual.IDebugRenderable;
import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.MyAssetManager;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class CircleCollider extends Collider implements IDebugRenderable
{

	protected float _radius = 10f;

	public CircleCollider(float radius)
	{
		super();
		_radius = radius;
		
		IsStatic = true;
	}


	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{
		Gdx.gl.glEnable(GL30.GL_BLEND);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(_currentDebugColor);
		shapeRenderer.circle(Transform.PositionX, Transform.PositionY, Radius());

		// start/end dots
		/*shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.circle(GetLeftSide(), Transform.Position.y, 10);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.circle(GetRightSide(), Transform.Position.y, 10);*/

		shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);

	}
	
	protected float Radius()
	{
		return _radius * Transform.ScaleX;
	}


	@Override
	public void Update(float deltaTime)
	{
		

	}

	@Override
	public boolean Collide(Collider collider)
	{
		return collider.CollideWithCircle(this);
	}

	@Override
	protected boolean CollideWithCircle(CircleCollider circle)
	{
		// circle-circle collision
		
		//System.out.println("Checking " + circle + " against " + this.Name());
			
		final Vector2 a = this.GetCenter();
		final Vector2 b = circle.GetCenter();
		final float radiusA = this.Radius();
		final float radiusB = circle.Radius();

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
			
			// CIRCLE PENETRATION:
			/*if (!this.IsStatic)
				this.Owner.AddPosition(penetrationVector.scl(-1));
			
			if (!circle.IsStatic)
				circle.Owner.AddPosition(penetrationVector.scl(-1));
			*/
			
			//System.out.println(circle + " hit " + this.Name());
			return true;
		}
		
		
	}

	@Override
	protected boolean CollideWithBox(BoxCollider box)
	{
		return box.CollideWithCircle(this);
	}

	@Override
	public int GetLeftSide()
	{
		return Math.round(Transform.PositionX - Radius());
	}
	
	@Override
	public int GetRightSide()
	{
		return Math.round(Transform.PositionX + Radius());
	}


	@Override
	public String OnSelectedText()
	{
		// TODO Auto-generated method stub
		return null;
	}



}
