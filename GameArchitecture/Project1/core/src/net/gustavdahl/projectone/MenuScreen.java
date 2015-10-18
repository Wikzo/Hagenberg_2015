package net.gustavdahl.projectone;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuScreen implements Screen
{

	Project1 project1;

	String MenuText = "";

	ArrayList<Label> labels = new ArrayList<Label>();

	private Stage stage;

	public MenuScreen(Project1 project1, String menuText)
	{
		this.project1 = project1;
		this.MenuText = menuText;

		stage = new Stage();
	}

	@Override
	public void show()
	{
		// label style
		LabelStyle labelStyle = new Label.LabelStyle(Assets.ArialFont, Color.WHITE);
		Label label1 = new Label(this.MenuText, labelStyle);

		stage.addActor(label1);

		stage.setViewport(new FitViewport(project1.V_WIDTH, project1.V_HEIGHT));

	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1.0f, 0f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int width, int height)
	{
		/*
		 * float w = Gdx.graphics.getWidth(); float h =
		 * Gdx.graphics.getHeight();
		 * 
		 * float aspect = w / h;
		 * 
		 * // https://github.com/libgdx/libgdx/wiki/Viewports
		 * 
		 * cam.setToOrtho(false, project1.V_WIDTH, project1.V_HEIGHT / aspect);
		 * cam.position.set(0, 0, 0); cam.update();
		 * 
		 * stage.getViewport().update(width, height);
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

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

}
