package net.gustavdahl.echelonengine.components.persistence;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.systems.PersistenceSystem;

public abstract class Persistable extends Component
{
	
	public Persistable()
	{
		DefaultSystem = PersistenceSystem.class;
	}

	@Override
	public void Update(float deltaTime)
	{
	}

	public abstract LevelCommand CreateCommand();

}
