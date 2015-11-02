package net.gustavdahl.projectone;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import net.gustavdahl.projectone.EntityComponentSystem.Component;
import net.gustavdahl.projectone.EntityComponentSystem.Entity;
import net.gustavdahl.projectone.EntityComponentSystem.RenderSystem;
import net.gustavdahl.projectone.EntityComponentSystem.ServiceLocator;
import net.gustavdahl.projectone.EntityComponentSystem.SpriteComponent;
import net.gustavdahl.projectone.EntityComponentSystem.TextComponent;
import net.gustavdahl.projectone.EntityComponentSystem.TransFormComponent;


public class ComponentTester implements Screen
{

	public ComponentTester()
	{
		SpriteBatch sb = new SpriteBatch();
		ServiceLocator service = new ServiceLocator(null, null);
		RenderSystem renderSystem = new RenderSystem(sb);
		ServiceLocator.RegisterSystem(renderSystem);

		Entity e = new Entity();

		TextComponent t1 = new TextComponent(sb);
		TextComponent t2 = new TextComponent(sb);
		
		e.AddComponent(t1);
		e.AddComponent(t2);
		
		//renderSystem.AddToRenderSystem(t);
		

		
		//System.out.println(TextComponent.class.getSimpleName());
		
		Component c_temp = e.GetComponent(TextComponent.class);
		Component c_temp2 = e.GetComponent(SpriteComponent.class);
		
		List<Component> tempList = e.GetAllComponentsOfType(TextComponent.class);
		
		System.out.println(c_temp);
		System.out.println(c_temp2);
		System.out.println(tempList);
		

		
		//System.out.println(c_temp2.Name());
		
		
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
			i +=  0.1f;
			ServiceLocator.UpdateSystems();
			
			//if (Gdx.input.isKeyJustPressed(Keys.SPACE))
				
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
