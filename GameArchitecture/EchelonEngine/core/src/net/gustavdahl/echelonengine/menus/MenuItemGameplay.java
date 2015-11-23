package net.gustavdahl.echelonengine.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.gustavdahl.echelonengine.scenes.*;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class MenuItemGameplay extends MenuItem
{

	public MenuItemGameplay(Stage stage, MyGame project1, CircleMenuList circleMenu, MenuItemType type, ServiceLocator serviceLocator)
	{
		super(stage, project1, circleMenu, type, serviceLocator);
	}


	@Override
	protected void CreateMenus()
	{

		// System.exit(-1);//.println("show");
		
		// label style
		LabelStyle labelStyle = new Label.LabelStyle(ServiceLocator.AssetManager.ArialFont, Color.WHITE);
		Label label1 = new Label("In-game hints: ENABLED", labelStyle);
		Label label2 = new Label("Difficulty: MEDIUM", labelStyle);
		Label label3 = new Label("Share data online: DISABLED", labelStyle);
		Label label4 = new Label("Automatic saving: ENABLED", labelStyle);

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

}
