package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.PhysicsComponent;

public class GameLogicSystem implements ISystem
{

	public static final String SystemName = GameLogicSystem.class.getSimpleName();
	
	private List<Component> _componentList;
	
	public boolean IsActive = true;
	
	public GameLogicSystem()
	{
		_componentList = new ArrayList<Component>();
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
		for (int i = 0; i < _componentList.size();i++)
		{
			if (!_componentList.get(i).IsActive())
				continue;
			
			_componentList.get(i).Update();
			
		}
		
	}

	@Override
	public void Destroy()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		_componentList.add(c);
		return true;
	}

}
