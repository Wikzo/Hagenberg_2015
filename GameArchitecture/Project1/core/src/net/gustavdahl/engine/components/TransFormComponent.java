package net.gustavdahl.engine.components;

import com.badlogic.gdx.math.Vector2;

public class TransFormComponent extends Component
{

	Vector2 Position;
	float Rotation;
	Vector2 Scale;

	
	public TransFormComponent()
	{
		super();
		Position = new Vector2(0,0);
		Rotation = 0f;
		Scale = new Vector2(1,1);
		
		CanHaveMultipleComponentsOfThisType = false;
	}
	
	@Override
	public String Name()
	{
		return "Transform";
	}
	
	public void Translate(Vector2 translation)
	{
		if (!IsActive())
			return;
		
		Position = Position.add(translation);
	}

}
