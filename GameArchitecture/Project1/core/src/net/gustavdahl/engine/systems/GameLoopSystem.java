package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.IUpdatable;
import net.gustavdahl.engine.components.PhysicsComponent;

public class GameLoopSystem extends BaseSystem
{	
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
		if (_gameLoop == null)
		{
			System.out.println("ERROR - game loop is null!");
			return;
		}
		_gameLoop.Update(deltaTime);
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		// TODO: move away?
		throw new RuntimeException("ERROR - cannot add " + c.Name() + " to the GameLoopSystem! Add an IUpdtable via the constructor instead.");
	}




}
