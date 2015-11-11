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

	// sprite colors
	protected Color _spriteColorNormal;
	protected Color _spriteColorCollision = new Color(1f, 0f, 0f, 0.5f);
	protected Color _currentSpriteColor;

	// collider colors (debug)
	protected Color _debugColorNormal = new Color(0.9f, 0.7f, 0.3f, 0.2f);
	protected Color _debugColorCollision = new Color(1f, 0f, 0f, 0.4f);

	protected Color _currentDebugColor;

	protected SpriteComponent _sprite;

	public Collider()
	{
		super();

		DefaultSystem = PhysicsSystem.class;
	}

	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();

		if (Owner.GetComponent(SpriteComponent.class) == null)
			throw new RuntimeException("ERROR! - " + Owner.Name + " has no Sprite component!");
		else
			_sprite = (SpriteComponent) Owner.GetComponent(SpriteComponent.class);

		// NOTE: need to make a new instance of color; otherwise a reference
		// would change the sprite's color every time
		_spriteColorNormal = new Color(_sprite._color.r, _sprite._color.g, _sprite._color.b, _sprite._color.a);
		_currentSpriteColor = _spriteColorNormal;

		_currentDebugColor = _debugColorNormal;

	}

	public Vector2 GetCenter()
	{
		return new Vector2(Transform.Position.x, Transform.Position.y);
	}

	public void SetHitColor(boolean hit)
	{
		if (hit)
		{
			_sprite.Color(_spriteColorCollision);
			_currentDebugColor = _debugColorCollision;
		} else
		{
			_sprite.Color(_spriteColorNormal);
			_currentDebugColor = _debugColorNormal;
		}
	}

	public static Collider MouseIntersectCollider(Ray ray, Collider collider)
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
				return null;
			} else
			{
				circle.SetHitColor(true);
				return circle;
			}

		} else if (collider instanceof BoxCollider)
		{
			BoxCollider box = (BoxCollider) collider;
			boolean x = false;
			if (ray.origin.x > box.Bounds().x && ray.origin.x < box.Bounds().x + box.Bounds().width)
				x = true;

			boolean y = false;
			if (ray.origin.y > box.Bounds().y && ray.origin.y < box.Bounds().y + box.Bounds().height)
				y = true;

			if (x && y)
			{
				box.SetHitColor(true);
				return box;
			} else
			{
				box.SetHitColor(false);
				return null;
			}

		}

		return null;

	}

}
