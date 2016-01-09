package net.gustavdahl.echelonengine.scenes;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.gustavdahl.echelonengine.components.visual.Text;
import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.entities.EntityFactory;
import net.gustavdahl.echelonengine.menus.CircleMenuList;
import net.gustavdahl.echelonengine.menus.SplashScreen;
import net.gustavdahl.echelonengine.systems.EntityManager;
import net.gustavdahl.echelonengine.systems.MyAssetManager;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class MyGame extends Game
{

	// virtual resolution
	public static final float V_WIDTH = 1366;
	public static final float V_HEIGHT = 768;

	public MyAssetManager _assetManager;

	@Override
	public void create()
	{
		_assetManager = new MyAssetManager();
		_assetManager.InitializeCommonAssets();
		_assetManager.InitializeMenuAssets();
		_assetManager.InitializeDebugAssets();

		/*
		 * _entityManager = new EntityManager(); _entityFactory = new
		 * EntityFactory();
		 * 
		 * _serviceLocator = new ServiceLocator(_assetManager, _entityManager,
		 * _entityFactory);
		 */

		setScreen(new CircleMenuList(this));
	}

	@Override
	public void dispose()
	{
		if (_assetManager != null)
		{
			_assetManager.DisposeAllAssets();
		}
	}

	public void render()
	{
		super.render(); // important!
	}

}