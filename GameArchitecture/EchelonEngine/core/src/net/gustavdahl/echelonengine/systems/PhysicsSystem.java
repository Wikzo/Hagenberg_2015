package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.IPhysics;
import net.gustavdahl.echelonengine.components.IRenderable;
import net.gustavdahl.echelonengine.components.PhysicsBody;

public class PhysicsSystem extends BaseSystem
{

	

	final double _fixedTimeStep = 0.016d;
	double _oldTime = 0;
	double _newTime;
	double _frameTime;
	double _accumulator = 0;
	double _frameTimeInSeconds = 0;
	
	private List<IPhysics> _physicsList;

	public PhysicsSystem()
	{
		super();
		_physicsList = new ArrayList<IPhysics>();
		
	}

	void VariedUpdate(float deltaTime)
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
		
		VariedUpdate(deltaTime);
		
		// http://gafferongames.com/game-physics/fix-your-timestep/

		_newTime = TimeUtils.nanoTime();
		_frameTime = _newTime - _oldTime;
		_oldTime = _newTime;

		// my frame rate
		_frameTimeInSeconds = (double) _frameTime / 1000000000.0;

		_accumulator += _frameTimeInSeconds;

		int counter = 0;
		while (_accumulator >= _fixedTimeStep)
		{
			counter++;
			_accumulator -= _fixedTimeStep;
		}

		//System.out.println(counter);
		
		// TODO: use interpolation to make renderer more "stable"
		// interpolate between previous and current Transform state
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
