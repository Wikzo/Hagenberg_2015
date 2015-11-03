package net.gustavdahl.projectone.EntityComponentSystem;

public  class Component implements IComponent, IUpdatable
{

	public Entity Owner;
	public boolean CanHaveMultipleComponentsOfThisType;
	
	private boolean _isActive;
	private boolean _hasBeenInitialized;
	
	private TransFormComponent _transform;
	
	public Component()
	{
		CanHaveMultipleComponentsOfThisType = true;
		
		System.out.println("[" + Name() + " created]");
	}
	
	public void Enable()
	{
		Initialize();
		GetExternalReferences();
		
		_hasBeenInitialized = true;
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

	public boolean IsActive()
	{
		return _isActive;
	}
	
	public boolean HasBeenInitialized()
	{
		return _hasBeenInitialized;
	}


}
