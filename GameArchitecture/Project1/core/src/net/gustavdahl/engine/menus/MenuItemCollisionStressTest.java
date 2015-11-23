package net.gustavdahl.engine.menus;

import com.badlogic.gdx.scenes.scene2d.Stage;

import Scenes.CollisionStressTest;
import Scenes.MyGame;
import net.gustavdahl.engine.systems.ServiceLocator;

public class MenuItemCollisionStressTest extends MenuItem
{

	public MenuItemCollisionStressTest(Stage stage, MyGame project1, CircleMenuList circleMenu, MenuItemType type, ServiceLocator serviceLocator)
	{
		super(stage, project1, circleMenu, type, serviceLocator);
		
		project1.setScreen(new CollisionStressTest(project1, serviceLocator));
		
	}

	@Override
	public void show()
	{

		
	}

	@Override
	public void render(float delta)
	{
		// TODO Auto-generated method stub
		
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
