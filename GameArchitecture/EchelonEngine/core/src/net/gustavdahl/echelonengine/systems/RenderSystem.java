package net.gustavdahl.echelonengine.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.visual.IRenderable;

public class RenderSystem extends BaseSystem<IRenderable>
{

	public static final String SystemName = RenderSystem.class.getSimpleName();

	protected SpriteBatch _spriteBatch;
	protected MyAssetManager _assetManager;
	private boolean _drawBackground;

	public RenderSystem(SpriteBatch spriteBatch)
	{
		super();

		_spriteBatch = spriteBatch;
		_assetManager = ServiceLocator.AssetManager;
		_drawBackground = true;
	}

	@Override
	public void Start()
	{
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (((Component) _componentList.get(i)).HasBeenInitialized())
				continue;

			((Component) _componentList.get(i)).Initialize();
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

		if (_componentList.size() < -1)
		{
			System.out.println("ERROR - no render components in RenderSystem!");
			return;
		}

		_spriteBatch.begin();
		
		if (_drawBackground)
			_spriteBatch.draw(_assetManager.GameBackground, 0, 0);
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (!((Component) _componentList.get(i)).IsActive())
				continue;

			
			_componentList.get(i).Render(_spriteBatch, deltaTime);
			
		}
		_spriteBatch.end();
	}

	@Override
	public boolean ValidateIfComponentCanBeAddedToSystem(Component c)
	{
		if (c instanceof IRenderable)
			return true;
		else
			return false;

		
		/*
		boolean succesfullyAdded = false;

		if (c instanceof IRenderable)
		{
			succesfullyAdded = true;
			_componentList.add((IRenderable) c);

		} else
			throw new RuntimeException("ERROR - " + c.Name() + " doesn't implement IRenderable interface!");
		//throw new GdxRuntimeException("ERROR - " + c.Name() + " doesn't implement IRenderable interface!");

		return succesfullyAdded;*/

	}



}
