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
import com.badlogic.gdx.utils.compression.lzma.Base;

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

public class SelectionScene extends BaseScene
{
	private final String _description1 = "-- Editor Selection --\nClick on entities to select them.\n";
	private final String _description2 = "Hold CTRL for multi-selection\n";
	private final String _description3 = "Hold any of the following buttons down while moving to perform an action:\n";
	private final String _description4 = "W = move, E = rotate, R = scale\n";
	private final String _fullDescription = _description1 + _description2 + _description3 + _description4;

	public SelectionScene(Game game, CircleMenuList circleMenu, ServiceLocator serviceLocator)
	{
		super(game, circleMenu, serviceLocator);
	}

	@Override
	public void CreateScene()
	{
		Entity e1 = _entityFactory.CreateStaticManWithBoxCollider("Static1", 100, 200, 0, 1, 1);
		Entity e2 = _entityFactory.CreateStaticManWithBoxCollider("Static2", 500, 200, 0, 1, 1);
		Entity e4 = _entityFactory.CreateAnimatedMan("Animated_1", 700, 200);
	}

	@Override
	public void render(float delta)
	{

		super.render(delta);

		_spriteBatch.begin();
		ServiceLocator.AssetManager.DebugFont2.draw(ServiceLocator.AssetManager.SpriteBatch, _fullDescription, 30, 450);
		ServiceLocator.AssetManager.DebugFont.draw(ServiceLocator.AssetManager.SpriteBatch,
				ServiceLocator.GetSystem(EditorSystem.class).GetEditorState(), 30, 300);
		_spriteBatch.end();

	}

	@Override
	public void UpdateScene(float deltaTime)
	{
	

	}

}
