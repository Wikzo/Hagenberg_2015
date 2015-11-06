package net.gustavdahl.engine;

import static org.junit.Assert.assertNotNull;

import java.io.Console;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.components.IUpdatable;
import net.gustavdahl.engine.components.SpriteSheetAnimatorComponent;
import net.gustavdahl.engine.components.SpriteComponent;
import net.gustavdahl.engine.components.TextComponent;
import net.gustavdahl.engine.components.TransFormComponent;
import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.MyAssetManager;
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

		/*Texture braid = _serviceLocator.AssetManager.DummyTexture;
		TextureRegion[] regions = new TextureRegion[32];

		float dimensionX = 1f / 7f;
		float dimensionY = 1f / 4f;

		int index = 0;
		for (int x = 0; x < 8; x++)
		{
			// System.out.println(dimensionY);

			for (int y = 0; y < 4; y++)
			{
				System.out.println("X: " + x + "; Y: " + y + "; Index: " + index);
				regions[index] = new TextureRegion(braid, x * 1, y * 1, 1, 1);

				System.out.println(
						"X: " + x * dimensionX + "; Y: " + y * dimensionY + "; W:" + dimensionX + "; H: " + dimensionY);
				
				index++;
			}
		}*/
		

		/*
		 * regions[0] = new TextureRegion(braid, 0, 0, 64, 64); // #3 regions[1]
		 * = new TextureRegion(braid, 0.166f, 0f, 0.166f, 0.25f); // #4
		 * regions[2] = new TextureRegion(braid, 0, 63, 64, 64); // #5
		 * regions[3] = new TextureRegion(braid, 0.5f, 0.5f, 1f, 1f); // #6
		 */

		/*
		 * _entity.AddComponent(new
		 * SpriteComponent(_serviceLocator.AssetManager.SpriteBatch,
		 * _serviceLocator.AssetManager.DummyTexture), RenderSystem.SystemName);
		 */
		
		//System.out.println(braid.getWidth()/7f);
		
		//regions[3] = new TextureRegion(braid,0.5f,0.2f,1f,1f);
		

		// _renderSystem.AddToRenderSystem((SpriteComponent)
		// _entity.GetComponent(SpriteComponent.class));

		// _serviceLocator.AddComponentToSystem(_entity.GetComponent(SpriteComponent.class),
		// RenderSystem.SystemName);
		// System.out.println("Size: "+ _renderSystem.ActiveComponents());
		
		regions = new TextureRegion[24];
		 texture = _serviceLocator.AssetManager.BraidSpriteSheet;
		 //texture = _serviceLocator.AssetManager.DummyTexture;
		
		 float xL = 1f/6f;
		 float yL = 1f/4f;
		 

		//regions[0] = new TextureRegion(texture, 0,0 ,xL,yL);      // #3
        //regions[1] = new TextureRegion(texture, xL, 0, xL*2, yL);    // #4
        //regions[2] = new TextureRegion(texture, xL*2, 0, xL*3, yL);    // #4
        //regions[3] = new TextureRegion(texture, 0,yL, xL, yL*2);    // #4
        //regions[2] = new TextureRegion(texture, 0, 63, 64, 64);     // #5
       // regions[3] = new TextureRegion(texture, 0.5f, 0.5f, 1f, 1f);    // #6
        
        int index = 0;
        for (int x = 0; x < 6; x++)
        {
        	for (int y = 0; y < 4; y++)
        	{
        		regions[index] = new TextureRegion(texture, xL*x, yL*y, xL*(x+1), yL*(y+1));    // #4
        		
        		index++;
        	}
        }
        
        TextureRegion[] r = SpriteSheetAnimatorComponent.CreateSpriteSheet(texture, 27, 7, 4);
        
//        _entity.AddComponent(new SpriteComponent(regions[5]), RenderSystem.SystemName);
        _entity.AddComponent(new SpriteSheetAnimatorComponent(r, 0.032f), RenderSystem.SystemName);

	}
	private TextureRegion[]     regions ;
	Texture texture;
	
	int hej = 0;
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
		
		//ServiceLocator.AssetManager.SpriteBatch.begin();
		//ServiceLocator.AssetManager.SpriteBatch.draw(texture, 0, 0, 64, 64);              // #7
	       
/*if (hej < 24)
{
	ServiceLocator.AssetManager.SpriteBatch.draw(regions[hej], 100f,100f);
	hej++;
	if (hej > 23)
		hej = 0;
	

}*/

		/*int index = 0;
		for (int x = 0; x < 12; x++)
		{
			for (int y = 0; y < 2; y++)
			{
				//System.out.println(index);
				if (index >= regions.length)
				{
					//System.out.println("bigger");
					continue;
				}
				
				ServiceLocator.AssetManager.SpriteBatch.draw(regions[index], 100f,100f);
				index++;
			}
		}*/
	        //ServiceLocator.AssetManager.SpriteBatch.end();
		
		
		

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
		// System.out.println("update");

		//_entity.GetTransform().Translate(new Vector2(1, 0));

		if (Gdx.input.isKeyJustPressed(Keys.P))
			// _entity.GetTransform().SetActive(!_entity.GetTransform().IsActive());
			_entity.SetActive(!_entity.GetActive());

	}
}
