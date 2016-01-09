package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.persistence.Persistable;
import net.gustavdahl.echelonengine.components.physics.IPhysics;

public class PersistenceSystem extends BaseSystem
{

	private List<Persistable> _persistables;
	
	public PersistenceSystem()
	{
		_persistables = new ArrayList<Persistable>();
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof Persistable)
		{
			succesfullyAdded = true;
			_persistables.add((Persistable) c);

		} else
			throw new RuntimeException("ERROR - component " + c.getClass().getSimpleName()
					+ " doesn't implement PhysicsComponent interface!");

		return succesfullyAdded;
	}

}
