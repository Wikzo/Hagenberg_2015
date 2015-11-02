package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main_ComponentTester
{

	public static void main(String[] args)
	{
		SpriteBatch sb = new SpriteBatch();
		ServiceLocator service = new ServiceLocator(null, null);
		RenderSystem renderSystem = new RenderSystem(sb);
		ServiceLocator.RegisterSystem(renderSystem);

		Entity e = new Entity();

		TextComponent t = new TextComponent(sb);
		e.AddComponent(t);
		
		renderSystem.AddToRenderSystem(t);
		
		float i = 0f;
		
		while (i < 500)
		{
			i +=  0.5f;
			ServiceLocator.UpdateSystems();
		}

	}

}
