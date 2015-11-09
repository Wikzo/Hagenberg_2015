package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.compression.lzma.Base;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.IRenderable;
import net.gustavdahl.engine.components.PhysicsComponent;

public class PhysicsSystem extends BaseSystem
{

	public PhysicsSystem()
	{
		super();
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof PhysicsComponent)
		{
			succesfullyAdded = true;
			_componentList.add((PhysicsComponent) c);

		} else
			throw new RuntimeException("ERROR - component " + c.getClass().getSimpleName()
					+ " doesn't implement PhysicsComponent interface!");

		return succesfullyAdded;
	}

}
