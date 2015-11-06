package net.gustavdahl.engine.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import net.gustavdahl.engine.MyGame;
import net.gustavdahl.engine.systems.ServiceLocator;

public class MenuVideo extends MenuItem
{

	public MenuVideo(Stage stage, MyGame project1, CircleMenuList circleMenu, MenuItemType type)
	{
		super(stage, project1, circleMenu, type);
	}

	@Override
	public void show()
	{
		

	}

	@Override
	public void render(float delta)
	{
		

	}

	@Override
	public void resize(int width, int height)
	{
		

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
		

	}

	@Override
	protected void CreateMenus()
	{
		// label style
		LabelStyle labelStyle = new Label.LabelStyle(ServiceLocator.AssetManager.ArialFont, Color.WHITE);
		Label label1 = new Label("FOV: LARGE", labelStyle);
		Label label2 = new Label("Anti-alisasing: ENABLED", labelStyle);
		Label label3 = new Label("Subtitles: DISABLED", labelStyle);
		Label label4 = new Label("4K resolution: DISABLED", labelStyle);

		float startPosX = -label1.getWidth();
		float offsetX = label1.getWidth() + 20f;
		float offsetY = label1.getHeight() + 20f;

		label1.setPosition(startPosX, 0);
		label2.setPosition(startPosX + label3.getWidth() + 20, 0);
		label3.setPosition(startPosX, offsetY);
		label4.setPosition(startPosX + label3.getWidth() + 20, offsetY);

		// TODO: make a better "grid" for the menus

		Group.addActor(label1);
		Group.addActor(label2);
		Group.addActor(label3);
		Group.addActor(label4);

	}

}
