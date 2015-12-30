package net.gustavdahl.echelonengine.scenes;

//import static org.junit.Assert.assertNotNull;

import java.io.Console;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;

import net.gustavdahl.echelonengine.components.BoxCollider;
import net.gustavdahl.echelonengine.components.CircleCollider;
import net.gustavdahl.echelonengine.components.Collider;
import net.gustavdahl.echelonengine.components.ConstantForce;
import net.gustavdahl.echelonengine.components.DebugComponent;
import net.gustavdahl.echelonengine.components.EditorComponent;
import net.gustavdahl.echelonengine.components.IUpdatable;
import net.gustavdahl.echelonengine.components.PhysicsBody;
import net.gustavdahl.echelonengine.components.SpringComponent;
import net.gustavdahl.echelonengine.components.SpriteAnimator;
import net.gustavdahl.echelonengine.components.SpriteComponent;
import net.gustavdahl.echelonengine.components.Text;
import net.gustavdahl.echelonengine.components.TransFormComponent;
import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.entities.EntityFactory;
import net.gustavdahl.echelonengine.enums.ForceMode;
import net.gustavdahl.echelonengine.systems.ColliderSystem;
import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.EditorSystem;
import net.gustavdahl.echelonengine.systems.GameLoopSystem;
import net.gustavdahl.echelonengine.systems.MyAssetManager;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class TestLevel implements Screen, IUpdatable
{
	public static OrthographicCamera _camera; // TODO: don't make public static
	private SpriteBatch _spriteBatch;
	private ServiceLocator _serviceLocator;
	private EntityFactory _entityFactory;

	Game _game;
	Entity _entity1;
	Entity _entity2;
	Entity _entity3;

	public TestLevel(Game game, ServiceLocator serviceLocator)
	{
		_game = game;

		InitializeSystems(serviceLocator);

		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);

		create();
	}

	DebugSystem _debugSystem;

	void InitializeSystems(ServiceLocator serviceLocator)
	{
		RenderSystem _renderSystem = new RenderSystem(ServiceLocator.AssetManager.SpriteBatch);
		_debugSystem = new DebugSystem(ServiceLocator.AssetManager.SpriteBatch,
				ServiceLocator.AssetManager.ShapeRenderer, ServiceLocator.AssetManager.DebugFont, _camera);
		PhysicsSystem _physicsSystem = new PhysicsSystem(60d).SetUseFixedUpdate(true).SetForceMode(ForceMode.ExplicitEuler);
		ColliderSystem _colliderSystem = new ColliderSystem();
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

	}

	TextureRegion[] r;

	public void create()
	{

		
		_entityFactory.CreateSingleSpring("Cog_Single", 200, 300);
		_entityFactory.CreateMultipleSprings("Cog_multi", 300, 400, 4);
		
		Entity e1 = _entityFactory.CreateStaticMan("Static1", 100, 200);
		Entity e2 = _entityFactory.CreateStaticMan("Static2", 500, 200);
		Entity e4 = _entityFactory.CreateAnimatedMan("Animated_1", 700, 200);
		
		
	}

	float fps = 1;
	@Override
	public void render(float delta)
	{

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_camera.update();

		ServiceLocator.AssetManager.SpriteBatch.setProjectionMatrix(_camera.combined);
		ServiceLocator.AssetManager.ShapeRenderer.setProjectionMatrix(_camera.combined);

		_debugSystem.GetCamera(_camera);

		_serviceLocator.UpdateSystems(delta);

	}

	private float _timer;

	@Override
	public void Update(float deltaTime)
	{
		
		 //_timer+= deltaTime;

		if (_timer > 0.01)
		{
			_timer = 0;

			float x = MathUtils.random(Gdx.graphics.getWidth() + 800);
			float y = MathUtils.random(Gdx.graphics.getHeight() + 800);
			
			_entityFactory.CreateStaticMan("StressTest", x, y);

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

	}
}
