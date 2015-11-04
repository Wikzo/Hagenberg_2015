package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.IRenderable;

public class RenderSystem implements ISystem
{

	public static final String SystemName = RenderSystem.class.getSimpleName();
	
	private List<IRenderable> _componentList;
	private SpriteBatch _spriteBatch;
	
	public boolean IsActive = true;

	public RenderSystem(SpriteBatch spriteBatch)
	{
		_spriteBatch = spriteBatch;

		_componentList = new ArrayList<IRenderable>();
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
			if (!((Component) _componentList.get(i)).IsActive())
				continue;

			_spriteBatch.begin();
			_componentList.get(i).Render();
			_spriteBatch.end();
		}
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

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;
		
		if (c instanceof IRenderable)
		{
			succesfullyAdded = true;
			_componentList.add((IRenderable) c);
			
		}
		else
		{
            try
			{
				throw new Exception("ERROR - component doesn't implement IRenderable interface!");
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		//System.out.println(added);
		
		return succesfullyAdded;
	}

}
