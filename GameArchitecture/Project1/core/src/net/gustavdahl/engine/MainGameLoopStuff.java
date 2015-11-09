package net.gustavdahl.engine;

import static org.junit.Assert.assertNotNull;

import java.io.Console;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;

import net.gustavdahl.engine.components.IUpdatable;
import net.gustavdahl.engine.components.BoxCollider;
import net.gustavdahl.engine.components.CircleCollider;
import net.gustavdahl.engine.components.Collider;
import net.gustavdahl.engine.components.ConstantForce;
import net.gustavdahl.engine.components.DebugComponent;
import net.gustavdahl.engine.components.EditorComponent;
import net.gustavdahl.engine.components.SpriteAnimator;
import net.gustavdahl.engine.components.SpriteComponent;
import net.gustavdahl.engine.components.Text;
import net.gustavdahl.engine.components.TransFormComponent;
import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.MyAssetManager;
import net.gustavdahl.engine.systems.DebugSystem;
import net.gustavdahl.engine.systems.EditorSystem;
import net.gustavdahl.engine.systems.GameLoopSystem;
import net.gustavdahl.engine.systems.PhysicsSystem;
import net.gustavdahl.engine.systems.RenderSystem;
import net.gustavdahl.engine.systems.ServiceLocator;

public class MainGameLoopStuff implements Screen, IUpdatable
{

	public static OrthographicCamera _camera;
	private SpriteBatch _spriteBatch;
	private ServiceLocator _serviceLocator;

	Game _game;
	Entity _entity1;
	Entity _entity2;

	public MainGameLoopStuff(Game game, ServiceLocator serviceLocator)
	{
		_game = game;

		InitializeSystems(serviceLocator);

		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);

		create();
	}

	void InitializeSystems(ServiceLocator serviceLocator)
	{
		RenderSystem _renderSystem = new RenderSystem(ServiceLocator.AssetManager.SpriteBatch);
		DebugSystem _debugSystem = new DebugSystem(ServiceLocator.AssetManager.SpriteBatch,
				ServiceLocator.AssetManager.ShapeRenderer);
		PhysicsSystem _physicsSystem = new PhysicsSystem();
		GameLoopSystem _gameLogicSystem = new GameLoopSystem(this);
		EditorSystem _editorSystem = new EditorSystem(_camera);

		_serviceLocator = serviceLocator;
		_serviceLocator.RegisterNewSystem(_renderSystem);
		_serviceLocator.RegisterNewSystem(_debugSystem);
		_serviceLocator.RegisterNewSystem(_physicsSystem);
		_serviceLocator.RegisterNewSystem(_gameLogicSystem);
		_serviceLocator.RegisterNewSystem(_editorSystem);
		_serviceLocator.InitializeSystems();
	}

	public void create()
	{


		_entity1 = new Entity("RunningMan");
		_entity2 = new Entity("StaticMan");
		_entity1.SetTransform(new Vector2(200,200));
		_entity2.SetTransform(new Vector2(100,300));

		Texture texture = _serviceLocator.AssetManager.RunningMan;

		TextureRegion[] r = SpriteAnimator.CreateSpriteSheet(texture, 30, 6, 5);

		// sprite animation
		_entity1.AddComponent(new SpriteAnimator(r, 0.032f).Color(Color.WHITE)
				// .Offset(100, 0)
				.SetOriginCenter(), RenderSystem.class);

		// static sprite
		_entity2.AddComponent(new SpriteComponent(r[0]).Color(Color.WHITE), RenderSystem.class);


		_entity1.AddComponent(
				new DebugComponent(_serviceLocator.AssetManager.DebugFont)
				.SetRenderPosition(true)
				.SetRenderName(true),
				DebugSystem.class);

		//_entity1.AddComponent(new CircleCollider(50f), DebugSystem.class);
		_entity1.AddComponent(new BoxCollider(200, 200), DebugSystem.class);
		
		_entity2.AddComponent(new CircleCollider(50f), DebugSystem.class);
		
		_entity1.AddComponent(new EditorComponent(), EditorSystem.class);
		_entity2.AddComponent(new EditorComponent(), EditorSystem.class);

	}


	
	@Override
	public void render(float delta)
	{

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_camera.update();
		


		ServiceLocator.AssetManager.SpriteBatch.setProjectionMatrix(_camera.combined);
		ServiceLocator.AssetManager.ShapeRenderer.setProjectionMatrix(_camera.combined);

		_serviceLocator.UpdateSystems(delta);

		// TODO: remember to apply stage viewport for camera

		// TODO: get PickRay (for mouse input editor) via camera

		// TODO: make camera system independt of render system

		// TODO: physics (box-aligned vs. axis-aligned bounding box)

	}

	@Override
	public void Update(float deltaTime)
	{

		//CircleCollider c1 =  (CircleCollider) _entity1.GetComponent(CircleCollider.class);
		BoxCollider c1 =  (BoxCollider) _entity1.GetComponent(BoxCollider.class);
		CircleCollider c2 =  (CircleCollider) _entity2.GetComponent(CircleCollider.class);
		
		//((CircleCollider) _entity1.GetComponent(CircleCollider.class)).CheckCircleCollision(c2);
		
		
		if (Gdx.input.isTouched())
		{
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

			// NOTE: ray check should happen before unprojecting the mouse position!
			//Ray ray = _camera.getPickRay(touchPos.x, touchPos.y);
			//Collider.Intersect(ray, c1);
			//Collider.Intersect(ray, c2);
			
			_camera.unproject(touchPos);
			
			
			


			if (_entity1.GetComponent(ConstantForce.class) != null)
				_entity1.GetComponent(ConstantForce.class).SetActive(false);

		} else
		{
			if (_entity1.GetComponent(ConstantForce.class) != null)
				_entity1.GetComponent(ConstantForce.class).SetActive(true);
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
