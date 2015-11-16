package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.engine.components.Collider;
import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.ICollider;
import net.gustavdahl.engine.components.IDebugRenderable;

public class ColliderSystem extends BaseSystem
{

	private List<ICollider> _colliderList;
	
	public ColliderSystem()
	{
		_colliderList = new ArrayList<ICollider>();
	}
	
	@Override
	public void Update(float deltaTime)
	{
		// reset all collider hits
		for (int i = 0; i < _colliderList.size(); i++)
		{
			ICollider a = _colliderList.get(i);
			((Collider) a).SetHitColorDebug(false);
		}

		// check all pairs against each other
		for (int i = 0; i < _colliderList.size(); i++)
		{
			ICollider a = _colliderList.get(i);
			
			for (int j = i+1; j < _colliderList.size();j++)
			{
				ICollider b = _colliderList.get(j);

				boolean hit = a.IsHit(b);
				if (hit)
				{
					((Collider) a).SetHitColorDebug(true);
					((Collider) b).SetHitColorDebug(true);
				}
			}
		}
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof ICollider)
		{
			succesfullyAdded = true;
			_colliderList.add((ICollider) c);
			System.out.println(c.Name() + " was added to ColliderSystem");

		} else
			throw new RuntimeException(
					"ERROR - component " + c.getClass().getSimpleName() + " doesn't implement ICollider!");

		return succesfullyAdded;
	}

}
