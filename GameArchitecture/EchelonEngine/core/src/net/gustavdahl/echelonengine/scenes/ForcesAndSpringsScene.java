package net.gustavdahl.echelonengine.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
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
import net.gustavdahl.echelonengine.systems.GameLoopSystem;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
import net.gustavdahl.echelonengine.systems.RenderSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class ForcesAndSpringsScene extends BaseScene
{
	private float _timer;
	private boolean _spawnEntities = false;
	private boolean _useFixedUpdate = true;

	public ForcesAndSpringsScene(Game game, CircleMenuList circleMenu, ServiceLocator serviceLocator)
	{
		super(game, circleMenu, serviceLocator);
	}

	void InitializeSystems(ServiceLocator serviceLocator)
	{
		super.InitializeSystems(serviceLocator);
		_serviceLocator.GetSystem(PhysicsSystem.class).SetUseFixedUpdate(false);
		_debugSystem.SetActive(true);

	}

	@Override
	public void CreateScene()
	{
		Entity e1 = _entityFactory.CreateStaticManWithBoxCollider("Static1", 100, 200, 0, 1, 1);
		Entity e2 = _entityFactory.CreateStaticManWithCircleCollider("Static2", 500, 200);
		Entity e4 = _entityFactory.CreateAnimatedMan("Animated_1", 700, 200);

		EntityFactory f = new EntityFactory();
		// f.CreateSingleSpring("Cog1", 300, 300);
		f.CreateMultipleSprings("COG_ROOT", 300, 300, 5);

	}

	@Override
	public void UpdateScene(float deltaTime)
	{

		if (Gdx.input.isKeyJustPressed(Keys.P))
		{
			_useFixedUpdate = !_useFixedUpdate;
			ServiceLocator.GetSystem(PhysicsSystem.class).SetUseFixedUpdate(_useFixedUpdate);
		}

		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText(" ");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("-- Forces & Springs --");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("PRESS O TO TOGGLE ENTITIES SPAWNING");
		ServiceLocator.GetSystem(DebugSystem.class).AddDebugText("PRESS P TO TOGGLE FIXED/VARIED PHYSICS UPDATE");

		if (_spawnEntities)
			_timer += deltaTime;

		if (_timer > 0.01)
		{
			_timer = 0;

			float x = MathUtils.random(Gdx.graphics.getWidth() + 800);
			float y = MathUtils.random(Gdx.graphics.getHeight() + 800);

			Entity e = _entityFactory.CreateAnimatedMan("StressTest_Man", x, y);
			e.AddComponent(new PhysicsBody().AddConstantForce(PhysicsBody.GravityForce));

		}

	}
}
