package net.gustavdahl.echelonengine.components;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.systems.GameLoopSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;

public class TransFormComponent extends Component
{
	public float PositionX, PositionY;
	public float Rotation;
	public float ScaleX, ScaleY;


	// TODO: store position as floats in array [x][y][rotation]
	// do this for physics body
	// same with velocityX, velocityY and angularVelocity
	// same with acceleration...
	
	//public float[]
	
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
