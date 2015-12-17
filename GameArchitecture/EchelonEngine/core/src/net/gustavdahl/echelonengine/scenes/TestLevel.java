package net.gustavdahl.echelonengine.scenes;

//import static org.junit.Assert.assertNotNull;

import java.io.Console;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;

import net.gustavdahl.echelonengine.components.BoxCollider;
import net.gustavdahl.echelonengine.components.CircleCollider;
import net.gustavdahl.echelonengine.components.Collider;
import net.gustavdahl.echelonengine.components.ConstantForce;
import net.gustavdahl.echelonengine.components.DebugComponent;
import net.gustavdahl.echelonengine.components.EditorComponent;
import net.gustavdahl.echelonengine.components.IUpdatable;
import net.gustavdahl.echelonengine.components.PhysicsBody;
import net.gustavdahl.echelonengine.components.SpringComponent;
import net.gustavdahl.echelonengine.components.SpriteAnimator;
import net.gustavdahl.echelonengine.components.SpriteComponent;
import net.gustavdahl.echelonengine.components.Text;
import net.gustavdahl.echelonengine.components.TransFormComponent;
import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.entities.EntityFactory;
import net.gustavdahl.echelonengine.enums.ForceMode;
import net.gustavdahl.echelonengine.systems.ColliderSystem;
import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.EditorSystem;
import net.gustavdahl.echelonengine.systems.GameLoopSystem;
import net.gustavdahl.echelonengine.systems.MyAssetManager;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class TestLevel implements Screen, IUpdatable
{
	public static OrthographicCamera _camera; // TODO: don't make public static
	private SpriteBatch _spriteBatch;
	private ServiceLocator _serviceLocator;

	Game _game;
	Entity _entity1;
	Entity _entity2;
	Entity _entity3;

	public TestLevel(Game game, ServiceLocator serviceLocator)
	{
		_game = game;

		InitializeSystems(serviceLocator);

		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);

		create();
	}

	DebugSystem _debugSystem;

	void InitializeSystems(ServiceLocator serviceLocator)
	{
		RenderSystem _renderSystem = new RenderSystem(ServiceLocator.AssetManager.SpriteBatch);
		_debugSystem = new DebugSystem(ServiceLocator.AssetManager.SpriteBatch,
				ServiceLocator.AssetManager.ShapeRenderer, ServiceLocator.AssetManager.DebugFont, _camera);
		PhysicsSystem _physicsSystem = new PhysicsSystem(60d).SetForceMode(ForceMode.ExplicitEuler);
		ColliderSystem _colliderSystem = new ColliderSystem();
		GameLoopSystem _gameLogicSystem = new GameLoopSystem(this);
		EditorSystem _editorSystem = new EditorSystem(_camera);

		_serviceLocator = serviceLocator;
		_serviceLocator.RegisterNewSystem(_renderSystem);
		_serviceLocator.RegisterNewSystem(_debugSystem);
		_serviceLocator.RegisterNewSystem(_physicsSystem);
		_serviceLocator.RegisterNewSystem(_colliderSystem);
		_serviceLocator.RegisterNewSystem(_gameLogicSystem);
		_serviceLocator.RegisterNewSystem(_editorSystem);
		_serviceLocator.InitializeSystems();
		
		//_colliderSystem.SetActive(false);
	}

	TextureRegion[] r;

	public void create()
	{

		EntityFactory f = new EntityFactory();
		//f.CreateSingleSpring("Cog1", 300, 300);
		f.CreateMultipleSprings("COG_ROOT", 300, 300, 5);
		
		Texture texture = _serviceLocator.AssetManager.RunningMan;

		r = SpriteAnimator.CreateSpriteSheet(texture, 30, 6, 5);
		//r = SpriteAnimator.CreateSpriteSheet(texture, 3, 3, 1);
		
		Entity t = f.CreateAnimatedMan("RunningMan_Factory1", 100, 200);
		f.CreateAnimatedMan("RunningMan_Factory2", 400, 200);
		f.CreateStaticMan("StaticMan", 600, 200);

		for (int i = 0; i < 3; i++)
		{
			Entity e = new Entity("Man_" + i);
			e.SetPosition(300 + i * 170, 300);

			e.AddComponent(new SpriteComponent(r[0]).SetOriginCenter().Color(Color.WHITE), RenderSystem.class);

			e.AddComponent(new CircleCollider(50f), ColliderSystem.class);
			e.GetComponent(CircleCollider.class).AddToSystem(DebugSystem.class);

			e.AddComponent(new EditorComponent(), EditorSystem.class);

			// falling down, not static
			// if (i == 0)
			{
				e.AddComponent(new PhysicsBody());

				Random r = new Random();
				float mass = r.nextFloat() * 100;
				float damp = r.nextFloat() * 5;

				// add gravity
				PhysicsBody body = e.GetComponent(PhysicsBody.class);
				body.AddConstantForce(PhysicsBody.GravityForce);
				body.SetMass(2);
				// e.GetComponent(PhysicsComponent.class).SetMass(2).SetEulerMethod(EulerMethod.Explicit);

				SpringComponent spring = new SpringComponent(body);
				e.AddComponent(spring);
				spring.SetSpringConstant((i + 1) * 2);
				spring.AddToSystem(DebugSystem.class);

			}

		}

		_entity1 = new Entity("RunningMan_Original1");

		_entity2 = new Entity("RunningMan_Original2");
		// _entity3 = new Entity("StaticMan3");
		_entity1.SetPosition(100, 200);
		_entity2.SetPosition(200, 400);
		// _entity3.SetPosition(new Vector2(300,300));

		// _entity1.AddComponent(new PhysicsComponent());



		// sprite animation
		_entity1.AddComponent(new SpriteAnimator(r, 0.032f).Color(Color.WHITE)
				// .Offset(100, 0)
				.SetOriginCenter(), RenderSystem.class);

		_entity2.AddComponent(new SpriteAnimator(r, 0.032f).Color(Color.WHITE)
				// .Offset(100, 0)
				.SetOriginCenter(), RenderSystem.class);

		_entity1.AddComponent(new BoxCollider(_entity1.GetComponent(SpriteComponent.class).GetWidth(),
				_entity1.GetComponent(SpriteComponent.class).GetHeight()), ColliderSystem.class);

		_entity2.AddComponent(new BoxCollider(_entity2.GetComponent(SpriteComponent.class).GetWidth(),
				_entity1.GetComponent(SpriteComponent.class).GetHeight()), ColliderSystem.class);

		_entity1.GetComponent(BoxCollider.class).AddToSystem(DebugSystem.class);
		_entity2.GetComponent(BoxCollider.class).AddToSystem(DebugSystem.class);

		_entity1.AddComponent(new EditorComponent(), EditorSystem.class);
		_entity2.AddComponent(new EditorComponent(), EditorSystem.class);

		/*
		 * Entity floor = new Entity("Floor"); floor.AddComponent(new
		 * SpriteComponent(new
		 * TextureRegion(_serviceLocator.AssetManager.Floor)));
		 * floor.SetPosition(new Vector2(400,50)); floor.AddComponent(new
		 * BoxCollider(floor.GetComponent(SpriteComponent.class).GetWidth(),
		 * floor.GetComponent(SpriteComponent.class).GetHeight()).SetStatic(true
		 * ));
		 * floor.GetComponent(BoxCollider.class).AddToSystem(DebugSystem.class);
		 * floor.AddComponent(new EditorComponent());
		 */

		// _serviceLocator.GetSystem(DebugSystem.class).AddToSystem(_entity1.GetComponent(EditorComponent.class));
	}

	float fps = 1;
	@Override
	public void render(float delta)
	{

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_camera.update();

		ServiceLocator.AssetManager.SpriteBatch.setProjectionMatrix(_camera.combined);
		ServiceLocator.AssetManager.ShapeRenderer.setProjectionMatrix(_camera.combined);

		_debugSystem.GetCamera(_camera);

		_serviceLocator.UpdateSystems(delta);
		

		// TODO: remember to apply stage viewport for camera

		// TODO: get PickRay (for mouse input editor) via camera

		// TODO: make camera system independt of render system

		// TODO: physics (box-aligned vs. axis-aligned bounding box)

	}

	private float _timer;
	private int _entityNumber;

	@Override
	public void Update(float deltaTime)
	{
		// duplicate an entity (not working correctly yet!)
		if (Gdx.input.isKeyJustPressed(Keys.F2))
		{
			Entity e = _entity1.DuplicateEntity();
			// e.AddComponent(new TransFormComponent());
			e.SetPosition(_entity1.GetPositionX(), _entity1.GetPositionY());
			e.AddComponent(new SpriteAnimator(r, 0.032f).Color(Color.PINK)
					// .Offset(100, 0)
					.SetOriginCenter(), RenderSystem.class);

			e.AddComponent(new BoxCollider(e.GetComponent(SpriteComponent.class).GetWidth(),
					e.GetComponent(SpriteComponent.class).GetHeight()), ColliderSystem.class);

			e.GetComponent(BoxCollider.class).AddToSystem(DebugSystem.class);

			e.AddComponent(new EditorComponent(), EditorSystem.class);

			// _entity1.SetPosition(_entity1.GetPositionX() + 50, 20);
		}

		DebugSystem.AddDebugText("Number of entities: " + ServiceLocator.EntityManager.GetEntityCount());
		DebugSystem.AddDebugText("Render FPS: " + Gdx.graphics.getFramesPerSecond());
		DebugSystem.AddDebugText("Physics FPS: " + _serviceLocator.GetSystem(PhysicsSystem.class).GetPhysicsUpdateRate());

		// _timer+= deltaTime;

		if (_timer > 0.01)
		{
			_timer = 0;
			_entityNumber++;

			float x = MathUtils.random(Gdx.graphics.getWidth() + 800);
			float y = MathUtils.random(Gdx.graphics.getHeight() + 800);

			Entity e = new Entity("StressTest_" + _entityNumber);
			e.SetPosition(x + 20, y + 20);

			e.AddComponent(new SpriteComponent(r[0]).SetOriginCenter().Color(Color.WHITE), RenderSystem.class);

			e.AddComponent(new CircleCollider(50f).SetStatic(true), ColliderSystem.class);
			e.GetComponent(CircleCollider.class).AddToSystem(DebugSystem.class);

			e.AddComponent(new EditorComponent(), EditorSystem.class);
			e.AddComponent(new PhysicsBody());

		}

	}

	@Override
	public void dispose()
	{
		_serviceLocator.DestroyAllSystems();
		ServiceLocator.AssetManager.DisposeAllAssets();
	}

	@Override
	public void show()
	{

	}

	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{

	}
}
