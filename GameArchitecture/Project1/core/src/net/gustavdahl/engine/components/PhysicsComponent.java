package net.gustavdahl.engine.components;

import net.gustavdahl.engine.systems.PhysicsSystem;

public class PhysicsComponent extends Component
{

	public PhysicsComponent()
	{
		DefaultSystem = PhysicsSystem.class;
	}

	@Override
	public void Update(float deltaTime)
	{
		
	}

}
