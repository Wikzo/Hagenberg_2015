package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.components.Collider;
import net.gustavdahl.echelonengine.components.CollisionState;
import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.ICollider;
import net.gustavdahl.echelonengine.components.IDebugRenderable;

public class ColliderSystem extends BaseSystem
{

	private List<Collider> _colliderList; // list of ALL colliders

	private boolean _firstTimeAddToActiveList;

	public ColliderSystem()
	{
		_colliderList = new ArrayList<Collider>();
	}

	void QuickSort()
	{
		Collections.sort(_colliderList);
	}

	private void SortAndPrune()
	{
		QuickSort();
		Prune();
	}

	private void Prune()
	{
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);

			for (int j = i + 1; j < _colliderList.size(); j++)
			{
				Collider b = _colliderList.get(j);

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

	void BruteForceCollisionCheck(List<Collider> list, String collisionCheckType)
	{
		DebugSystem.AddDebugText("List size: " + list.size(), new Vector2(300, 400));
		DebugSystem.AddDebugText(collisionCheckType + " checks: " + Math.pow(list.size(), 2), new Vector2(300, 370));

		// potential collision
		for (int i = 0; i < list.size(); i++)
			list.get(i).SetCollisionState(CollisionState.PotentialCollision);

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
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);
			a.ClearCollisions();
			
			//a.Update(deltaTime);
		}
	}
	
	void CalculateCollisions()
	{
		SortAndPrune();
		// BruteForceCollisionCheck(_colliderList, "BruteForce");
	}
	
	
	@Override
	public void Update(float deltaTime)
	{

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
			_colliderList.add((Collider) c);
			System.out.println(c.Name() + " was added to ColliderSystem");

		} else
			throw new RuntimeException("ERROR - component " + c.getClass().getSimpleName() + " is not a Collider!!");

		return succesfullyAdded;
	}

}
