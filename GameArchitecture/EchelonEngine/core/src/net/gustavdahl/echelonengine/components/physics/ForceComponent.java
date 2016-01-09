package net.gustavdahl.echelonengine.components.physics;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;

public abstract class ForceComponent extends Component implements IPhysics
{
	public PhysicsBody _body;
	
	public ForceComponent(PhysicsBody b)
	{
		_body = b;
		DefaultSystem = PhysicsSystem.class;
	}
	
	protected abstract Vector2 CalculateForce();
	
	@Override
	public void Update(float deltaTime)
	{
		AddForce();
	}
	
	protected void AddForce()
	{
		_body.AddForce(CalculateForce());
	}
	

}
