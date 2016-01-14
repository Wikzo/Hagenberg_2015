package net.gustavdahl.echelonengine.main;

import com.badlogic.gdx.Game;
import net.gustavdahl.echelonengine.menus.CircleMenuList;
import net.gustavdahl.echelonengine.menus.SplashScreen;
import net.gustavdahl.echelonengine.systems.MyAssetManager;

// Created by Gustav Dahl, 2016 - Hagenberg

public class MainMyGame extends Game
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