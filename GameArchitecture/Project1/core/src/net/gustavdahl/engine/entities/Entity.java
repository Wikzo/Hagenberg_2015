package net.gustavdahl.engine.entities;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.ConstantForce;
import net.gustavdahl.engine.components.TransFormComponent;
import net.gustavdahl.engine.systems.BaseSystem;
import net.gustavdahl.engine.systems.GameLoopSystem;
import net.gustavdahl.engine.systems.PhysicsSystem;

public class Entity
{

	public String Name;
	private TransFormComponent _transform;
	private List<Component> _components;

	private boolean _isActive;
	private Entity _parent;
	
	public boolean CurrentlySelectedByEditor; // TODO: move this to EditorComponent

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

	// bool indicates whether the component was successfully retrieved
	public boolean RemoveComponentOfType(Class componentClass)
	{
		boolean removedComponent = false;

		for (int i = _components.size() - 1; i >= 0; i--)
		{

			// System.out.println(i);
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

		// TODO: be able to retrieve all subclasses, e.g. by specifying
		// "RenderComponent" and then get all sub-components
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

	public void AddComponent(Component c, Class systemName)
	{
		// System.out.println(GetComponent(c.getClass()));

		/*if (GetComponent(c.getClass()) != null && !c.CanHaveMultipleComponentsOfThisType)
		{
			System.err.println("ERROR: Cannot have more than one " + c.Name() + " on this entity!");
			c.Destroy();
			return;
		}*/
		_components.add(c);
		System.out.println("[" + c.Name() + " added to entity]");
		c.Enable(this, systemName);
	}

	public void AddComponent(Component c)
	{
		AddComponent(c, c.DefaultSystem);
	}

	public Component GetComponent_old(Class componentClass)
	{
		boolean found = false;
		for (Component c : _components)
		{
			if (c.getClass() == componentClass)
			{
				found = true;
				return c;
			}
		}

		// TODO: how to return null AND throw an exception at the same time?
		if (!found)
		{
			//throw new RuntimeException("ERROR - " + Name + " does not have a " + componentClass.getName());
			//System.out.println("ERROR - " + this.Name + " has no " + componentClass.getName());
			return null;
		}
		
		return null;
	}
	
	public <T extends Component> T GetComponent(Class<T> clazz)
	{
		
		for (int i = 0; i < _components.size(); i++)
		{
			//System.out.println("want: " + clazz.getName() + "; have: " + _components.get(i).Name());
			
			if (clazz.isAssignableFrom(_components.get(i).getClass()))
				//if (_components.get(i).getClass().isAssignableFrom(clazz))
			{
				//System.out.println("got " + _components.get(i).Name());
				return clazz.cast(_components.get(i));
				//return (T) _components.get(i);
			}
		}
		
		return null;
	}
	
	/*
	 * 	public static <T extends BaseSystem> T GetSystem(Class<T> clazz) 
	{
		for (int i = 0; i < _systems.size(); i++)
		{
			// check if clazz and system's class match
			if (_systems.get(i).getClass().isAssignableFrom(clazz))
			{
				//System.out.println(clazz.getName() + " was added to " + _systems.get(i).getClass());
				return clazz.cast(_systems.get(i));
				//return (T) _systems.get(i);
			}
		}
		
		return null;
		
	}
	 */

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

	public void SetPosition(Vector2 pos)
	{
		_transform.Position = pos;
	}
	
	public void AddPosition(Vector2 pos)
	{
		_transform.Position.add(pos);
	}
	
	public void SetScale(Vector2 scale)
	{
		_transform.Scale = scale;
	}
	
	public Vector2 GetScale()
	{
		return _transform.Scale;
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
