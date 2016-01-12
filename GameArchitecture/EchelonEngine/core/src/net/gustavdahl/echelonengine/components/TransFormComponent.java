package net.gustavdahl.echelonengine.components;

public class TransFormComponent extends Component
{
	public float PositionX, PositionY;
	public float Rotation;
	public float ScaleX, ScaleY;

	
	public TransFormComponent()
	{
		super();

		PositionX = 0;
		PositionY = 0;
		Rotation = 0f;
		ScaleX = 1;
		ScaleY = 1;
		
		CanHaveMultipleComponentsOfThisType = false;
	}
	
	
	@Override
	public String Name()
	{
		return "Transform";
	}
	
	@Override
	public void Update(float deltaTime)
	{
		
	}

}
