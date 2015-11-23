package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.echelonengine.components.Collider;
import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.ICollider;
import net.gustavdahl.echelonengine.components.IDebugRenderable;

public class ColliderSystem extends BaseSystem
{

	private List<Collider> _colliderList;
	
	public ColliderSystem()
	{
		_colliderList = new ArrayList<Collider>();
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

		// check all pairs against each other
		for (int i = 0; i < _colliderList.size(); i++)
		{
			Collider a = _colliderList.get(i);
			
			for (int j = i+1; j < _colliderList.size();j++)
			{
				Collider b = _colliderList.get(j);

				//boolean hit = a.IsHit(b);
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
			throw new RuntimeException(
					"ERROR - component " + c.getClass().getSimpleName() + " is not a Collider!!");

		return succesfullyAdded;
	}

}
