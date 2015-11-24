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
	private List<Collider> _activeList; // list of colliders that potentially overlap

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
		
		String sortedPositions = "";
		
		for (int i = 0; i < _colliderList.size(); i++)
			sortedPositions += "\n" + _colliderList.get(i).GetLeftSide();
		
		sortedPositions += "\n";
		
		DebugSystem.AddDebugText("Sorted positions: " + sortedPositions, null);
	}

	void AddToActiveList(Collider c)
	{

		for (int i = 0; i < _activeList.size(); i++)
		{
			// new one not colliding with old one?
			if (c.GetLeftSide() > _activeList.get(i).GetRightSide())
			{
				System.out
						.println("Potential collision: NO: " + c.Owner.Name + " and " + _activeList.get(i).Owner.Name);

				_activeList.remove(i);

			} else
			{
				System.out
						.println("Potential collision: YES: " + c.Owner.Name + " and " + _activeList.get(i).Owner.Name);

				// maybe perform the collision check here directly, such as:
				/*
				 * boolean hit = c.Collide(_activeList.get(i)); if (hit) {
				 * c.SetHitColorDebug(true);
				 * _activeList.get(i).SetHitColorDebug(true); }
				 */
			}

		}
		
		_activeList.add(c);
	}

	@Override
	public void Update(float deltaTime)
	{
		QuickSort();
		_activeList.clear();
		
		if (_firstTimeAddToActiveList) // only do this one time
		{
			_activeList.add(_colliderList.get(0));
			_firstTimeAddToActiveList = true;
		}

		// reset all collider hits
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);
			a.SetHitColorDebug(false);

			a.Update(deltaTime);
		}

		// doing the actual sweep and pruning
		for (int i = 0; i < _colliderList.size(); i++)
		{
			AddToActiveList(_colliderList.get(i));
		}

		// debug texts
		String colliders = "";
		for (int i = 0; i < _activeList.size();i++)
			colliders += "\n" + _activeList.get(i).Owner.Name;
		
		DebugSystem.AddDebugText("Colliders in ActiveList: " + colliders, new Vector2(300, 400));
		
		//DebugSystem.AddDebugText("# collision checks (SAP): " + Math.pow(_activeList.size(), 2), null);
		//DebugSystem.AddDebugText("# collision checks (brute force): " + Math.pow(_colliderList.size(), 2), null);

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
