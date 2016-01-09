package net.gustavdahl.echelonengine.systems;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.IUpdatable;

public class GameLoopSystem extends BaseSystem
{	
	// TODO: remove this?
	
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
	public boolean ValidateIfComponentCanBeAddedToSystem(Component c)
	{
		return false;
		
		/*// TODO: move away?
		throw new RuntimeException("ERROR - cannot add " + c.Name() + " to the GameLoopSystem! Add an IUpdtable via the constructor instead.");
		*/
	}




}
