package net.gustavdahl.echelonengine.scenes;

import java.io.IOException;
import java.sql.Time;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import net.gustavdahl.echelonengine.components.IUpdatable;
import net.gustavdahl.echelonengine.components.physics.PhysicsBody;
import net.gustavdahl.echelonengine.components.visual.SpriteComponent;
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

public abstract class BaseScene implements Screen, IUpdatable
{
	protected static OrthographicCamera _camera;
	protected SpriteBatch _spriteBatch;
	protected ServiceLocator _serviceLocator;
	protected EntityFactory _entityFactory;
	protected Game _game;
	protected CircleMenuList _circleMenu;
	protected DebugSystem _debugSystem;

	public BaseScene(Game game, CircleMenuList circleMenu, ServiceLocator serviceLocator)
	{
		_game = game;
		_circleMenu = circleMenu;

		InitializeSystems(serviceLocator);

		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);

		create();
	}

	

	void InitializeSystems(ServiceLocator serviceLocator)
	{
		_spriteBatch = ServiceLocator.AssetManager.SpriteBatch;
		RenderSystem _renderSystem = new RenderSystem(ServiceLocator.AssetManager.SpriteBatch);
		_debugSystem = new DebugSystem(ServiceLocator.AssetManager.SpriteBatch,
				ServiceLocator.AssetManager.ShapeRenderer, ServiceLocator.AssetManager.DebugFont, _camera);
		PhysicsSystem _physicsSystem = new PhysicsSystem(60d).SetUseFixedUpdate(true)
				.SetForceMode(ForceMode.ExplicitEuler);
		ColliderSystem _colliderSystem = new ColliderSystem(CollisionMode.BruteForce);
		GameLoopSystem _gameLogicSystem = new GameLoopSystem(this);
		EditorSystem _editorSystem = new EditorSystem(_camera);
		PersistenceSystem _persistenceSystem = new PersistenceSystem();

		_serviceLocator = serviceLocator;
		_serviceLocator.RegisterNewSystem(_renderSystem);
		_serviceLocator.RegisterNewSystem(_debugSystem);
		_serviceLocator.RegisterNewSystem(_physicsSystem);
		_serviceLocator.RegisterNewSystem(_colliderSystem);
		_serviceLocator.RegisterNewSystem(_gameLogicSystem);
		_serviceLocator.RegisterNewSystem(_editorSystem);
		_serviceLocator.RegisterNewSystem(_persistenceSystem);
		_serviceLocator.InitializeSystems();

		_entityFactory = _serviceLocator.EntityFactory;

		_debugSystem.SetActive(false);

	}

	public abstract void CreateScene();
	public abstract void UpdateScene(float deltaTime);

	public void create()
	{
		CreateScene();
	}

	@Override
	public void render(float delta)
	{
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Keys.F2))
			_game.setScreen(_circleMenu);			

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		UpdateCameras();

		_serviceLocator.UpdateSystems(delta);

	}
	

	protected void UpdateCameras()
	{
		_camera.update();
		ServiceLocator.AssetManager.SpriteBatch.setProjectionMatrix(_camera.combined);
		ServiceLocator.AssetManager.ShapeRenderer.setProjectionMatrix(_camera.combined);
		_debugSystem.GetCamera(_camera);
		ServiceLocator.GetSystem(EditorSystem.class).SetCamera(_camera);
	}

	@Override
	public void Update(float deltaTime)
	{
		UpdateScene(deltaTime);
	}

	@Override
	public void dispose()
	{
		_serviceLocator.DestroyAllSystems();
		ServiceLocator.AssetManager.DisposeAllAssets();
	}

	@Override
	public void show()
	{

	}

	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{
		// dispose();
	}
}
