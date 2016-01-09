package net.gustavdahl.echelonengine.menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import net.gustavdahl.echelonengine.entities.EntityFactory;
import net.gustavdahl.echelonengine.scenes.*;
import net.gustavdahl.echelonengine.systems.EntityManager;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class MenuItem
{
	public String MenuName = "";
	private String InnerMenuName = "";

	final MyGame game;
	CircleMenuList circleMenu;
	MenuItemType MenuType;
	
	ServiceLocator _serviceLocator;

	ArrayList<Label> labels = new ArrayList<Label>();

	protected Stage Stage;
	public Group Group;

	public MenuItem(MenuItemType type, MyGame game, Stage stage, CircleMenuList circleMenu)
	{
		this.game = game;
		this.circleMenu = circleMenu;
		this.MenuType = type;

		this.Stage = stage;
		Group = new Group();
		
		SetMenuNames();
		
		InitializeSubMenu();
	}

	public void SetMenuNames()
	{
		switch (MenuType)
		{
		case CollisionBruteForce:
			MenuName = "Collision:\nBrute Force";
			InnerMenuName = "***PAIR-WISE COLLISION***\nBrute-force checking every entitiy against each other.\nComplexity is O(n^2)";
			break;
			
		case CollisionSortAndPrune:
			MenuName=  "Collision:\nSort and Prune";
			InnerMenuName = "***SORT AND PRUNE COLLISION***\nBefore checking each pair, the entities are sorted by using their X position.\nEntities that have overlapping X positions are then checked.";
			break;
			
		case Persistence:
			MenuName= "Persistence";
			InnerMenuName = "***PERSISTENCE***\nPeppe";
			break;
			
		case Selection:
			MenuName= "Editor Selection";
			InnerMenuName = "***EDITOR SELECTION***\nUse the mouse to select entities.\nWhen an entity is selected, the following actions can be performed:\nMOVE (W); Rotate (E); Scale (R) and Multi-Select (CTRL).";
			break;
			
		case SpringsAndForces:
			InnerMenuName = "***FORCES AND SPRINGS***\nShowing gravity forces and connected springs using Euler integration.";
			MenuName= "Forces and Springs";
		}
	}

	Screen s;
	public void LoadScene()
	{
		System.gc();
		
		EntityManager _entityManager = new EntityManager();
		EntityFactory _entityFactory = new EntityFactory();
		
		_serviceLocator = new ServiceLocator(game._assetManager, _entityManager, _entityFactory);
		
		switch (MenuType)
		{
		case CollisionBruteForce:
			s = new BruteForceCollisionScene(game, circleMenu, _serviceLocator);
			game.setScreen(s);
			break;
			
		case CollisionSortAndPrune:
			s = new SortAndPruneCollisionScene(game, circleMenu, _serviceLocator);
			game.setScreen(s);
			break;
		case Persistence:
			break;
		case Selection:
			s = new SelectionScene(game, circleMenu, _serviceLocator);
			game.setScreen(s);
			break;
			
		case SpringsAndForces:
			s = new ForcesAndSpringsScene(game, circleMenu, _serviceLocator);
			game.setScreen(s);
			break;
		}
	}

	public void InitializeSubMenu()
	{
		CreateMenus();

		Group.setVisible(false);
		Stage.addActor(Group);

	}

	protected void CreateMenus()
	{
		// label style
		LabelStyle labelStyle = new Label.LabelStyle(game._assetManager.InnerMenuFont, Color.WHITE);
		Label label1 = new Label(InnerMenuName, labelStyle);
		float startPosX = Gdx.graphics.getWidth() / 2;
		label1.setWrap(true);
		label1.setWidth(500);
		label1.setPosition(-100, 0);

		Group.addActor(label1);

	}

}
