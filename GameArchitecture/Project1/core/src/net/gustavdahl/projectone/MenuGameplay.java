package net.gustavdahl.projectone;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuGameplay extends MenuItem
{

	public MenuGameplay(MyGame project1, CircleMenuList circleMenu, MenuItemType type)
	{
		super(project1, circleMenu, type);
	}
	
	@Override
	public void show()
	{
		// label style
		LabelStyle labelStyle = new Label.LabelStyle(Assets.ArialFont, Color.WHITE);
		Label label1 = new Label("Menu item 1", labelStyle);
		Label label2 = new Label("Menu item 2", labelStyle);
		Label label3 = new Label("Menu item 3", labelStyle);
		Label label4 = new Label("Menu item 4", labelStyle);

		float startPosX = -label1.getWidth();
		float offsetX = label1.getWidth() + 20f;
		float offsetY = label1.getHeight() + 20f;
		
		label1.setPosition(startPosX, 0);
		label2.setPosition(startPosX + offsetX , 0);
		label3.setPosition(startPosX, offsetY);
		label4.setPosition(startPosX + offsetX, offsetY);
		
		stage.addActor(label1);
		stage.addActor(label2);
		stage.addActor(label3);
		stage.addActor(label4);

		stage.setViewport(new FitViewport(game.V_WIDTH, game.V_HEIGHT));

	}

}
