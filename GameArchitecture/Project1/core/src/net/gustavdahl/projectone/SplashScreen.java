package net.gustavdahl.projectone;

import java.sql.Time;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javafx.scene.shape.MoveTo;

public class SplashScreen implements Screen
{

	private Project1 project1;
	private OrthographicCamera cam;

	private float displayTime;
	private float fadeInTime = 1f;
	private float fadeOutTime = 1f;
	private float DisplayTime = 1f;
	private Stage stage;

	public SplashScreen(Project1 project)
	{
		// TODO Auto-generated constructor stub
		this.project1 = project;
		cam = new OrthographicCamera();
		displayTime = 2f;
		stage = new Stage();
	}

	@Override
	public void show()
	{
		Image img = new Image(Assets.SplashTexture);
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
		stage.setViewport(new FitViewport(project1.V_WIDTH, project1.V_HEIGHT));

	}

	private void fadeOut(float fadeTime)
	{
		Actor img = stage.getRoot().findActor("Splash");
		assert(img != null);
		img.clearActions();
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

		project1.setScreen(new CircleMenuList(project1));
		// System.out.println("New state");
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.1f, 1f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
		// Assets.DisposeAllAssets(this.toString());
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

}
