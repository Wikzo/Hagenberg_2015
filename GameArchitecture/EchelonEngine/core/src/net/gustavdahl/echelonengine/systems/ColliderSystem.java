package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

		/*String sortedPositions = "";

		for (int i = 0; i < _colliderList.size(); i++)
			sortedPositions += "\n" + _colliderList.get(i).GetLeftSide();

		sortedPositions += "\n";

		DebugSystem.AddDebugText("Sorted positions: " + sortedPositions);*/
	}

	void SortAndPrune()
	{

		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);

			for (int j = i + 1; j < _colliderList.size(); j++)
			{
				Collider b = _colliderList.get(j);

				if (b.GetLeftSide() < a.GetRightSide())
				{
					if (!_activeList.contains(a))
						_activeList.add(a);
					if (!_activeList.contains(b))
					_activeList.add(b);
				} else
				{
					CollisionCheckSAP();
					break;
				}
			}

		}

	}

	void CollisionCheckSAP()
	{
		DebugSystem.AddDebugText("SAP: checks: " + Math.pow(_activeList.size(), 2), new Vector2(300, 400));
		
		// doing the pair-wise collision check
		for (int i = 0; i < _activeList.size(); i++)
		{
			Collider a = _activeList.get(i);

			for (int j = i + 1; j < _activeList.size(); j++)
			{
				Collider b = _activeList.get(j);

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
	
	void CollisionCheckBruteForce()
	{
		DebugSystem.AddDebugText("BruteForce: checks: " + Math.pow(_colliderList.size(), 2), new Vector2(300, 400));
		
		// doing the pair-wise collision check
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);

			for (int j = i + 1; j < _colliderList.size(); j++)
			{
				Collider b = _colliderList.get(j);

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
		QuickSort();
		_activeList.clear();

		// reset all collider hits
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);
			a.SetHitColorDebug(false);

			a.Update(deltaTime);
		}

		SortAndPrune();
		//CollisionCheckBruteForce();
		
		// DebugSystem.AddDebugText("# collision checks (SAP): " +
		// Math.pow(_activeList.size(), 2), null);
		// DebugSystem.AddDebugText("# collision checks (brute force): " +
		// Math.pow(_colliderList.size(), 2), null);

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
