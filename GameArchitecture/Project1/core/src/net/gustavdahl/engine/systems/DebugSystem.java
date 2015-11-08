package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.DebugComponent;
import net.gustavdahl.engine.components.IDebugRenderable;
import net.gustavdahl.engine.components.IRenderable;
import net.gustavdahl.engine.components.PhysicsComponent;

public class DebugSystem extends RenderSystem
{
	public static final String SystemName = DebugSystem.class.getSimpleName();

	protected List<IDebugRenderable> _debugRenderList;

	private ShapeRenderer _shapeRenderer;

	public DebugSystem(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
	{
		super(spriteBatch);

		_shapeRenderer = shapeRenderer;

		_debugRenderList = new ArrayList<IDebugRenderable>();

		_isActive = false;
	}

	@Override
	public void Update(float deltaTime)
	{

		// TODO: show general GUI to enable/disable systems and other things...
		
		if (Gdx.input.isKeyJustPressed(Keys.F1))
			_isActive = !_isActive;

		if (!_isActive)
			return;

		if (_debugRenderList.size() < 1)
		{
			System.out.println("ERROR - no render components in DebugSystem!");
			return;
		}

		for (int i = 0; i < _debugRenderList.size(); i++)
		{
			if (!((Component) _debugRenderList.get(i)).IsActive())
				continue;

			_spriteBatch.begin();
			_debugRenderList.get(i).DebugRender(_spriteBatch, _shapeRenderer, deltaTime);
			_spriteBatch.end();
		}

	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof IDebugRenderable)
		{
			succesfullyAdded = true;
			_debugRenderList.add((IDebugRenderable) c);

		} else
		{
			try
			{
				throw new Exception(
						"ERROR - component " + c.getClass().getSimpleName() + " doesn't isn't a DebugComponent!");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return succesfullyAdded;
	}

}
