package net.gustavdahl.echelonengine.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import net.gustavdahl.echelonengine.components.IUpdatable;
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
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class BruteForceCollisionScene implements Screen, IUpdatable
{
	public static OrthographicCamera _camera; // TODO: don't make public static
	private SpriteBatch _spriteBatch;
	private ServiceLocator _serviceLocator;
	private EntityFactory _entityFactory;
	private Game _game;
	private CircleMenuList _circleMenu;

	public BruteForceCollisionScene(Game game, CircleMenuList circleMenu, ServiceLocator serviceLocator)
	{
		_game = game;
		_circleMenu = circleMenu;

		InitializeSystems(serviceLocator);

		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);

		create();
	}

	DebugSystem _debugSystem;

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

		_serviceLocator = serviceLocator;
		_serviceLocator.RegisterNewSystem(_renderSystem);
		_serviceLocator.RegisterNewSystem(_debugSystem);
		_serviceLocator.RegisterNewSystem(_physicsSystem);
		_serviceLocator.RegisterNewSystem(_colliderSystem);
		_serviceLocator.RegisterNewSystem(_gameLogicSystem);
		_serviceLocator.RegisterNewSystem(_editorSystem);
		_serviceLocator.InitializeSystems();

		_entityFactory = _serviceLocator.EntityFactory;
		
		_debugSystem.SetActive(true);

	}
	
	// TODO:
	// Toggle between fixed/varied update rate
	// Toggle between spawning/non spawning

	public void create()
	{
		Entity e1 = _entityFactory.CreateStaticManWithBoxCollider("Static1", 100, 200);
		Entity e2 = _entityFactory.CreateStaticManWithCircleCollider("Static2", 500, 200);
		Entity e4 = _entityFactory.CreateAnimatedMan("Animated_1", 700, 200);

	}
	
	@Override
	public void render(float delta)
	{

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_camera.update();

		ServiceLocator.AssetManager.SpriteBatch.setProjectionMatrix(_camera.combined);
		ServiceLocator.AssetManager.ShapeRenderer.setProjectionMatrix(_camera.combined);

		_debugSystem.GetCamera(_camera);
		ServiceLocator.GetSystem(EditorSystem.class).SetCamera(_camera);

		_serviceLocator.UpdateSystems(delta);

	}

	private float _timer;
	private boolean _spawnEntities = false;
	private boolean _useFixedUpdate = true;

	@Override
	public void Update(float deltaTime)
	{
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			_game.setScreen(_circleMenu);
		
		if (Gdx.input.isKeyJustPressed(Keys.O))
			_spawnEntities = !_spawnEntities;
		
		if (Gdx.input.isKeyJustPressed(Keys.P))
		{
			_useFixedUpdate = !_useFixedUpdate;
			ServiceLocator.GetSystem(PhysicsSystem.class).SetUseFixedUpdate(_useFixedUpdate);
		}
		
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText(" ");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("-- Brute Force Collision --");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("PRESS O TO TOGGLE ENTITIES SPAWNING");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("PRESS P TO TOGGLE FIXED/VARIED PHYSICS UPDATE");

		if (_spawnEntities)
			_timer+= deltaTime;

		if (_timer > 0.01)
		{
			_timer = 0;

			float x = MathUtils.random(Gdx.graphics.getWidth() + 800);
			float y = MathUtils.random(Gdx.graphics.getHeight() + 800);

			Entity e = _entityFactory.CreateAnimatedMan("StressTest_Man", x, y);
			e.AddComponent(new PhysicsBody().AddConstantForce(PhysicsBody.GravityForce));

		}

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
