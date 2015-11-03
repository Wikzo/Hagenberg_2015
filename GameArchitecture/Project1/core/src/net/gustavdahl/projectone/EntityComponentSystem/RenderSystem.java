package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderSystem implements ISystem
{

	private ArrayList<SpriteComponent> _componentList;
	private SpriteBatch _spriteBatch;
	
	public boolean IsActive = true;

	public RenderSystem(SpriteBatch spriteBatch)
	{
		// TODO Auto-generated constructor stub
		_spriteBatch = spriteBatch;

		_componentList = new ArrayList<SpriteComponent>();
	}

	@Override
	public void Initialize()
	{
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (_componentList.get(i).HasBeenInitialized())
				continue;

			_componentList.get(i).Initialize();
		}
	}

	@Override
	public void Start()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void Update()
	{
		// TODO Auto-generated method stub
		Render();

	}

	public void Render()
	{
		if (!IsActive)
			return;
		
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (!_componentList.get(i).IsActive())
				continue;

			_spriteBatch.begin();
			_componentList.get(i).Render();
			_spriteBatch.end();
		}
	}

	public void AddToRenderSystem(SpriteComponent c)
	{
		_componentList.add(c);
	}

	public int ActiveComponents()
	{
		return _componentList.size();
	}

	@Override
	public void Destroy()
	{
		System.out.println("[Destroying " + this.getClass().getSimpleName() + "]");

		for (int i = 0; i < _componentList.size(); i++)
		{
			// _componentList.get(i).Owner.RemoveComponentOfType(_componentList.get(i).getClass());
			// _componentList.get(i).Owner.RemoveAllComponents();
			// TODO: also destroy the entity itself! (maybe?)
		}

		// _componentList.clear();
		// _componentList = null;

	}

}
