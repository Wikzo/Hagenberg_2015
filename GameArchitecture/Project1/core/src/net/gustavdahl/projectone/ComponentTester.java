package net.gustavdahl.projectone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.gustavdahl.projectone.EntityComponentSystem.Entity;
import net.gustavdahl.projectone.EntityComponentSystem.RenderSystem;
import net.gustavdahl.projectone.EntityComponentSystem.ServiceLocator;
import net.gustavdahl.projectone.EntityComponentSystem.TextComponent;


public class ComponentTester implements Screen
{

	public ComponentTester()
	{
		SpriteBatch sb = new SpriteBatch();
		ServiceLocator service = new ServiceLocator(null, null);
		RenderSystem renderSystem = new RenderSystem(sb);
		ServiceLocator.RegisterSystem(renderSystem);

		Entity e = new Entity();

		TextComponent t = new TextComponent(sb);
		e.AddComponent(t);
		
		renderSystem.AddToRenderSystem(t);
		
		
	}
	
	float i = 0f;

	@Override
	public void show()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta)
	{
		while (i < 500)
		{
			i +=  0.5f;
			ServiceLocator.UpdateSystems();
		}
		
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
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

	

}
