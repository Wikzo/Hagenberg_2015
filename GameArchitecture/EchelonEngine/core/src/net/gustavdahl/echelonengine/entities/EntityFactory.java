package net.gustavdahl.echelonengine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.gustavdahl.echelonengine.components.colliders.BoxCollider;
import net.gustavdahl.echelonengine.components.colliders.CircleCollider;
import net.gustavdahl.echelonengine.components.editor.EditorComponent;
import net.gustavdahl.echelonengine.components.persistence.StaticManPersistence;
import net.gustavdahl.echelonengine.components.physics.PhysicsBody;
import net.gustavdahl.echelonengine.components.physics.SpringComponent;
import net.gustavdahl.echelonengine.components.visual.SpriteAnimator;
import net.gustavdahl.echelonengine.components.visual.SpriteComponent;
import net.gustavdahl.echelonengine.systems.ColliderSystem;
import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.EditorSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class EntityFactory
{

	private Entity CreateEntity(String name, Float x, Float y)
	{
		Entity e = new Entity(name);
		e.SetPosition(x, y);

		return e;
	}

	private void AddRenderComponent(Entity e, TextureRegion texture)
	{
		e.AddComponent(new SpriteComponent(texture).SetOriginCenter().SetColor(Color.WHITE), RenderSystem.class);
	}

	private void AddCircleCollider(Entity e)
	{
		e.AddComponent(new CircleCollider(e.GetComponent(SpriteComponent.class).GetHeight() / 2), ColliderSystem.class);
		e.GetComponent(CircleCollider.class).AddToSystem(DebugSystem.class);
		e.AddComponent(new EditorComponent(), EditorSystem.class);
	}

	private void AddBoxCollider(Entity e)
	{
		e.AddComponent(new BoxCollider(e.GetComponent(SpriteComponent.class).GetWidth(),
				e.GetComponent(SpriteComponent.class).GetHeight()), ColliderSystem.class);
		e.GetComponent(BoxCollider.class).AddToSystem(DebugSystem.class);
		e.AddComponent(new EditorComponent(), EditorSystem.class);
	}

	private PhysicsBody AddPhysicsBody(Entity e)
	{
		PhysicsBody body = new PhysicsBody(true);
		
		body.SetMass(5);

		e.AddComponent(body);

		return body;

	}

	private SpringComponent AddSpringComponent(Entity e, float x, float y, PhysicsBody body)
	{
		// add spring
		SpringComponent spring = new SpringComponent(body);
		e.AddComponent(spring);
		spring.SetSpringConstant(5f);
		spring.AddToSystem(DebugSystem.class);

		return spring;
	}

	public Entity CreateSingleSpring(String name, float x, float y)
	{
		Entity root = CreateEntity(name, x, y);
		AddRenderComponent(root, ServiceLocator.AssetManager.CogWheels[1]);
		AddCircleCollider(root);
		PhysicsBody body = AddPhysicsBody(root);
		SpringComponent spring = AddSpringComponent(root, x, y, body);
		spring.SetSpringConstant(20);
		spring.SetDamp(20);

		return root;
	}

	public void CreateMultipleSprings(String name, float x, float y, int numberOfSprings)
	{
		// TODO: make all springs connected two-ways

		Entity root = CreateSingleSpring(name, x, y);

		for (int i = 0; i < numberOfSprings - 1; i++)
		{

			Entity e = CreateEntity(name + i, x, y + (-i * 70));
			AddRenderComponent(e, ServiceLocator.AssetManager.CogWheels[0]);
			AddCircleCollider(e);
			PhysicsBody body = AddPhysicsBody(e);
			SpringComponent s = AddSpringComponent(e, x, y, body);
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

		TextureRegion[] r = ServiceLocator.AssetManager.RunningManRegion;
		Entity e = CreateEntity(name, x, y);

		// sprite animation
		e.AddComponent(new SpriteAnimator(r, 0.032f)
				// .Offset(100, 0)
				.SetOriginCenter(), RenderSystem.class);

		AddBoxCollider(e);

		return e;

	}

	public Entity CreateStaticManWithBoxCollider(String name, float x, float y, float angle, float scaleX, float scaleY)
	{

		TextureRegion[] r = ServiceLocator.AssetManager.RunningManRegion;

		Entity e = CreateEntity(name, x, y);
		e.SetRotation(angle);
		e.SetScale(scaleX, scaleY);

		AddRenderComponent(e, r[0]);
		AddBoxCollider(e);
		e.AddComponent(new StaticManPersistence());

		return e;

	}
	
	public Entity CreateStaticManWithCircleCollider(String name, float x, float y)
	{

		TextureRegion[] r = ServiceLocator.AssetManager.RunningManRegion;

		Entity e = CreateEntity(name, x, y);

		AddRenderComponent(e, r[0]);
		AddCircleCollider(e);

		return e;

	}

}
