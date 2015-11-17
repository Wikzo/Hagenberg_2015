package net.gustavdahl.engine.components;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.systems.GameLoopSystem;
import net.gustavdahl.engine.systems.PhysicsSystem;

public class TransFormComponent extends Component
{

	public Vector2 Position;
	public float Rotation;
	public Vector2 Scale;

	
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

	
	public void AddPosition(Vector2 pos)
	{
		this.Position.add(pos);
	}

	@Override
	public void Update(float deltaTime)
	{
		
	}

}
