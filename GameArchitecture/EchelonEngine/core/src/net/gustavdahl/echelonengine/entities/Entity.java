package net.gustavdahl.echelonengine.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.ConstantForce;
import net.gustavdahl.echelonengine.components.TransFormComponent;
import net.gustavdahl.echelonengine.systems.BaseSystem;
import net.gustavdahl.echelonengine.systems.GameLoopSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
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

		if (GetComponent(c.getClass()) != null && !c.CanHaveMultipleComponentsOfThisType)
		{

			System.err.println("ERROR: Cannot have more than one " + c.Name() + " on this entity!");
			c.Destroy();

			return;
		}

		_components.add(c);
		System.out.println(String.format("%s: [%s added]", this.Name, c.Name()));

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
			// throw new RuntimeException("ERROR - " + Name + " does not have a
			// " + componentClass.getName());
			// System.out.println("ERROR - " + this.Name + " has no " +
			// componentClass.getName());
			return null;
		}

		return null;
	}

	public <T extends Component> T GetComponent(Class<T> clazz)
	{

		for (int i = 0; i < _components.size(); i++)
		{
			// System.out.println("want: " + clazz.getName() + "; have: " +
			// _components.get(i).Name());

			if (clazz.isAssignableFrom(_components.get(i).getClass()))
			// if (_components.get(i).getClass().isAssignableFrom(clazz))
			{
				// System.out.println("got " + _components.get(i).Name());
				return clazz.cast(_components.get(i));
				// return (T) _components.get(i);
			}
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

	// not working correctly yet!
	public Entity DuplicateEntity()
	{
		Entity e = new Entity(this.Name + "_Copy");

		List<Component> originalComponents = GetAllComponents();
		List<Component> duplicatedComponents = new ArrayList<Component>(originalComponents);

		for (int i = 0; i < duplicatedComponents.size(); i++)
		{
			if (!(duplicatedComponents.get(i) instanceof TransFormComponent))
			{
				e.AddComponent(duplicatedComponents.get(i));
				duplicatedComponents.get(i).Owner = e;
			} else
			{
				e.AddComponent(new TransFormComponent());
				// e.SetPosition(this.GetPositionX() + 50, this.GetPositionY());
			}
			System.out.println(duplicatedComponents.get(i).Name());
		}

		return e;
	}

}
