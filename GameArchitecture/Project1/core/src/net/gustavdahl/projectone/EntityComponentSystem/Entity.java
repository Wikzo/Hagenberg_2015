package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity
{

	private String _ID;
	public TransFormComponent _transform;
	private List<Component> _components;

	private boolean _isActive;
	private Entity _parent;

	public Entity()
	{
		_components = new ArrayList<Component>();
		_transform = new TransFormComponent();

		_components.add(_transform);
	}

	public void SetActive()
	{
	}

	// bool indicates whether the component was successfully retrieved
	public boolean RemoveComponentOfType(Class componentClass)
	{

		boolean removedComponent = false;

		for (int i = _components.size() - 1; i >= 0; i--)
		{
			
			//System.out.println(i);
			if (_components.get(i).getClass() == componentClass)
			{		
				_components.get(i).Destroy();	
				_components.remove(i);
				
				System.gc();
				
				removedComponent = true;
				
			}
		}

		return removedComponent;
	}
	
	public boolean RemoveComponentOfTypeUsingBase(Class baseClass)
	{

		// TODO: be able to retrieve all subclasses, e.g. by specifying "RenderComponent" and then get all sub-components
		return false;
	}

	public boolean RemoveAllComponents()
	{
		if (_components.size() <= 0)
			return false;

		for (int i = _components.size() - 1; i >= 0; i--)
		{
			_components.get(i).Destroy();
			_components.remove(i);
			System.gc();
		}

		return true;
	}

	// USING MAP
	// https://stackoverflow.com/questions/33485007/java-how-to-retrieve-a-specific-type-in-an-arraylist/33485349#33485349
	// problem: can NOT have duplicated keys!
	/*
	 * private Map<String, Component> _components = new HashMap<String,
	 * Component>(); public void AddComponent(Component c) {
	 * _components.put(c.Name(), c); c.Enable(); }
	 * 
	 * public Component GetComponent(Component c) { return
	 * _components.get(c.Name()); }
	 */

	public void AddComponent(Component c)
	{
		//System.out.println(GetComponent(c.getClass()));
		
		if (GetComponent(c.getClass()) != null && !c.CanHaveMultipleComponentsOfThisType)
		{
			System.err.println("ERROR: Cannot have more than one " + c.Name() + " on this entity!");
			c.Destroy();
			return;
		}
		_components.add(c);
		System.out.println("[" + c.Name() + " added to entity]");
		c.Enable(this);
	}

	public Component GetComponent(Class componentClass)
	{
		for (Component c : _components)
		{
			if (c.getClass() == componentClass)
				return c;
		}

		return null;
	}

	public List<Component> GetAllComponentsOfType(Class componentClass)
	{
		List<Component> components = new ArrayList<Component>();
		for (Component c : _components)
		{
			if (c.getClass() == componentClass)
				components.add(c);
		}

		return components;
	}
	
	public List<Component> GetAllComponents()
	{
		return _components;
	}

	public TransFormComponent GetTransform()
	{
		return _transform;
	}

	public void SetTransform(TransFormComponent transform)
	{
		_transform = transform;
	}
	
	public void DestroyEntity()
	{
		// TODO: destroy this (garbage collector?)
	}

}
