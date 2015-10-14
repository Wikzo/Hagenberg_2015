package net.gustavdahl.projectone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Project1 extends Game
{

	// virtual resoltuion
	public static final float V_WIDTH = 1366;
	public static final float V_HEIGHT = 768;

	@Override
	public void create()
	{
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose()
	{
		Assets.DisposeAllAssets(this.toString());
	}

}