package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.visual.IRenderable;

public class RenderSystem extends BaseSystem
{

	public static final String SystemName = RenderSystem.class.getSimpleName();

	private List<IRenderable> _renderList;
	protected SpriteBatch _spriteBatch;

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
		if (!_isActive)
			return;

		if (_renderList.size() < -1)
		{
			System.out.println("ERROR - no render components in RenderSystem!");
			return;
		}

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
			throw new RuntimeException("ERROR - " + c.Name() + " doesn't implement IRenderable interface!");
		//throw new GdxRuntimeException("ERROR - " + c.Name() + " doesn't implement IRenderable interface!");

		return succesfullyAdded;

	}

}
