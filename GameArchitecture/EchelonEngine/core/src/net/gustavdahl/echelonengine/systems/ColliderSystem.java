package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.colliders.Collider;
import net.gustavdahl.echelonengine.components.colliders.CollisionState;
import net.gustavdahl.echelonengine.components.physics.ICollider;
import net.gustavdahl.echelonengine.components.visual.IDebugRenderable;
import net.gustavdahl.echelonengine.enums.CollisionMode;

public class ColliderSystem extends BaseSystem<Collider>
{

	private CollisionMode _collisionMode;

	public ColliderSystem(CollisionMode collisionMode)
	{
		//_colliderList = new ArrayList<Collider>();
		this._collisionMode = collisionMode;
	}

	void QuickSort()
	{
		Collections.sort(_componentList);
	}

	private void SortAndPrune()
	{
		QuickSort();
		Prune();
	}

	private void Prune()
	{
		DebugSystem.AddDebugText("Collision Method: Sort and Prune");

		for (int i = 0; i < _componentList.size(); i++)
		{
			Collider a = _componentList.get(i);

			for (int j = i + 1; j < _componentList.size(); j++)
			{
				Collider b = _componentList.get(j);

				if (b.GetLeftSide() < a.GetRightSide())
				{
					a.SetCollisionState(CollisionState.PotentialCollision);
					b.SetCollisionState(CollisionState.PotentialCollision);

					boolean hit = a.Collide(b);
					if (hit)
					{
						a.SetCollisionState(CollisionState.IsColliding);
						b.SetCollisionState(CollisionState.IsColliding);
					}

				}
			}
		}
	}

	void BruteForceCollisionCheck(List<Collider> list)
	{
		DebugSystem.AddDebugText("Collision Method: Pair-wise Brute Force");
		
		// potential collision
		// for (int i = 0; i < list.size(); i++)
		// list.get(i).SetCollisionState(CollisionState.PotentialCollision);

		// doing the pair-wise collision check
		for (int i = 0; i < list.size(); i++)
		{
			Collider a = list.get(i);

			for (int j = i + 1; j < list.size(); j++)
			{
				Collider b = list.get(j);

				// check if a and b collide
				boolean hit = a.Collide(b);

				if (hit)
				{
					a.SetCollisionState(CollisionState.IsColliding);
					b.SetCollisionState(CollisionState.IsColliding);
				}
			}
		}
	}

	void ClearCollisions()
	{
		for (int i = 0; i < _componentList.size(); i++)
		{
			Collider a = _componentList.get(i);
			a.ClearCollisions();

			// a.Update(deltaTime);
		}
	}

	void CalculateCollisions()
	{
		switch (_collisionMode)
		{
		case SortAndPrune:
			SortAndPrune();
			break;

		case BruteForce:
			BruteForceCollisionCheck(_componentList);
			break;
		}
	}

	@Override
	public void Update(float deltaTime)
	{

		if (!_isActive)
			return;

		// fixed delta time?

		ClearCollisions();
		CalculateCollisions();

	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof Collider)
		{
			succesfullyAdded = true;
			_componentList.add((Collider) c);

		} else
			throw new RuntimeException("ERROR - component " + c.getClass().getSimpleName() + " is not a Collider!!");

		return succesfullyAdded;
	}

}
