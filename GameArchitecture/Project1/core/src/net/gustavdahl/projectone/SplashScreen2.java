package net.gustavdahl.projectone;

import java.sql.Time;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class SplashScreen2 implements Screen
{

	private Project1 project1;
	private Stage stage;

	private Texture texture;
	private SpriteBatch spriteBatch;
	private OrthographicCamera cam;

	private float displayTime;

	public SplashScreen2(Project1 project)
	{
		// TODO Auto-generated constructor stub
		this.project1 = project;
		this.stage = project1.getStage();

		this.spriteBatch = project.getSpriteBatch();
		this.cam = new OrthographicCamera();

		texture = new Texture("splash.png");

		this.displayTime = 2f;
	}

	float fadeTime = 1f;
	float DisplayTime = 1f;

	@Override
	public void show()
	{
		Image img = new Image(texture);
		img.setPosition(0, 0, Align.center);
		img.getColor().a = 0f;
		img.setName("Splash");

		//img.addAction(Actions.sequence(Actions.fadeOut(5f), Actions.hide()));

		img.addAction(Actions.sequence(
				//Actions.show(),
				Actions.delay(0.2f),
				Actions.fadeIn(1),
				Actions.delay(DisplayTime),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						fadeOut();
					}
				})
				));

		stage.addActor(img);

		// Gdx.input

		// configure viewport
		stage.setViewport(new FitViewport(project1.V_WIDTH, project1.V_HEIGHT));

	}

	private void fadeOut()
	{
		Actor img = stage.getRoot().findActor("Splash");
		assert(img != null);
		img.clearActions();
		img.addAction(Actions.sequence(Actions.fadeOut(2f), Actions.run(new Runnable()
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
		project1.setScreen(new Menu(project1));
		//System.out.println("New state");
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.1f, 1f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

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
		// TODO Auto-generated method stub
		dispose();

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		texture.dispose();

	}

}
