package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.ArrayList;
import java.util.List;

public class Entity
{
	
	private String _ID;
	private TransFormComponent _transform;
	private ArrayList<Component> _components;
	private boolean _isActive;
	private Entity _parent;

	public Entity()
	{
		_transform = new TransFormComponent();
		_components = new ArrayList<Component>();
	}
	
	public void SetActive() {}
	
	public void AddComponent(Component c)
	{
		_components.add(c);
		c.Enable();
	}
	
	// bool indicates whether the component was successfully retrieved
	public boolean RemoveComponent(Component c) { return false;}
	public boolean RemoveAllComponents() { return false;}
	public boolean GetComponent(Component c) { return false;}

	public TransFormComponent GetTransform()
	{
		return _transform;
	}

	public void SetTransform(TransFormComponent transform)
	{
		_transform = transform;
	}

}
