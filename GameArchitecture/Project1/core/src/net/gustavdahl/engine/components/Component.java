package net.gustavdahl.engine.components;

import net.gustavdahl.engine.entities.Entity;

public  class Component implements IComponent, IUpdatable
{

	public Entity Owner;
	public boolean CanHaveMultipleComponentsOfThisType;
	
	protected boolean _isActive;
	protected boolean _hasBeenInitialized;
	
	protected TransFormComponent _transform;
	
	public Component()
	{
		CanHaveMultipleComponentsOfThisType = true;
		
		System.out.println("[" + Name() + " created]");
	}
	
	public void Enable(Entity owner)
	{
		this.Owner = owner;
		
		System.out.println("[" + Name() + " enabled]");
		Initialize();
		
		
		_hasBeenInitialized = true;
		_isActive = true;
		
		GetExternalReferences();
		
	}
	
	public void Disable()
	{
		_isActive = false;
	}
	
	public void Reset()
	{
		
	}

	public void Destroy()
	{
		// TODO: make sure that the object is destroyed/disposed of!
		_isActive = false;
		System.out.println("[" + Name() + " destroyed]");
	}
	
	public int UpdateOrder()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void Update()
	{
		// TODO Auto-generated method stub
		
	}

	public String Name()
	{
		return this.getClass().getSimpleName();
	}

	
	public void Initialize() // this is for internal stuff for this class only
	{
		
	}

	public void GetExternalReferences()
	{
		_transform = Owner.GetTransform();
		
	}

	public void SetActive(boolean active)
	{
		_isActive = active;
	}
	
	public boolean IsActive()
	{
		return _isActive;
	}
	
	public boolean HasBeenInitialized()
	{
		return _hasBeenInitialized;
	}


}
