package net.gustavdahl.engine.components;

import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.ServiceLocator;

public  class Component implements IComponent, IUpdatable
{

	public Entity Owner;
	public boolean CanHaveMultipleComponentsOfThisType;
	
	protected boolean _isActive;
	protected boolean _hasBeenInitialized;
	
	protected TransFormComponent _transform;
	
	public String DefaultSystem = "PhysicsSystem";
	
	public int UpdatePriority = 1;
	
	public Component()
	{
		CanHaveMultipleComponentsOfThisType = true;
		
		System.out.println("[" + Name() + " created]");
	}
	
	public void Enable(Entity owner, String systemName)
	{
		this.Owner = owner;
		
		System.out.println("[" + Name() + " enabled]");
		Initialize();
		
		
		_hasBeenInitialized = true;
		_isActive = true;
		
		//ServiceLocator.AddComponentToSystem(this, systemName);
		ServiceLocator.GetSystem(systemName).AddToSystem(this);
		//System.out.println("Component successfully added to appropriate system: " + added);
		
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
		GetExternalReferences();
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
