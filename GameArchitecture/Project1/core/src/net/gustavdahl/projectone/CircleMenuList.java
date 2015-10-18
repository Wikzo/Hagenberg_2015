package net.gustavdahl.projectone;

import java.sql.Time;
import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CircleMenuList implements Screen
{

	private Project1 project1;
	private OrthographicCamera cam;
	private ArrayList<Label> labels = new ArrayList<Label>();
	private Stage stage;

	public CircleMenuList(Project1 project)
	{
		// TODO Auto-generated constructor stub
		this.project1 = project;
		cam = new OrthographicCamera();
		stage = new Stage();

	}

	// TODO: transitions states (for changing bg color)
	void DebugStuff()
	{

	}

	@Override
	public void show()
	{

		// label style
		LabelStyle labelStyle = new Label.LabelStyle(Assets.ArialFont, Color.WHITE);
		// Label label1 = new Label("1", labelStyle);

		// create labels
		for (int i = 0; i < 12; i++)
		{
			Label l = new Label("Menu " + Integer.toString(i), labelStyle);
			l.setColor(Color.BLACK);
			l.setPosition(l.getWidth() * i + 50, 0, Align.left);

			labels.add(l);

			stage.addActor(l);
		}

		// make circle
		float multiplier = 400;
		float angle = 0f;
		float x = 0f;
		float y = 0f;
		for (int i = 0; i < labels.size(); i++)
		{

			angle += (360f / labels.size());
			x = (float) Math.sin(Math.toRadians(angle)) * multiplier;
			y = (float) Math.cos(Math.toRadians(angle)) * multiplier;

			labels.get(i).setPosition(x, y);

			// System.out.println(x + ", " + y);

		}

		// labels.get(0).setPosition(200f, 300f);

		// System.out.println(labels.get(0).getX() + ", " +
		// labels.get(0).getY());

		labels.get(highlightIndex).setColor(Color.WHITE);

		stage.setViewport(new FitViewport(project1.V_WIDTH, project1.V_HEIGHT));

	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.1f, 1f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		// TODO: continuous pressing down
		if (Gdx.input.isKeyJustPressed(Keys.LEFT))
			MenuMove(-1);
		else if (Gdx.input.isKeyJustPressed(Keys.RIGHT))
			MenuMove(1);

		if (Gdx.input.isKeyJustPressed(Keys.ENTER))
		{
			project1.setScreen(new MenuScreen(project1, "Menu text hejsa"));
		}

	}

	int highlightIndex = 0;

	void MenuMove(int direction)
	{
		// System.out.println("moving " + direction);

		// white = selected; black = not selected

		switch (direction)
		{
		case 1: // clockwise
			highlightIndex++;

			if (highlightIndex > labels.size() - 2) // last menu is just dummy
				highlightIndex = 0;

			labels.get(highlightIndex).setColor(Color.WHITE);

			if (highlightIndex != 0)
				labels.get(highlightIndex - 1).setColor(Color.BLACK);
			else
				labels.get(labels.size() - 2).setColor(Color.BLACK);

			break;

		case -1: // counter-clockwise
			highlightIndex--;

			if (highlightIndex < 0)
				highlightIndex = labels.size() - 2;

			labels.get(highlightIndex).setColor(Color.WHITE);

			if (highlightIndex != labels.size() - 2)
				labels.get(highlightIndex + 1).setColor(Color.BLACK);
			else
				labels.get(0).setColor(Color.BLACK);
			break;
		}

		// System.out.println(highlightIndex);
	}

	@Override
	public void resize(int width, int height)
	{

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		float aspect = w / h;

		// https://github.com/libgdx/libgdx/wiki/Viewports

		cam.setToOrtho(false, project1.V_WIDTH, project1.V_HEIGHT / aspect);
		cam.position.set(0, 0, 0);
		cam.update();

		/*
		 * float ar = (float) Gdx.graphics.getWidth() /
		 * Gdx.graphics.getHeight(); cam.setToOrtho(false, project1.V_WIDTH,
		 * project1.V_WIDTH / ar); cam.position.set(0, 0, 0); cam.update();
		 */

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
	}

}
