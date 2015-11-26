package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.components.Collider;
import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.ICollider;
import net.gustavdahl.echelonengine.components.IDebugRenderable;

public class ColliderSystem extends BaseSystem
{

	private List<Collider> _colliderList; // list of ALL colliders
	private List<Collider> _activeList; // list of colliders that potentially
										// overlap

	// Used to add the first collide one time, because I currently have no
	// Initialize() method
	private boolean _firstTimeAddToActiveList;

	public ColliderSystem()
	{
		_colliderList = new ArrayList<Collider>();
		_activeList = new ArrayList<Collider>();
	}

	void QuickSort()
	{
		// Each Collider implement the Comparable interface
		// compareTo() is overriden to use the int from GetLeftSide()
		Collections.sort(_colliderList);

		/*
		 * String sortedPositions = "";
		 * 
		 * for (int i = 0; i < _colliderList.size(); i++) sortedPositions +=
		 * "\n" + _colliderList.get(i).GetLeftSide();
		 * 
		 * sortedPositions += "\n";
		 * 
		 * DebugSystem.AddDebugText("Sorted positions: " + sortedPositions);
		 */
	}

	private void SortAndPrune()
	{
		QuickSort();
		_activeList.clear();
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
					a.SetHitColorDebug(true, Color.GOLDENROD);
					b.SetHitColorDebug(true, Color.GOLDENROD);

					boolean hit = a.Collide(b);
					if (hit)
					{
						a.SetHitColorDebug(true);
						b.SetHitColorDebug(true);
					}

				} else
				{
					// CollisionCheck(_activeList, "SAP");
					break;
				}
			}
			System.out.println("Outer loop");
			
		}
	}

	void CollisionCheck(List<Collider> list, String collisionCheckType)
	{
		DebugSystem.AddDebugText("List size: " + list.size(), new Vector2(300, 400));
		DebugSystem.AddDebugText(collisionCheckType + " checks: " + Math.pow(list.size(), 2), new Vector2(300, 370));

		// potential collision
		for (int i = 0; i < list.size(); i++)
			list.get(i).SetHitColorDebug(true, Color.GOLDENROD);

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
					a.SetHitColorDebug(true);
					b.SetHitColorDebug(true);
				}
			}
		}
	}

	@Override
	public void Update(float deltaTime)
	{

		// reset all collider hits
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);
			a.SetHitColorDebug(false);

			a.Update(deltaTime);
		}

		SortAndPrune();
		// CollisionCheck(_colliderList, "BruteForce");

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
