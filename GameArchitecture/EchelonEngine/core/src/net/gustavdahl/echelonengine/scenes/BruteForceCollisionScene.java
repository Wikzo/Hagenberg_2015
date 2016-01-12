package net.gustavdahl.echelonengine.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import net.gustavdahl.echelonengine.components.IUpdatable;
import net.gustavdahl.echelonengine.components.physics.PhysicsBody;
import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.entities.EntityFactory;
import net.gustavdahl.echelonengine.enums.CollisionMode;
import net.gustavdahl.echelonengine.enums.ForceMode;
import net.gustavdahl.echelonengine.menus.CircleMenuList;
import net.gustavdahl.echelonengine.systems.ColliderSystem;
import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.EditorSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class BruteForceCollisionScene extends BaseScene
{
	private float _timer;
	private boolean _spawnEntities = false;
	private boolean _useFixedUpdate = true;

	public BruteForceCollisionScene(Game game, CircleMenuList circleMenu, ServiceLocator serviceLocator)
	{
		super(game, circleMenu, serviceLocator);
	}

	void InitializeSystems(ServiceLocator serviceLocator)
	{
		super.InitializeSystems(serviceLocator);
		_debugSystem.SetActive(true);

	}

	public void CreateScene()
	{
		_entityFactory.CreateStaticManWithBoxCollider("StaticBox1", 444, 211, 0, 1, 1);
		_entityFactory.CreateStaticManWithBoxCollider("StaticBox2", 348, 100, 0, 1, 1);

		_entityFactory.CreateStaticManWithCircleCollider("StaticCircle1", 160, 210);
		_entityFactory.CreateStaticManWithCircleCollider("StaticCircle2", 140, 100);
		_entityFactory.CreateStaticManWithCircleCollider("StaticCircle3", 580, 270);

		_entityFactory.CreateAnimatedMan("Animated_1", 613, 132);
		_entityFactory.CreateAnimatedMan("Animated_2", 710, 270);
	}

	@Override
	public void UpdateScene(float deltaTime)
	{

		if (Gdx.input.isKeyJustPressed(Keys.O))
			_spawnEntities = !_spawnEntities;

		if (Gdx.input.isKeyJustPressed(Keys.P))
		{
			_useFixedUpdate = !_useFixedUpdate;
			ServiceLocator.GetSystem(PhysicsSystem.class).SetUseFixedUpdate(_useFixedUpdate);
		}

		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText(" ");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("-- Brute Force Collision --");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("Press O to toggle entities spawning");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("Press P to toggle fixed/varied physics update");

		if (_spawnEntities)
			_timer += deltaTime;

		if (_timer > 0.01)
		{
			_timer = 0;

			float x = MathUtils.random(Gdx.graphics.getWidth() + 800);
			float y = MathUtils.random(Gdx.graphics.getHeight() + 800);

			Entity e = _entityFactory.CreateAnimatedMan("StressTest_Man", x, y);
			e.AddComponent(new PhysicsBody(true));

		}

	}
}
