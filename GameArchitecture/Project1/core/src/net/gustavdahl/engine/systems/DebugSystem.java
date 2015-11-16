package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.compression.lzma.Base;

import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.DebugComponent;
import net.gustavdahl.engine.components.IDebugRenderable;
import net.gustavdahl.engine.components.IRenderable;
import net.gustavdahl.engine.components.PhysicsComponent;

public class DebugSystem extends BaseSystem
{
	protected List<IDebugRenderable> _debugRenderList;

	private SpriteBatch _spriteBatch;
	private ShapeRenderer _shapeRenderer;
	private BitmapFont _font;
	private static String _debugText = "";

	public DebugSystem(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, BitmapFont font)
	{
		_spriteBatch = spriteBatch;
		_shapeRenderer = shapeRenderer;
		_font = font;
		
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

		// debug text
		_spriteBatch.begin();	
		_font.draw(_spriteBatch, "DEBUG MENU", Gdx.graphics.getWidth() / 4, 450);
		_font.draw(_spriteBatch, _debugText, 30, 400);
		_debugText = "";
		_spriteBatch.end();
		
		// debug shapes
		for (int i = 0; i < _debugRenderList.size(); i++)
		{
			if (!((Component) _debugRenderList.get(i)).IsActive())
				continue;
			
			_debugRenderList.get(i).DebugRender(_spriteBatch, _shapeRenderer, deltaTime);
		}

	}
	
	public static void AddDebugText(String text)
	{
		_debugText += text + "\n";
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof IDebugRenderable)
		{
			succesfullyAdded = true;
			_debugRenderList.add((IDebugRenderable) c);
			System.out.println(c.Name() + " was added to DebugSystem");

		} else
			throw new RuntimeException(
					"ERROR - component " + c.getClass().getSimpleName() + " doesn't isn't a DebugComponent!");

		return succesfullyAdded;
	}

}
