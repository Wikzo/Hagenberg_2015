package net.gustavdahl.echelonengine.systems;

import java.util.Collections;
import java.util.List;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.colliders.Collider;
import net.gustavdahl.echelonengine.components.colliders.CollisionState;
import net.gustavdahl.echelonengine.enums.CollisionMode;

public class ColliderSystem extends BaseSystem<Collider>
{

	private CollisionMode _collisionMode;
	
	int _sortAndPruneOverlapCounter = 0;

	public ColliderSystem(CollisionMode collisionMode)
	{
		this._collisionMode = collisionMode;
	}

	public void SetCollisionMode(CollisionMode mode)
	{
		this._collisionMode = mode;
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
		DebugSystem.AddDebugText("Collision Method: Sort and Prune [complexity: " + _sortAndPruneOverlapCounter + "]");

		_sortAndPruneOverlapCounter = 0;
		for (int i = 0; i < _componentList.size(); i++)
		{
			Collider a = _componentList.get(i);

			for (int j = i + 1; j < _componentList.size(); j++)
			{
				Collider b = _componentList.get(j);

				if (b.GetLeftSide() < a.GetRightSide())
				{
					_sortAndPruneOverlapCounter++;
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

	void BruteForceCollisionCheck()
	{
		DebugSystem.AddDebugText("Collision Method: Pair-wise Brute Force [complexity: " + _componentList.size() * _componentList.size() + "]");
		
		// potential collision
		// for (int i = 0; i < list.size(); i++)
		// list.get(i).SetCollisionState(CollisionState.PotentialCollision);

		// doing the pair-wise collision check
		for (int i = 0; i < _componentList.size(); i++)
		{
			Collider a = _componentList.get(i);

			for (int j = i + 1; j < _componentList.size(); j++)
			{
				Collider b = _componentList.get(j);

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
			BruteForceCollisionCheck();
			break;
		}
	}

	@Override
	public void Update(float deltaTime)
	{

		if (!_isActive)
			return;

		ClearCollisions();
		CalculateCollisions();

	}

	@Override
	public boolean ValidateIfComponentCanBeAddedToSystem(Component c)
	{
		if (c instanceof Collider)
			return true;
		else
			return false;
	}

}
