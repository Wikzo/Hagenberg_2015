package net.gustavdahl.projectone;

import static org.junit.Assert.assertNotNull;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.gustavdahl.projectone.EntityComponentSystem.Entity;
import net.gustavdahl.projectone.EntityComponentSystem.RenderSystem;
import net.gustavdahl.projectone.EntityComponentSystem.ServiceLocator;
import net.gustavdahl.projectone.EntityComponentSystem.TextComponent;

public class GameTest implements Screen
{

	private OrthographicCamera  _camera;
	private SpriteBatch _spriteBatch;
	private ServiceLocator _serviceLocator;
	private RenderSystem _renderSystem;
	private Assets _assetManager;
	
	Game _game;
	Entity _entity;
	
	public GameTest(Game game, ServiceLocator serviceLocator)
	{
		_game = game;
		
		_assetManager = serviceLocator.AssetManager;
		
		_serviceLocator = serviceLocator;
		_renderSystem = new RenderSystem(Assets.SpriteBatch); // TODO: make render system part of the ServiceLocator!
		
		_serviceLocator.RegisterSystem(_renderSystem);
		_serviceLocator.InitializeSystems();
		_serviceLocator.StartSystems();
		
		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);
		
		create();
	}

	
	public void create()
	{
		// TODO Auto-generated method stub
		
		//SpriteBatch sb = new SpriteBatch();
		//assertNotNull(sb);

		assertNotNull(_serviceLocator);
		
		assertNotNull(_renderSystem);

		_entity = new Entity();
		assertNotNull(_entity);

		TextComponent component1 = new TextComponent(_serviceLocator.AssetManager.SpriteBatch, "Comp1");
		assertNotNull(component1);

		TextComponent component2 = new TextComponent(_serviceLocator.AssetManager.SpriteBatch, "Comp2");
		assertNotNull(component2);

		_entity.AddComponent(component1);
		_entity.AddComponent(component2);
		
		_renderSystem.AddToRenderSystem(component1);
		
	}

	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		_camera.update();

	    Assets.SpriteBatch.setProjectionMatrix(_camera.combined);
	    
	    _serviceLocator.UpdateSystems();
	}
	
	@Override
	public void dispose()
	{
		_serviceLocator.DestroyAllSystems();
		Assets.DisposeAllAssets();
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
}
