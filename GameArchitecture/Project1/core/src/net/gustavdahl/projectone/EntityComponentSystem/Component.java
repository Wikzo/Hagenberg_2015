package net.gustavdahl.projectone.EntityComponentSystem;

public  class Component implements IComponent, IUpdatable
{

	public Entity Owner;
	public boolean CanHaveMultipleComponentsOfThisType;
	
	private boolean _isActive;
	
	public Component()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void Enable()
	{
		_isActive = true;
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
		// remove from owner's list
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
		// TODO Auto-generated method stub
		return null;
	}

	public void InitializeInternal()
	{
		// TODO Auto-generated method stub
		
	}

	public void GetExternalReferences()
	{
		// TODO Auto-generated method stub
		
	}

}
