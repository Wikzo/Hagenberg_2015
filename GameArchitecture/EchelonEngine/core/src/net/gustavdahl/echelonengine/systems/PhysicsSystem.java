package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.physics.IPhysics;
import net.gustavdahl.echelonengine.components.physics.PhysicsBody;
import net.gustavdahl.echelonengine.components.visual.IRenderable;
import net.gustavdahl.echelonengine.enums.ForceMode;

public class PhysicsSystem extends BaseSystem
{
	public ForceMode ForceMode;

	private boolean _usedFixedTimeStep = true;

	double _targetUpdateRate = 60;
	public double _fixedTimeStep;
	double _oldTime = 0;
	double _newTime;
	double _frameTime;
	double _accumulator = 0;
	double _frameTimeInSeconds = 0;

	private List<IPhysics> _physicsList;

	public PhysicsSystem(double targetFPS)
	{
		super();
		_physicsList = new ArrayList<IPhysics>();
		ForceMode = ForceMode.ExplicitEuler;

		_targetUpdateRate = targetFPS;
		_fixedTimeStep = 1d / _targetUpdateRate; // go from seconds to milliseconds
		
		_usedFixedTimeStep = false;

	}

	public PhysicsSystem SetForceMode(ForceMode forceMode)
	{
		this.ForceMode = forceMode;
		return this;
	}


	
	public double GetPhysicsUpdateRate()
	{
		if (_usedFixedTimeStep)
			return _targetUpdateRate;
		else
			return Gdx.graphics.getFramesPerSecond();			
	}

	public PhysicsSystem SetPhysicsUpdateRate(float updateRate)
	{
		_targetUpdateRate = updateRate;
		_fixedTimeStep = 1d / _targetUpdateRate;
		return this;
	}
	
	public PhysicsSystem SetUseFixedUpdate(Boolean useFixedUpdate)
	{
		_usedFixedTimeStep = useFixedUpdate;
	
		return this;
	}

	void UpdatePhysics(float deltaTime)
	{
		for (int i = 0; i < _physicsList.size(); i++)
		{
			_physicsList.get(i).Update(deltaTime);
		}
	}

	@Override
	public void Update(float deltaTime)
	{
		if (!_isActive)
			return;
		
		DebugSystem.AddDebugText("Using fixed physics update: " + _usedFixedTimeStep);
		if (_usedFixedTimeStep)
			DebugSystem.AddDebugText("Fixed physics update: " + GetPhysicsUpdateRate());
		
		DebugSystem.AddDebugText("Render FPS: " + Gdx.graphics.getFramesPerSecond());
		DebugSystem.AddDebugText("Number of entities: " + ServiceLocator.EntityManager.GetEntityCount());
				

		if (!_usedFixedTimeStep) // varied update
		{
			UpdatePhysics(deltaTime);
		} else // fixed update
		{
			// TODO: does not work well with springs?
			
			// http://gafferongames.com/game-physics/fix-your-timestep/

			_newTime = TimeUtils.nanoTime();
			_frameTime = _newTime - _oldTime;
			_oldTime = _newTime;

			// my frame rate
			_frameTimeInSeconds = (double) _frameTime / 1000000000.0;
			_accumulator += _frameTimeInSeconds;

			while (_accumulator >= _fixedTimeStep)
			{
				_accumulator -= _fixedTimeStep;
				
			}

			UpdatePhysics((float) _fixedTimeStep);
			
			// TODO: use interpolation to make renderer more "stable"
			// interpolate between previous and current Transform state
		}
	}



	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof IPhysics)
		{
			succesfullyAdded = true;
			_physicsList.add((IPhysics) c);

		} else
			throw new RuntimeException("ERROR - component " + c.getClass().getSimpleName()
					+ " doesn't implement PhysicsComponent interface!");

		return succesfullyAdded;
	}

}
