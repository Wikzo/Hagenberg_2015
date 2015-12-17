package net.gustavdahl.echelonengine.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.swing.Spring;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder.Body;

import net.gustavdahl.echelonengine.components.BoxCollider;
import net.gustavdahl.echelonengine.components.CircleCollider;
import net.gustavdahl.echelonengine.components.EditorComponent;
import net.gustavdahl.echelonengine.components.PhysicsBody;
import net.gustavdahl.echelonengine.components.SpringComponent;
import net.gustavdahl.echelonengine.components.SpriteAnimator;
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

	private Entity CreateEntity(String name, Float x, Float y)
	{
		UUID id = UUID.randomUUID();

		Entity e = new Entity(name);
		ServiceLocator.EntityManager.AddEntity(e);
		e.ID = id;
		e.SetPosition(x, y);

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

	public SpringComponent CreateSingleSpring(Entity e, float x, float y, TextureRegion texture)
	{
		AddRenderComponent(e, texture);
		
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
		// TODO: make all springs connected two-ways

		Entity root = CreateEntity(name, x, y);
		AddRenderComponent(root, ServiceLocator.AssetManager.CogWheels[0]);
		// AddPhysicsBody(root);

		for (int i = 0; i < numberOfSprings - 1; i++)
		{

			Entity e = CreateEntity(name + i, x, y + (-i * 70));
			SpringComponent s = CreateSingleSpring(e, x, y,ServiceLocator.AssetManager.CogWheels[0]);
			s.SetRoot(root);

			if (i % 2 == 0)
				s.SetSpringColor(Color.YELLOW);
			else
				s.SetSpringColor(Color.BLUE);
			root = e;
		}

	}

	public Entity CreateAnimatedMan(String name, float x, float y)
	{

		// TODO: move running man texture region to asset manager

		Texture texture = ServiceLocator.AssetManager.RunningMan;
		TextureRegion[] r = SpriteAnimator.CreateSpriteSheet(texture, 30, 6, 5);
		Entity e = CreateEntity(name, x, y);
	

		// sprite animation
		e.AddComponent(new SpriteAnimator(r, 0.032f).Color(Color.WHITE)
				// .Offset(100, 0)
				.SetOriginCenter(), RenderSystem.class);

		e.AddComponent(new BoxCollider(e.GetComponent(SpriteComponent.class).GetWidth(),
				e.GetComponent(SpriteComponent.class).GetHeight()), ColliderSystem.class);

		e.GetComponent(BoxCollider.class).AddToSystem(DebugSystem.class);

		e.AddComponent(new EditorComponent(), EditorSystem.class);
		
		return e;

	}
	
	public Entity CreateStaticMan(String name, float x, float y)
	{

		// TODO: move running man texture region to asset manager
		Texture texture = ServiceLocator.AssetManager.RunningMan;
		TextureRegion[] r = SpriteAnimator.CreateSpriteSheet(texture, 30, 6, 5);

		Entity e = CreateEntity(name, x, y);
	
		e.AddComponent(new SpriteComponent(r[0]).SetOriginCenter().Color(Color.WHITE), RenderSystem.class);
		
		// circle collider
		e.AddComponent(new CircleCollider(r[0].getRegionWidth() / 2), ColliderSystem.class);
		e.GetComponent(CircleCollider.class).AddToSystem(DebugSystem.class);
		
		// box collider
		/*e.AddComponent(new BoxCollider(e.GetComponent(SpriteComponent.class).GetWidth(),
				e.GetComponent(SpriteComponent.class).GetHeight()), ColliderSystem.class);
		e.GetComponent(BoxCollider.class).AddToSystem(DebugSystem.class);*/

		e.AddComponent(new EditorComponent(), EditorSystem.class);
		
		return e;

	}

}
