package net.gustavdahl.echelonengine.menus;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.gustavdahl.echelonengine.enums.MenuItemType;
import net.gustavdahl.echelonengine.scenes.*;
import net.gustavdahl.echelonengine.systems.MyAssetManager;

public class CircleMenuList implements Screen
{

	public final MyGame game;
	private OrthographicCamera camera;
	private Viewport viewport;

	private ArrayList<Label> labels = new ArrayList<Label>();
	private Stage stage;

	private int highlightIndex = 0;

	java.util.List<MenuItem> MenuItems;

	public CircleMenuList(MyGame project)
	{
		this.game = project;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.V_WIDTH, game.V_HEIGHT);
		viewport = new FitViewport(game.V_WIDTH, game.V_HEIGHT, camera);

		stage = new Stage();
		
		Image img = new Image(game.MyAssetManager.Mountain);
		img.setPosition(0, 0, Align.center);
		img.getColor().a = 1f;
		img.setName("Splash");
		stage.addActor(img);

		MenuItems = new ArrayList<MenuItem>();
		MenuItems.add(new MenuItem(MenuItemType.Selection, game, stage, this));
		MenuItems.add(new MenuItem(MenuItemType.CollisionBruteForce, game, stage, this));
		MenuItems.add(new MenuItem(MenuItemType.CollisionSortAndPrune, game, stage, this));
		MenuItems.add(new MenuItem(MenuItemType.SpringsAndForces, game, stage, this));
		MenuItems.add(new MenuItem(MenuItemType.Persistence, game, stage, this));

		CreateMenuItems();

	}

	void CreateMenuItems()
	{
		
		
		
		
		// label style
		LabelStyle labelStyle = new Label.LabelStyle(game.MyAssetManager.ArialFont, Color.WHITE);
		// Label label1 = new Label("1", labelStyle);

		// create labels
		for (int i = 0; i < MenuItems.size(); i++)
		{
			String menuName = MenuItems.get(i).MenuName;

			Label l = new Label(menuName, labelStyle);
			l.setColor(Color.BLACK);
			l.setPosition(l.getWidth() * i + 50, 0, Align.left);

			labels.add(l);

			stage.addActor(l);
		}

		// make circle
		float angle = 0f;
		float x = 0f;
		float y = 0f;
		for (int i = 0; i < labels.size(); i++)
		{
			x = (float) Math.sin(Math.toRadians(angle)) * 400;
			y = (float) Math.cos(Math.toRadians(angle)) * 300;

			labels.get(i).setPosition(x, y);
			angle += (360f / labels.size());
		}


		labels.get(highlightIndex).setColor(Color.WHITE);

		stage.setViewport(new FitViewport(game.V_WIDTH, game.V_HEIGHT));

		MenuItems.get(highlightIndex).Group.setVisible(true);
		
		
	}

	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.76f, 0.68f, 0.417f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.MyAssetManager.SpriteBatch.setProjectionMatrix(camera.combined);

		stage.act(delta);
		stage.draw();

		// TODO: continuous pressing down
		if (Gdx.input.isKeyJustPressed(Keys.LEFT))
			MenuMove(-1);
		else if (Gdx.input.isKeyJustPressed(Keys.RIGHT))
			MenuMove(1);

		if (Gdx.input.isKeyJustPressed(Keys.ENTER))
			MenuItems.get(highlightIndex).LoadScene();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Keys.F2))
			Gdx.app.exit();

	}

	void MenuMove(int direction)
	{
		switch (direction)
		{
		case 1: // clockwise
			highlightIndex++;

			if (highlightIndex > labels.size() - 1)
				highlightIndex = 0;

			labels.get(highlightIndex).setColor(Color.WHITE);
			MenuItems.get(highlightIndex).Group.setVisible(true);

			if (highlightIndex != 0)
			{
				labels.get(highlightIndex - 1).setColor(Color.BLACK);
				MenuItems.get(highlightIndex - 1).Group.setVisible(false);
			} else
			{
				labels.get(labels.size() - 1).setColor(Color.BLACK);
				MenuItems.get(labels.size() - 1).Group.setVisible(false);
			}

			break;

		case -1: // counter-clockwise
			highlightIndex--;

			if (highlightIndex < 0)
				highlightIndex = labels.size() - 1;

			labels.get(highlightIndex).setColor(Color.WHITE);
			MenuItems.get(highlightIndex).Group.setVisible(true);

			if (highlightIndex != labels.size() - 1)
			{
				labels.get(highlightIndex + 1).setColor(Color.BLACK);
				MenuItems.get(highlightIndex + 1).Group.setVisible(false);
			} else
			{
				labels.get(0).setColor(Color.BLACK);
				MenuItems.get(0).Group.setVisible(false);
			}
			break;
		}
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
		// dispose();

	}

	@Override
	public void dispose()
	{
	}

}
