package net.gustavdahl.echelonengine.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;

import net.gustavdahl.echelonengine.systems.ColliderSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;



public abstract class Collider extends Component implements Comparable<Collider>
{

	private Vector2 _center;

	// sprite colors
	protected Color _spriteColorNormal;
	protected Color _spriteColorCollision = new Color(0f, 1f, 0f, 0.5f);
	protected Color _currentSpriteColor;

	// collider colors (debug)
	protected Color CollisionColor =  new Color(1f, 0f, 0f, 0.8f);
	protected Color PotentialHitColor = Color.GOLDENROD;
	protected Color NoCollisionColor = new Color(0.9f, 0.7f, 0.3f, 0.2f);

	protected Color _currentDebugColor;

	protected SpriteComponent _sprite;

	protected boolean IsStatic = false;

	protected CollisionState CollisionState;

	public Collider()
	{
		super();

		DefaultSystem = ColliderSystem.class;
	}

	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();

		if (Owner.GetComponent(SpriteComponent.class) == null)
			throw new RuntimeException("ERROR! - " + Owner.Name + " has no Sprite component!");
		else
			_sprite = (SpriteComponent) Owner.GetComponent(SpriteComponent.class);

		CollisionState = CollisionState.NoCollision;
		_spriteColorNormal = new Color(_sprite._color.r, _sprite._color.g, _sprite._color.b, _sprite._color.a);
		
		_currentSpriteColor = _spriteColorNormal;
		_currentDebugColor = NoCollisionColor;

	}

	public Vector2 GetCenter()
	{
		return new Vector2(Transform.Position.x, Transform.Position.y);
	}

	public abstract int GetLeftSide(); // TODO: use float instead of int!

	public abstract int GetRightSide(); // TODO: use float instead of int!

	@Override
	public int compareTo(Collider o)
	{
		// TODO: use float instead of int!

		int leftSide = ((Collider) o).GetLeftSide();

		// ascending order
		return this.GetLeftSide() - leftSide;

		// descending order
		// return compareQuantity - this.quantity;
	}

	public void SetHitColor(boolean hit)
	{
		// NOT USED ANYMORE

		if (hit)
		{
			_sprite.Color(_spriteColorCollision);
			// _currentDebugColor = _debugColorCollision;
		} else
		{
			_sprite.Color(_spriteColorNormal);
			// _currentDebugColor = _debugColorNormal;
		}
	}

	

	public void SetCollisionState(CollisionState state)
	{
		CollisionState = state;
		
		switch (state)
		{
		case IsColliding:
			_currentDebugColor = CollisionColor;
			break;
		case PotentialCollision:
			_currentDebugColor = PotentialHitColor;
		}

	}
	
	public void ClearCollisions()
	{
		CollisionState = CollisionState.NoCollision;
		_currentDebugColor = NoCollisionColor;
	}

	public Collider SetStatic(boolean s)
	{
		IsStatic = s;
		return this;
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
				// circle.SetHitColor(false);
				return null;
			} else
			{
				// circle.SetHitColor(true);
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
				// box.SetHitColor(true);
				return box;
			} else
			{
				// box.SetHitColor(false);
				return null;
			}

		}

		return null;

	}

	// http://www.wildbunny.co.uk/blog/2011/04/20/collision-detection-for-dummies/comment-page-1/
	// double dispatch / visitor pattern:
	// https://www.gamedev.net/topic/453624-double-dispatch-in-c/
	public abstract boolean Collide(Collider collider);

	protected abstract boolean CollideWithCircle(CircleCollider circle);

	protected abstract boolean CollideWithBox(BoxCollider box);

}
