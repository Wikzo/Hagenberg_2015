package net.gustavdahl.echelonengine.components.persistence;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.gustavdahl.echelonengine.components.TransFormComponent;
import net.gustavdahl.echelonengine.components.visual.IDebugRenderable;
import net.gustavdahl.echelonengine.components.visual.SpriteComponent;
import net.gustavdahl.echelonengine.entities.EntityFactory;
import net.gustavdahl.echelonengine.systems.PersistenceSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class StaticManPersistence extends PersistableComponent
{

	public StaticManPersistence()
	{
		_defaultSystem = PersistenceSystem.class;
	}

	@Override
	public CreateEntityCommand CreateCommand()
	{
		return new CreateCommand(this.Owner.Name, this.Transform);
	}

	private static class CreateCommand implements CreateEntityCommand
	{
		private static final long serialVersionUID = 2462369320459303180L;

		private String _name;
		private float _posX;
		private float _posY;
		private float _rotation;
		private float _scaleX;
		private float _scaleY;

		public CreateCommand(String name, TransFormComponent transform)
		{
			_name = name;
			_posX = transform.PositionX;
			_posY = transform.PositionY;
			_rotation = transform.Rotation;
			_scaleX = transform.ScaleX;
			_scaleY = transform.ScaleY;

		}

		public void Execute()
		{
			ServiceLocator.EntityFactory.CreateStaticManWithBoxCollider(_name, _posX, _posY, _rotation, _scaleX,
					_scaleY);
		}
	}
}