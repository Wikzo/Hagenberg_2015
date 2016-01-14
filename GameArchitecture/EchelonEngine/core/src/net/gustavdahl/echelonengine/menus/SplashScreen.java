package net.gustavdahl.echelonengine.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.gustavdahl.echelonengine.main.MainMyGame;
import net.gustavdahl.echelonengine.scenes.*;
import net.gustavdahl.echelonengine.systems.MyAssetManager;

public class SplashScreen implements Screen
{

	final MainMyGame game;
	private OrthographicCamera camera;
	private Viewport viewport;

	private float displayTime;
	private float fadeInTime = 1.2f;
	private float fadeOutTime = 1f;
	private float DisplayTime = 3f;
	private Stage stage;
	private MyAssetManager _assetManager;

	public SplashScreen(MainMyGame project)
	{
		this.game = project;
		this._assetManager = project.MyAssetManager;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.V_WIDTH, game.V_HEIGHT);
		viewport = new FitViewport(game.V_WIDTH, game.V_HEIGHT, camera);
		
		displayTime = 2f;
		stage = new Stage();
	}

	@Override
	public void show()
	{
		Image img = new Image(_assetManager.SplashTexture);
		img.setPosition(0, 0, Align.center);
		img.getColor().a = 0f;
		img.setName("Splash");

		img.addAction(Actions.sequence(
				// Actions.show(),
				Actions.delay(0.2f), Actions.fadeIn(fadeInTime), Actions.delay(DisplayTime), Actions.run(new Runnable()
				{
					@Override
					public void run()
					{
						fadeOut(fadeOutTime);
					}
				})));

		stage.addActor(img);

		// Gdx.input

		// configure viewport
		stage.setViewport(new FitViewport(game.V_WIDTH, game.V_HEIGHT));

	}

	private void fadeOut(float fadeTime)
	{
		Actor img = stage.getRoot().findActor("Splash");
		assert(img != null);
		//img.clearActions();
		img.addAction(Actions.sequence(Actions.fadeOut(fadeTime), Actions.run(new Runnable()
		{
			@Override
			public void run()
			{
				EndOfState();
			}
		})));
	}

	void EndOfState()
	{

		game.setScreen(new CircleMenuList(game));
		//dispose();
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		_assetManager.SpriteBatch.setProjectionMatrix(camera.combined);

		stage.act(delta);
		stage.draw();

		if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.isTouched())
			fadeOut(0.1f);

	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height);

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
		// Assets.DisposeAllAssets(this.toString());
	}

	@Override
	public void dispose()
	{
		

	}

}
