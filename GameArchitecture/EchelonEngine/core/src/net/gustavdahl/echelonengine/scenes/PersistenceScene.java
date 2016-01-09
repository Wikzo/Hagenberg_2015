package net.gustavdahl.echelonengine.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
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

public class PersistenceScene extends BaseScene
{
	public PersistenceScene(Game game, CircleMenuList circleMenu, ServiceLocator serviceLocator)
	{
		super(game, circleMenu, serviceLocator);
	}


	void InitializeSystems(ServiceLocator serviceLocator)
	{
		super.InitializeSystems(serviceLocator);		
		_debugSystem.SetActive(true);
	}
	

	@Override
	public void CreateScene()
	{

	}
	
	@Override
	public void UpdateScene(float deltaTime)
	{
	}
}
