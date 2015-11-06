package net.gustavdahl.engine;

import static org.junit.Assert.assertNotNull;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.components.IUpdatable;
import net.gustavdahl.engine.components.SpriteComponent;
import net.gustavdahl.engine.components.TextComponent;
import net.gustavdahl.engine.components.TransFormComponent;
import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.Assets;
import net.gustavdahl.engine.systems.GameLoopSystem;
import net.gustavdahl.engine.systems.PhysicsSystem;
import net.gustavdahl.engine.systems.RenderSystem;
import net.gustavdahl.engine.systems.ServiceLocator;

public class GameTest implements Screen, IUpdatable
{

	private OrthographicCamera _camera;
	private SpriteBatch _spriteBatch;
	private ServiceLocator _serviceLocator;
	private Assets _assetManager;

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
		_assetManager = serviceLocator.AssetManager;

		RenderSystem _renderSystem = new RenderSystem(ServiceLocator.AssetManager.SpriteBatch);
		PhysicsSystem _physicsSystem = new PhysicsSystem();
		GameLoopSystem _gameLogicSystem = new GameLoopSystem(this);

		_serviceLocator = serviceLocator;
		_serviceLocator.RegisterNewSystem(_renderSystem);
		_serviceLocator.RegisterNewSystem(_physicsSystem);
		_serviceLocator.RegisterNewSystem(_gameLogicSystem);
		_serviceLocator.InitializeSystems();
	}

	public void create()
	{
		// TODO Auto-generated method stub

		// SpriteBatch sb = new SpriteBatch();
		// assertNotNull(sb);

		assertNotNull(_serviceLocator);

		// assertNotNull(_renderSystem);

		_entity = new Entity();
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
		_entity.AddComponent(new SpriteComponent(_assetManager.SpriteBatch, _assetManager.DummyTexture),
				RenderSystem.SystemName);
				// _renderSystem.AddToRenderSystem((SpriteComponent)
				// _entity.GetComponent(SpriteComponent.class));

		// _serviceLocator.AddComponentToSystem(_entity.GetComponent(SpriteComponent.class),
		// RenderSystem.SystemName);
		// System.out.println("Size: "+ _renderSystem.ActiveComponents());

	}

	@Override
	public void render(float delta)
	{

		// TODO: maybe make this class an IUpdatable as well
		// then GameLogicSystem will class this update

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_camera.update();

		ServiceLocator.AssetManager.SpriteBatch.setProjectionMatrix(_camera.combined);

		_serviceLocator.UpdateSystems(delta);

		// _renderSystem.IsActive = false;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void Update(float deltaTime)
	{
		// System.out.println("update");

		_entity.GetTransform().Translate(new Vector2(1, 0));

		if (Gdx.input.isKeyJustPressed(Keys.P))
			// _entity.GetTransform().SetActive(!_entity.GetTransform().IsActive());
			_entity.SetActive(!_entity.GetActive());

	}
}
