package net.gustavdahl.engine.components;

import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.PhysicsSystem;
import net.gustavdahl.engine.systems.RenderSystem;
import net.gustavdahl.engine.systems.ServiceLocator;

public class Component implements IComponent, IUpdatable
{

	public Entity Owner;
	public boolean CanHaveMultipleComponentsOfThisType;
	
	protected boolean _isActive;
	protected boolean _hasBeenInitialized;
	
	protected TransFormComponent Transform;
	
	public Class DefaultSystem = PhysicsSystem.class;
	
	public int UpdatePriority = 1;
	
	public Component()
	{
		CanHaveMultipleComponentsOfThisType = true;
		
		System.out.println("[" + Name() + " created]");
	}
	
	public void Enable(Entity owner, Class systemClass)
	{
		this.Owner = owner;
		
		System.out.println("[" + Name() + " enabled]");
		Initialize();
		
		
		_hasBeenInitialized = true;
		_isActive = true;
		
		if (systemClass != null)
			ServiceLocator.GetSystem(systemClass).AddToSystem(this);
		
		GetExternalReferences();
		
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


	public void Update(float deltaTime)
	{
		
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
		Transform = Owner.GetTransform();
		
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
	
	@Override
	public String toString()
	{
		return this.Name();
	}


}
