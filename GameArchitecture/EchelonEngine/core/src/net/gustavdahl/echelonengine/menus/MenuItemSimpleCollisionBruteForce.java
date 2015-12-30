package net.gustavdahl.echelonengine.menus;

import com.badlogic.gdx.scenes.scene2d.Stage;

import net.gustavdahl.echelonengine.scenes.*;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class MenuItemSimpleCollisionBruteForce extends MenuItem
{

	public MenuItemSimpleCollisionBruteForce(Stage stage, MyGame project1, CircleMenuList circleMenu, MenuItemType type, ServiceLocator serviceLocator)
	{
		super(stage, project1, circleMenu, type, serviceLocator);
		
		project1.setScreen(new SimpleCollisionBruteForce(project1, circleMenu, serviceLocator));
		
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
		// TODO Auto-generated method stub
		
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
	protected void CreateMenus()
	{
		// TODO Auto-generated method stub
		
	}

}
