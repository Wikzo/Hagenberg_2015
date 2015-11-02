package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity
{

	private String _ID;
	private TransFormComponent _transform;
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

		for (int i = _components.size(); i > 0; i--)
		{
			if (_components.get(i).getClass() == componentClass)
			{
				_components.get(i).Destroy();
				removedComponent = true;
			}
		}

		return removedComponent;
	}

	public boolean RemoveAllComponents()
	{
		if (_components.size() <= 0)
			return false;

		for (int i = _components.size(); i > 0; i--)
			_components.get(i).Destroy();

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
		_components.add(c);
		c.Enable();
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

	public TransFormComponent GetTransform()
	{
		return _transform;
	}

	public void SetTransform(TransFormComponent transform)
	{
		_transform = transform;
	}

}
