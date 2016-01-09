package net.gustavdahl.echelonengine.components.persistence;

import net.gustavdahl.echelonengine.components.TransFormComponent;
import net.gustavdahl.echelonengine.entities.EntityFactory;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class StaticManPersistence extends PersistableComponent
{

	public StaticManPersistence()
	{

	}

	@Override
	public LevelCommand CreateCommand()
	{
		return new CreateCommand(this.Owner.Name, this.Transform);
	}

	private static class CreateCommand implements LevelCommand
	{
		private static final long serialVersionUID = 2462369320459303180L;

		private float posX;
		private float posY;
		private float angle;
		private int colorIndex;
		private String _name;

		public CreateCommand(String name, TransFormComponent transform)
		{
			posX = transform.PositionX;
			posY = transform.PositionY;
			angle = transform.Rotation;
			_name = name;
		}

		public void Execute()
		{
			// ServiceManager.getService(EntityFactory.class).createCogWheel1(colorIndex,
			// posX, posY, angle);
			
			ServiceLocator.EntityFactory.CreateStaticManWithBoxCollider(_name, posX, posY);
		}
	}
}