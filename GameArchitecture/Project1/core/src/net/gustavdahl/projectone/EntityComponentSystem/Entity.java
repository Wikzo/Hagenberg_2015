package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.List;

public class Entity
{
	
	private String _ID;
	private Transform _transform;
	private List<Component> components;
	private boolean _isActive;
	private Entity _parent;

	public Entity()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void SetActive() {}
	
	public void AddComponent() {}
	
	// bool indicates whether the component was successfully retrieved
	public boolean RemoveComponent(Component c) { return false;}
	public boolean RemoveAllComponents() { return false;}
	public boolean GetComponent(Component c) { return false;}

	public Transform GetTransform()
	{
		return _transform;
	}

	public void SetTransform(Transform transform)
	{
		_transform = transform;
	}

}
