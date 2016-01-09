package net.gustavdahl.echelonengine.scenes;

import com.badlogic.gdx.Game;
import net.gustavdahl.echelonengine.menus.CircleMenuList;
import net.gustavdahl.echelonengine.systems.MyAssetManager;

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