package net.gustavdahl.echelonengine.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.physics.IPhysics;
import net.gustavdahl.echelonengine.enums.ForceMode;

public class PhysicsSystem extends BaseSystem<IPhysics>
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

	public PhysicsSystem(double targetFPS)
	{
		super();
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
		for (int i = 0; i < _componentList.size(); i++)
		{
			_componentList.get(i).Update(deltaTime);
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
	public boolean ValidateIfComponentCanBeAddedToSystem(Component c)
	{
		if (c instanceof IPhysics)
			return true;
		else
			return false;
		
	}

}
