package net.gustavdahl.engine.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;

import net.gustavdahl.engine.systems.PhysicsSystem;

public abstract class Collider extends PhysicsComponent
{

	private Vector2 _center;

	protected Color _noCollision = new Color(0.7f, 0.7f, 0.8f, 0.5f);
	protected Color _collision = new Color(1f, 0f, 0f, 0.5f);
	protected Color _currentColor;

	public Collider()
	{
		super();

		_currentColor = _noCollision;
		DefaultSystem = PhysicsSystem.class;
	}

	public Vector2 GetCenter()
	{
		return new Vector2(Transform.Position.x, Transform.Position.y);
	}

	public void SetHitColor(boolean hit)
	{
		if (hit)
			_currentColor = _collision;
		else
			_currentColor = _noCollision;
	}

	public static boolean Intersect(Ray ray, Collider collider)
	{
		// check for circle collider
		if (collider instanceof CircleCollider)
		{
			CircleCollider circle = (CircleCollider) collider;

			final float dst2 = circle.GetCenter().dst2(new Vector2(ray.origin.x, ray.origin.y));
			final float r2 = circle._radius * circle._radius;

			if (dst2 > r2)
			{
				circle.SetHitColor(false);
				return false;
			}
			else
			{
				circle.SetHitColor(true);
				return true;
			}
			
			
		} else if (collider instanceof BoxCollider)
		{
			BoxCollider box = (BoxCollider) collider;
			boolean x = false;
			if (ray.origin.x > box._bounds.getX() && ray.origin.x < box._bounds.getX() + box._bounds.width)
				x = true;

			boolean y = false;
			if (ray.origin.y > box._bounds.getY() && ray.origin.y < box._bounds.getY() + box._bounds.height)
				y = true;

			if (x && y)
			{
				box.SetHitColor(true);
				return true;
			}
			else
			{
				box.SetHitColor(false);
				return false;
			}

		}

		return false;

	}

}
