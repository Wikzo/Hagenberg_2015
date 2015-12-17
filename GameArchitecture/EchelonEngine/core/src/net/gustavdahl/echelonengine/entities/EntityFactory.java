package net.gustavdahl.echelonengine.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.swing.Spring;

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
		ServiceLocator.EntityManager.AddEntity(e);
		e.ID = id;
		e.SetPosition(x, y);

		AddRenderComponent(e, texture);

		return e;
	}

	private void AddRenderComponent(Entity e, TextureRegion texture)
	{

		e.AddComponent(new SpriteComponent(texture).SetOriginCenter().Color(Color.WHITE), RenderSystem.class);
		e.AddComponent(new CircleCollider(texture.getRegionWidth() / 2), ColliderSystem.class);
		e.GetComponent(CircleCollider.class).AddToSystem(DebugSystem.class);

		e.AddComponent(new EditorComponent(), EditorSystem.class);
	}

	private PhysicsBody AddPhysicsBody(Entity e)
	{
		PhysicsBody body = new PhysicsBody();

		body.AddConstantForce(PhysicsBody.GravityForce);
		body.SetMass(5);

		e.AddComponent(body);

		return body;

	}

	private Entity CreateEntityWithSpring(String name, float x, float y)
	{
		Entity e = CreateEntity(name, x, y, ServiceLocator.AssetManager.CogWheels[0]);
		return e;
	}

	public SpringComponent CreateSingleSpring(Entity e, float x, float y)
	{

		// add physics body
		PhysicsBody body = AddPhysicsBody(e);

		// add spring
		SpringComponent spring = new SpringComponent(body);
		e.AddComponent(spring);
		spring.SetSpringConstant(5f);
		spring.AddToSystem(DebugSystem.class);

		return spring;
	}

	public void CreateMultipleSprings(String name, float x, float y, int numberOfSprings)
	{
		// Entity parent = CreateSingleSpring(name, x, y);
		Entity root = CreateEntity(name, x, y, ServiceLocator.AssetManager.CogWheels[1]);
		// AddPhysicsBody(root);

		for (int i = 0; i < numberOfSprings - 1; i++)
		{
			Entity e = CreateEntityWithSpring(name + i, x, y + (-i * 70));
			SpringComponent s = CreateSingleSpring(e, x, y);
			s.SetRoot(root);

			if (i % 2 == 0)
				s.SetSpringColor(Color.YELLOW);
			else
				s.SetSpringColor(Color.BLUE);
			root = e;
		}

	}

}
