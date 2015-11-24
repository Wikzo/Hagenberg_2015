package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.gustavdahl.echelonengine.components.Collider;
import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.ICollider;
import net.gustavdahl.echelonengine.components.IDebugRenderable;

public class ColliderSystem extends BaseSystem
{

	private List<Collider> _colliderList;
	private List<Collider> _activeList;

	public ColliderSystem()
	{
		_colliderList = new ArrayList<Collider>();
		_activeList = new ArrayList<Collider>();

	}

	boolean oneTime;

	void CheckSort()
	{
		if (!oneTime)
		{
			oneTime = true;

			System.out.println("Before sorting: ");
			for (Collider c : _colliderList)
			{
				System.out.println(c.GetLeftSide());
			}

			Collections.sort(_colliderList);

			System.out.println("After sorting: ");
			for (Collider c : _colliderList)
			{
				System.out.println(c.GetLeftSide());
			}
		}
	}

	// for loop with body/axis list feeding in to AddToActiveList
	// _activeList.add(newC);

	void AddToActiveList(int index)
	{
		// System.out.println("Size: " + _activeList.size());

		// System.out.println(_colliderList.get(index).GetLeftSide());

		// for (int i = 0; i < _activeList.size(); i++)
		for (int i = _activeList.size() - 1; i > 0; i--)
		{
			// new one not colliding with old one?
			if (_colliderList.get(index).GetLeftSide() > _activeList.get(i).GetRightSide())
			{
				System.out.println("Potential collision: NO");
				 _activeList.remove(i);
			} else
			{
				System.out.println("Potential collision: YES: " + _colliderList.get(index).Owner.Name + " and "
						+ _activeList.get(i).Owner.Name);
				 _activeList.add(_colliderList.get(index));

			}

		}

	}

	boolean firstTime;

	@Override
	public void Update(float deltaTime)
	{

		CheckSort();

		// add to active list
		//_activeList.clear();

		if (!firstTime)
		{
			_activeList.add(_colliderList.get(0));
			firstTime = true;
			System.out.println("Size: " + _activeList.size());
		}
		for (int i = 0; i < _colliderList.size(); i++)
		{
			//System.out.println("SAP'ing");
			AddToActiveList(i);
		}

		DebugSystem.AddDebugText("SAP: " + _activeList.size(), null);

		// reset all collider hits
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);
			a.SetHitColorDebug(false);

			a.Update(deltaTime);
		}

		DebugSystem.AddDebugText("Number of collision checks: " + Math.pow(_colliderList.size(), 2), null);
		// check all pairs against each other
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);

			for (int j = i + 1; j < _colliderList.size(); j++)
			{
				Collider b = _colliderList.get(j);

				// boolean hit = a.IsHit(b);
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
