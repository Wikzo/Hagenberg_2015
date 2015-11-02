package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.math.Vector2;

public class TransFormComponent extends Component
{

	Vector2 Position;
	float Rotation;
	Vector2 Scale;

	public TransFormComponent()
	{
		Position = new Vector2(0,0);
		Rotation = 0f;
		Scale = new Vector2(1,1);
	}
	
	@Override
	public String Name()
	{
		return "Transform";
	}

}
