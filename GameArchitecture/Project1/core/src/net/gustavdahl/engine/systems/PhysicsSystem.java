package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.IRenderable;
import net.gustavdahl.engine.components.PhysicsComponent;

public class PhysicsSystem implements ISystem
{
	public static final String SystemName = PhysicsSystem.class.getSimpleName();
	
	private List<PhysicsComponent> _componentList;
	private float deltaTime = 1f;
	
	public boolean IsActive = true;

	public PhysicsSystem()
	{
		_componentList = new ArrayList<PhysicsComponent>();
	}

	@Override
	public void Start()
	{
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (_componentList.get(i).HasBeenInitialized())
				continue;

			_componentList.get(i).Initialize();
		}
	}


	@Override
	public void Update()
	{
		if (!IsActive)
			return;

		for (int i = 0; i < _componentList.size(); i++)
		{
			if (!_componentList.get(i).IsActive())
				continue;

			_componentList.get(i).Update();

		}

	}


	public int ActiveComponents()
	{
		return _componentList.size();
	}

	@Override
	public void Destroy()
	{
		System.out.println("[Destroying " + this.getClass().getSimpleName() + "]");

		for (int i = 0; i < _componentList.size(); i++)
		{
			// _componentList.get(i).Owner.RemoveComponentOfType(_componentList.get(i).getClass());
			// _componentList.get(i).Owner.RemoveAllComponents();
			// TODO: also destroy the entity itself! (maybe?)
		}

		// _componentList.clear();
		// _componentList = null;

	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;
		
		if (c instanceof PhysicsComponent)
		{
			succesfullyAdded = true;
			_componentList.add((PhysicsComponent) c);
			
		}
		else
		{
            try
			{
				throw new Exception("ERROR - component " + c.getClass().getSimpleName() + " doesn't implement PhysicsComponent interface!");
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		//System.out.println(added);
		
		return succesfullyAdded;
	}

}
