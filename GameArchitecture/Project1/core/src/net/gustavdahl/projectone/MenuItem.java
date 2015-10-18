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
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class MenuItem implements Screen
{

	final MyGame game;

	CircleMenuList circleMenu;

	MenuItemType MyType;

	private OrthographicCamera camera;
	private Viewport viewport;

	String MenuText = "";

	ArrayList<Label> labels = new ArrayList<Label>();

	protected Stage stage;

	public MenuItem(MyGame project1, CircleMenuList circleMenu, MenuItemType type)
	{
		this.game = project1;
		this.circleMenu = circleMenu;
		this.MyType = type;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.V_WIDTH, game.V_HEIGHT);
		viewport = new FitViewport(game.V_WIDTH, game.V_HEIGHT, camera);

		stage = new Stage();

		//System.out.println("New menu screen with type: " + MyType);
	}

	@Override
	public void show()
	{
		// label style
		LabelStyle labelStyle = new Label.LabelStyle(Assets.ArialFont, Color.WHITE);
		Label label1 = new Label("Menu item 1", labelStyle);

		stage.addActor(label1);

		stage.setViewport(new FitViewport(game.V_WIDTH, game.V_HEIGHT));

	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1.0f, 0f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

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

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

}
