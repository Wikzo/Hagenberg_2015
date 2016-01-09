package net.gustavdahl.echelonengine.components;

import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public abstract class Component implements IComponent, IUpdatable
{

	public Entity Owner;
	public boolean CanHaveMultipleComponentsOfThisType;

	protected boolean _isActive;
	protected boolean _hasBeenInitialized;

	protected TransFormComponent Transform;

	public Class DefaultSystem = null;

	public int UpdatePriority = 1;

	public Component()
	{
		CanHaveMultipleComponentsOfThisType = true;

		//System.out.println("[" + Name() + " created]");
	}

	public void Enable(Entity owner, Class systemClass)
	{
		this.Owner = owner;

		// System.out.println("[" + Name() + " enabled]");
		Initialize();

		_hasBeenInitialized = true;
		_isActive = true;

		if (systemClass == null)
			AddToSystem(DefaultSystem);
		else
			AddToSystem(systemClass);

	}

	public Component AddToSystem(Class systemClass)
	{
		if (systemClass != null)
		{
			boolean added = ServiceLocator.GetSystem(systemClass).AddComponentToSystem(this);
			
			if (!added)
			System.out.println(String.format("ERROR! [%s] could NOT be added to [%s]! (%s)",
					this.Name(),
					systemClass.getSimpleName(),
					this.Owner.Name));
		}

		return this;
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

	public abstract void Update(float deltaTime);

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
