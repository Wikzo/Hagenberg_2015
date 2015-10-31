package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.math.Vector2;

public class Transform extends Component
{

	Vector2 Position;
	float Rotation;
	Vector2 Scale;
	
	public Transform()
	{
		
	}
	
	@Override
	public String Name()
	{
		return "Transform";
	}

}
