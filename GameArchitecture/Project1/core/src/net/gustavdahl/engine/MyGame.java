package net.gustavdahl.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.gustavdahl.engine.components.Text;
import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.MyAssetManager;
import net.gustavdahl.engine.systems.EntityManager;
import net.gustavdahl.engine.systems.RenderSystem;
import net.gustavdahl.engine.systems.ServiceLocator;

public class MyGame extends Game
{

	// virtual resolution
	public static final float V_WIDTH = 1366;
	public static final float V_HEIGHT = 768;
	
	private static ServiceLocator _serviceLocator;
	private MyAssetManager _assetManager;
	//private static EntityManager _entityManager;
	

	@Override
	public void create()
	{
		_assetManager = new MyAssetManager();
		_assetManager.InitializeCommonAssets();
		_assetManager.InitializeMenuAssets();
		_assetManager.InitializeDebugAssets();
		
		_serviceLocator = new ServiceLocator(_assetManager, null);
		setScreen(new GameTest(this, _serviceLocator));
		
		
		
		//setScreen(new ComponentTester()); old test
		
		//setScreen(new SplashScreen(this)); old menu stuff
	}

	@Override
	public void dispose()
	{
		_serviceLocator.DestroyAllSystems();
		ServiceLocator.AssetManager.DisposeAllAssets();
	}
	
	public void render()
	{
		super.render(); // important!
	}

}