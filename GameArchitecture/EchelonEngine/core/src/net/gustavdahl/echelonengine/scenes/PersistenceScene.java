package net.gustavdahl.echelonengine.scenes;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.compression.lzma.Base;

import net.gustavdahl.echelonengine.components.IUpdatable;
import net.gustavdahl.echelonengine.components.persistence.CreateEntityCommand;
import net.gustavdahl.echelonengine.components.persistence.PersistableComponent;
import net.gustavdahl.echelonengine.components.persistence.StaticManPersistence;
import net.gustavdahl.echelonengine.components.physics.PhysicsBody;
import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.entities.EntityFactory;
import net.gustavdahl.echelonengine.enums.CollisionMode;
import net.gustavdahl.echelonengine.enums.ForceMode;
import net.gustavdahl.echelonengine.menus.CircleMenuList;
import net.gustavdahl.echelonengine.systems.ColliderSystem;
import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.EditorSystem;
import net.gustavdahl.echelonengine.systems.GameLoopSystem;
import net.gustavdahl.echelonengine.systems.PersistenceSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class PersistenceScene extends BaseScene
{
	
	final float _savingInterval = 10f;
	float _time = 0f;
	
	private final String _description1 = "-- Persistence Scene --\n";
	private final String _description2 = "The states of the entities (position, rotation and scale)\n";
	private final String _description3 = "will be stored upon exiting the scene.\n";
	private final String _description4 = "When the scene is loaded again, their states will be re-created.\n";
	private final String _description5 = "This is achieved by storing the commands to create the entities.";
	private final String _fullDescription = _description1 + _description2 + _description3 + _description4 + _description5;
	
	public PersistenceScene(Game game, CircleMenuList circleMenu, ServiceLocator serviceLocator)
	{
		super(game, circleMenu, serviceLocator);
	}

	void InitializeSystems(ServiceLocator serviceLocator)
	{
		super.InitializeSystems(serviceLocator);
	}

	protected boolean CanLoadSceneFromFile()
	{
		String name = this.getClass().getSimpleName() + ".txt";
		boolean exists = Gdx.files.local(name).exists();

		if (!exists)
			return false;
		else
		{
			FileHandle myFile = Gdx.files.local(name);

			try
			{
				_serviceLocator.GetSystem(PersistenceSystem.class).restore(myFile.file());
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			return true;
		}

	}

	protected void SaveSceneToFile()
	{
		String name = this.getClass().getSimpleName() + ".txt";
		FileHandle myFile = Gdx.files.local(name);

		try
		{
			_serviceLocator.GetSystem(PersistenceSystem.class).store(myFile.file());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void CreateScene()
	{
		if (!CanLoadSceneFromFile())
		{
			_entityFactory.CreateStaticManWithBoxCollider("Persistent_Man1", 100, 100, 0, 1, 1);
			_entityFactory.CreateStaticManWithBoxCollider("Persistent_Man2", 200, 100, 0, 1, 1);
			_entityFactory.CreateStaticManWithBoxCollider("Persistent_Man3", 300, 100, 0, 1, 1);
			_entityFactory.CreateStaticManWithBoxCollider("Persistent_Man4", 400, 100, 0, 1, 1);
		}

	}

	@Override
	public void render(float delta)
	{
		super.render(delta);

		_spriteBatch.begin();
		ServiceLocator.AssetManager.DebugFont2.draw(ServiceLocator.AssetManager.SpriteBatch, _fullDescription, 30, 450);
		_spriteBatch.end();

	}

	
	@Override
	public void UpdateScene(float deltaTime)
	{
		_time += deltaTime;
		
		if (_time > _savingInterval)
			SaveSceneToFile();
	}

	@Override
	public void hide()
	{
		SaveSceneToFile();
		super.hide();
	}
	
	@Override
	public void dispose()
	{
		SaveSceneToFile();
		super.dispose();
	}
}
