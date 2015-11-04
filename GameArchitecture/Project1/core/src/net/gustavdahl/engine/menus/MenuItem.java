package net.gustavdahl.engine.menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.gustavdahl.engine.MyGame;

public abstract class MenuItem implements Screen
{

	final MyGame game;

	CircleMenuList circleMenu;

	MenuItemType MyType;

	String MenuText = "";

	ArrayList<Label> labels = new ArrayList<Label>();
	
	protected Stage Stage;
	protected Group Group;

	public MenuItem(Stage stage, MyGame project1, CircleMenuList circleMenu, MenuItemType type)
	{
		this.game = project1;
		this.circleMenu = circleMenu;
		this.MyType = type;
		this.Stage = stage;
				
		Group = new Group();

		//System.out.println("New menu screen with type: " + MyType);
	}


	
	public void InitializeSubMenu()
	{
		CreateMenus();
		
		Group.setVisible(true);
		Stage.addActor(Group);
		
	}
	
	protected abstract void CreateMenus();
	
	public void RemoveSubMenu()
	{
		Group.setVisible(false);
		
		// better to remove the actor LATER:
		// https://stackoverflow.com/questions/22121467/remove-actors-from-stage
		Group.addAction(Actions.removeActor());
		//Group.remove();
		
		dispose();
	}

	@Override
	public void dispose()
	{	

	}

}
