package net.gustavdahl.echelonengine.entities;

import java.util.Random;
import java.util.UUID;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder.Body;

import net.gustavdahl.echelonengine.components.CircleCollider;
import net.gustavdahl.echelonengine.components.EditorComponent;
import net.gustavdahl.echelonengine.components.PhysicsBody;
import net.gustavdahl.echelonengine.components.SpringComponent;
import net.gustavdahl.echelonengine.components.SpriteComponent;
import net.gustavdahl.echelonengine.systems.ColliderSystem;
import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.EditorSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class EntityFactory
{

	public EntityFactory()
	{
		
	}
	
	private Entity CreateEntity(String name, Float x, Float y, TextureRegion texture)
	{
		UUID id = UUID.randomUUID();
		
		Entity e = new Entity(name);
		e.ID = id;
		e.SetPosition(x, y);
		
		AddRenderComponent(e, texture);
		
		return e;
	}
	
	private void AddRenderComponent(Entity e, TextureRegion texture)
	{

		e.AddComponent(new SpriteComponent(texture).SetOriginCenter().Color(Color.WHITE), RenderSystem.class);
		e.AddComponent(new CircleCollider(texture.getRegionWidth()/2), ColliderSystem.class);
		e.GetComponent(CircleCollider.class).AddToSystem(DebugSystem.class);

		e.AddComponent(new EditorComponent(), EditorSystem.class);
	}
	
	private PhysicsBody AddPhysicsBody(Entity e)
	{
		PhysicsBody body = new PhysicsBody();
		
		//body.AddConstantForce(PhysicsBody.GravityForce);
		body.SetMass(2);

		e.AddComponent(body);
		
		return body;

		
	}
	
	public void CreateSingleSpring(String name, float x, float y)
	{
		// create entity
		Entity e = CreateEntity(name, x, y ,ServiceLocator.AssetManager.CogWheels[0]);
		
		// add physics body
		PhysicsBody body = AddPhysicsBody(e);
		
		// add spring
		SpringComponent spring = new SpringComponent(body);
		e.AddComponent(spring);
		spring.SetSpringConstant(1);
		spring.AddToSystem(DebugSystem.class);
		
		//return e;
	}
	
	//public 

}
