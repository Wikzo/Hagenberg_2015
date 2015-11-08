package net.gustavdahl.engine;

import static org.junit.Assert.assertNotNull;

import java.io.Console;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.gustavdahl.engine.components.IUpdatable;
import net.gustavdahl.engine.components.ConstantForce;
import net.gustavdahl.engine.components.DebugComponent;
import net.gustavdahl.engine.components.SpriteAnimator;
import net.gustavdahl.engine.components.SpriteComponent;
import net.gustavdahl.engine.components.Text;
import net.gustavdahl.engine.components.TransFormComponent;
import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.MyAssetManager;
import net.gustavdahl.engine.systems.DebugSystem;
import net.gustavdahl.engine.systems.GameLoopSystem;
import net.gustavdahl.engine.systems.PhysicsSystem;
import net.gustavdahl.engine.systems.RenderSystem;
import net.gustavdahl.engine.systems.ServiceLocator;

public class GameTest implements Screen, IUpdatable
{

	private OrthographicCamera _camera;
	private SpriteBatch _spriteBatch;
	private ServiceLocator _serviceLocator;

	Game _game;
	Entity _entity;

	public GameTest(Game game, ServiceLocator serviceLocator)
	{
		_game = game;

		InitializeSystems(serviceLocator);

		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);

		create();
	}

	void InitializeSystems(ServiceLocator serviceLocator)
	{
		RenderSystem _renderSystem = new RenderSystem(ServiceLocator.AssetManager.SpriteBatch);
		DebugSystem _debugSystem = new DebugSystem(ServiceLocator.AssetManager.SpriteBatch);
		PhysicsSystem _physicsSystem = new PhysicsSystem();
		GameLoopSystem _gameLogicSystem = new GameLoopSystem(this);
		
		_serviceLocator = serviceLocator;
		_serviceLocator.RegisterNewSystem(_renderSystem);
		_serviceLocator.RegisterNewSystem(_debugSystem);
		_serviceLocator.RegisterNewSystem(_physicsSystem);
		_serviceLocator.RegisterNewSystem(_gameLogicSystem);
		_serviceLocator.InitializeSystems();
	}

	public void create()
	{

		assertNotNull(_serviceLocator);


		_entity = new Entity("BraidGuy");
		assertNotNull(_entity);
		assertNotNull(_entity.GetTransform());

		// TextComponent component1 = new
		// TextComponent(_serviceLocator.AssetManager.SpriteBatch, "Comp1");
		// assertNotNull(component1);

		// TextComponent component2 = new
		// TextComponent(_serviceLocator.AssetManager.SpriteBatch, "Comp2");
		// assertNotNull(component2);

		assertNotNull(_entity.GetTransform());

		// _entity.AddComponent(component1, GameLoopSystem.SystemName);
		// assertNotNull(component1.Owner);

		// _entity.GetComponent(TextComponent.class).Disable();
		// _entity.AddComponent(component2);

		// _renderSystem.AddToRenderSystem(component1);
		// _renderSystem.AddToRenderSystem(component2);

		// _entity.AddComponent(new SpriteComponent(_assetManager.SpriteBatch,
		// _assetManager.DummyTexture));



		Texture texture = _serviceLocator.AssetManager.BraidSpriteSheet;

        TextureRegion[] r = SpriteAnimator.CreateSpriteSheet(texture, 27, 7, 4);
        
        // sprite animation
       _entity.AddComponent(new SpriteAnimator(r, 0.032f)
        		.Color(Color.BLUE)
        		.Offset(100, 0)
        		.SetOriginCenter(), RenderSystem.SystemName);
        
       // static sprite
        _entity.AddComponent(new SpriteComponent(r[0])
        		.Color(Color.RED), RenderSystem.SystemName);
        
        // constant force
       //_entity.AddComponent(new ConstantForce(Vector2.Zero, 2f), PhysicsSystem.SystemName);
       
       
       _entity.AddComponent(new DebugComponent(_serviceLocator.AssetManager.DebugFont)
    		   .SetRenderPosition(true)
    		   .SetRenderAllComponents(true),
    		   DebugSystem.SystemName);
       
       
       _serviceLocator.GetSystem(PhysicsSystem.SystemName).SetActive(true);

	}

	@Override
	public void render(float delta)
	{

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_camera.update();

		ServiceLocator.AssetManager.SpriteBatch.setProjectionMatrix(_camera.combined);

		_serviceLocator.UpdateSystems(delta);


		// TODO: remember to apply stage viewport for camera

		// TODO: get PickRay (for mouse input editor) via camera

		// TODO: make camera system independt of render system

		// TODO: physics (box-aligned vs. axis-aligned bounding box)

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

	@Override
	public void Update(float deltaTime)
	{
			
		if (Gdx.input.isTouched())
		{
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			_camera.unproject(touchPos);

			_entity.SetTransform(new Vector2(touchPos.x, touchPos.y));
			
			if (_entity.GetComponent(ConstantForce.class) != null)
				_entity.GetComponent(ConstantForce.class).SetActive(false);
			
		}
		else
		{
			if (_entity.GetComponent(ConstantForce.class) != null)
				_entity.GetComponent(ConstantForce.class).SetActive(true);
		}
		
		

	}
}
