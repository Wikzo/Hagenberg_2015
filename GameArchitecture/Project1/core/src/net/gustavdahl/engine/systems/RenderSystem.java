package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.IRenderable;

public class RenderSystem extends BaseSystem
{

	public static final String SystemName = RenderSystem.class.getSimpleName();

	private List<IRenderable> _renderList;
	private SpriteBatch _spriteBatch;

	public boolean IsActive = true;

	public RenderSystem(SpriteBatch spriteBatch)
	{
		super();
		
		_spriteBatch = spriteBatch;
		_renderList = new ArrayList<IRenderable>();
	}

	@Override
	public void Start()
	{
		for (int i = 0; i < _renderList.size(); i++)
		{
			if (((Component) _renderList.get(i)).HasBeenInitialized())
				continue;

			((Component) _renderList.get(i)).Initialize();
		}
	}

	@Override
	public void Update(float deltaTime)
	{
		Render(deltaTime);
	}

	public void Render(float deltaTime)
	{
		if (!IsActive)
			return;

		for (int i = 0; i < _renderList.size(); i++)
		{
			if (!((Component) _renderList.get(i)).IsActive())
				continue;

			_spriteBatch.begin();
			_renderList.get(i).Render(_spriteBatch, deltaTime);
			_spriteBatch.end();
		}
	}



	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof IRenderable)
		{
			succesfullyAdded = true;
			_renderList.add((IRenderable) c);

		} else
		{
			try
			{
				throw new Exception("ERROR - component doesn't implement IRenderable interface!");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return succesfullyAdded;
		
	}


}
