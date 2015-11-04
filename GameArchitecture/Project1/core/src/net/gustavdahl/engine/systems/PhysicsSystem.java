package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.ISystem;

public class PhysicsSystem implements ISystem
{
	private List<Component> _componentList;
	
	public boolean IsActive = true;

	public PhysicsSystem()
	{
		_componentList = new ArrayList<Component>();
	}

	@Override
	public void Initialize()
	{
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (_componentList.get(i).HasBeenInitialized())
				continue;

			_componentList.get(i).Initialize();
		}
	}

	@Override
	public void Start()
	{
		// TODO Auto-generated method stub

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

	public void AddToPhysicsSystem(Component c)
	{
		_componentList.add(c);
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
		// TODO Auto-generated method stub
		return false;
	}

}
