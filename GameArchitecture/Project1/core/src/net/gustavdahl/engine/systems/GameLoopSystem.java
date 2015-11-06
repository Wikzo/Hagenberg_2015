package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.IUpdatable;
import net.gustavdahl.engine.components.PhysicsComponent;

public class GameLoopSystem extends BaseSystem
{

	public static final String SystemName = GameLoopSystem.class.getSimpleName();
	
	//private List<IUpdatable> _componentList;
	private IUpdatable _gameLoop;
	
	public boolean IsActive = true;
	
	public GameLoopSystem(IUpdatable gameLoop)
	{
		super();
		_gameLoop = gameLoop;
	}
	
	@Override
	public void Update(float deltaTime)
	{
		_gameLoop.Update(deltaTime);
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		try
		{
			throw new Exception("ERROR - cannot add " + c.Name() + " to the GameLoopSystem! Add an IUpdtable via the constructor instead.");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}




}
