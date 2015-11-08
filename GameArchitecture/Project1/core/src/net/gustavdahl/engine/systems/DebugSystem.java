package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.DebugComponent;
import net.gustavdahl.engine.components.IDebugRenderable;
import net.gustavdahl.engine.components.IRenderable;
import net.gustavdahl.engine.components.PhysicsComponent;

public class DebugSystem extends RenderSystem
{
	public static final String SystemName = DebugSystem.class.getSimpleName();
	
	protected List<IDebugRenderable> _debugRenderList;
	
	public DebugSystem(SpriteBatch spriteBatch)
	{
		super(spriteBatch);
		_debugRenderList = new ArrayList<IDebugRenderable>();
	}

	@Override
	public void Update(float deltaTime)
	{		
		if (!_isActive)
			return;
		
		if (_debugRenderList.size() < 1)
		{
			System.out.println("ERROR - no render components in DebugSystem!");
			return;
		}

		for (int i = 0; i < _debugRenderList.size(); i++)
		{
			if (!((Component) _debugRenderList.get(i)).IsActive())
				continue;

			//_spriteBatch.enableBlending();
			//_spriteBatch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			_spriteBatch.begin();
			_debugRenderList.get(i).DebugRender(_spriteBatch, deltaTime);
			_spriteBatch.end();
		}
		
	}
	
	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof DebugComponent)
		{
			succesfullyAdded = true;
			_debugRenderList.add((DebugComponent) c);

		} else
		{
			try
			{
				throw new Exception(
						"ERROR - component " + c.getClass().getSimpleName() + " doesn't isn't a DebugComponent!");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return succesfullyAdded;
	}

}
