package net.gustavdahl.projectone;

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

import net.gustavdahl.projectone.EntityComponentSystem.Entity;
import net.gustavdahl.projectone.EntityComponentSystem.RenderSystem;
import net.gustavdahl.projectone.EntityComponentSystem.ServiceLocator;
import net.gustavdahl.projectone.EntityComponentSystem.SpriteComponent;
import net.gustavdahl.projectone.EntityComponentSystem.TextComponent;
import net.gustavdahl.projectone.EntityComponentSystem.TransFormComponent;

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
		_renderSystem = new RenderSystem(ServiceLocator.AssetManager.SpriteBatch); // TODO: make render system part of the ServiceLocator!
		
		_serviceLocator.RegisterNewSystem(_renderSystem);
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
		assertNotNull(_entity.GetTransform());

		TextComponent component1 = new TextComponent(_serviceLocator.AssetManager.SpriteBatch, "Comp1");
		assertNotNull(component1);
		

		TextComponent component2 = new TextComponent(_serviceLocator.AssetManager.SpriteBatch, "Comp2");
		assertNotNull(component2);

		assertNotNull(_entity.GetTransform());
		
		_entity.AddComponent(component1);
		assertNotNull(component1.Owner);

		//_entity.GetComponent(TextComponent.class).Disable();
		//_entity.AddComponent(component2);
			
		//_renderSystem.AddToRenderSystem(component1);
		//_renderSystem.AddToRenderSystem(component2);
		
		 _entity.AddComponent(new SpriteComponent(_assetManager.SpriteBatch, _assetManager.DummyTexture));
		//_renderSystem.AddToRenderSystem((SpriteComponent) _entity.GetComponent(SpriteComponent.class));
		
		_serviceLocator.AddComponentToSystem(_entity.GetComponent(SpriteComponent.class), RenderSystem.SystemName);
		//System.out.println("Size: "+ _renderSystem.ActiveComponents());
		
		
	}

	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		_camera.update();

		ServiceLocator.AssetManager.SpriteBatch.setProjectionMatrix(_camera.combined);
	    
	    _serviceLocator.UpdateSystems();
	    
	    _entity.GetTransform().Translate(new Vector2(1,0));
	    
	    if (Gdx.input.isKeyJustPressed(Keys.P))
	    	_entity.GetTransform().SetActive(!_entity.GetTransform().IsActive());
	    
	    //_renderSystem.IsActive = false;
	   
	    
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
}
