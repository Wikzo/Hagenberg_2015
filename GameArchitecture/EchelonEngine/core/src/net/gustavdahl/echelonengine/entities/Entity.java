package net.gustavdahl.echelonengine.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.TransFormComponent;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class Entity
{

	public String Name;
	public UUID ID;
	private TransFormComponent _transform;
	private List<Component> _components;

	private boolean _isActive;
	private Entity _parent;

	public boolean CurrentlySelectedByEditor; // TODO: move this to
												// EditorComponent

	public Entity(String name)
	{
		Name = name;

		_components = new ArrayList<Component>();
		_transform = new TransFormComponent();
		_transform.Enable(this, null); // QUESTION: does Transform need to be
										// added to a system?

		_components.add(_transform);

		CurrentlySelectedByEditor = false;

		_isActive = true;

		UUID id = UUID.randomUUID();
		this.ID = id;
		ServiceLocator.EntityManager.AddEntity(this);

		System.out.println("*** " + this.Name + " created! ***");
	}

	public void SetActive(boolean active)
	{
		_isActive = active;

		for (Component c : _components)
		{
			c.SetActive(active);
		}
	}

	public boolean GetActive()
	{
		return _isActive;
	}

	public boolean RemoveComponentOfType(Class componentClass)
	{
		boolean removedComponent = false;

		for (int i = _components.size() - 1; i >= 0; i--)
		{
			// System.out.println(i);
			if (_components.get(i).getClass() == componentClass)
			{
				_components.get(i).RemoveComponent();

				System.gc();

				removedComponent = true;

			}
		}

		return removedComponent;
	}

	public void RemoveComponentFromEntity(Component component)
	{
		if (_components.contains(component))
			_components.remove(component);
		
		component = null;
	}

	public boolean RemoveAllComponents()
	{
		if (_components.size() <= 0)
			return false;

		for (int i = _components.size() - 1; i >= 0; i--)
		{
			_components.get(i).RemoveComponent();
			_components.remove(i);
			System.gc();
		}

		return true;
	}


	public void AddComponent(Component c, Class systemName)
	{
		// System.out.println(GetComponent(c.getClass()));

		if (GetComponent(c.getClass(), true) != null && !c.CanHaveMultipleComponentsOfThisType)
		{

			System.err.println("ERROR: Cannot have more than one " + c.Name() + " on this entity!");
			c.RemoveComponent();

			return;
		}

		_components.add(c);
		//System.out.println(String.format("%s: [%s added]", this.Name, c.Name()));

		c.Enable(this, systemName);
	}

	public void AddComponent(Component c)
	{
		AddComponent(c, null);
	}


	private <T extends Component> T GetComponent(Class<T> clazz, boolean internalLookup)
	{

		for (int i = 0; i < _components.size(); i++)
		{
			if (clazz.isAssignableFrom(_components.get(i).getClass()))
			{
				return clazz.cast(_components.get(i));
			}
		}

		if (!internalLookup)
		{
			throw new RuntimeException("ERROR - could not find " + clazz.getSimpleName() + " on " + this.Name);
			
		}
		else
			return null;
	}
	
	public <T extends Component> T GetComponent(Class<T> clazz)
	{
		return GetComponent(clazz, false);
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

	public float GetPositionX()
	{
		return _transform.PositionX;
	}

	public float GetPositionY()
	{
		return _transform.PositionY;
	}

	public void SetPosition(float x, float y)
	{
		_transform.PositionX = x;
		_transform.PositionY = y;
	}
	
	public void AddPosition(float x, float y)
	{
		_transform.PositionX += x;
		_transform.PositionY += y;
	}

	public String GetPositionString()
	{
		return "X: " + _transform.PositionX + "; Y: " + _transform.PositionY;
	}

	public String GetScaleString()
	{
		return "X: " + _transform.ScaleX + "; Y: " + _transform.ScaleY;
	}

	public void SetScale(float x, float y)
	{
		_transform.ScaleX = x;
		_transform.ScaleY = y;
	}

	public void SetRotation(float rotation)
	{
		_transform.Rotation = rotation;
	}

	public void DestroyEntity()
	{
		// TODO: destroy this (garbage collector?)
	}


}
