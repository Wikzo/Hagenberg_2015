package net.gustavdahl.echelonengine.scenes;

import com.badlogic.gdx.Game;
import net.gustavdahl.echelonengine.menus.CircleMenuList;
import net.gustavdahl.echelonengine.menus.SplashScreen;
import net.gustavdahl.echelonengine.systems.MyAssetManager;

public class MyGame extends Game
{

	// virtual resolution
	public static final float V_WIDTH = 1366;
	public static final float V_HEIGHT = 768;

	public MyAssetManager MyAssetManager;

	@Override
	public void create()
	{
		MyAssetManager = new MyAssetManager();
		MyAssetManager.InitializeCommonAssets();
		MyAssetManager.InitializeMenuAssets();
		MyAssetManager.InitializeDebugAssets();

		//setScreen(new CircleMenuList(this));
		setScreen(new SplashScreen(this));
		
	}

	@Override
	public void dispose()
	{
		if (MyAssetManager != null)
		{
			MyAssetManager.DisposeAllAssets();
		}
	}

	public void render()
	{
		super.render(); // important!
	}

}