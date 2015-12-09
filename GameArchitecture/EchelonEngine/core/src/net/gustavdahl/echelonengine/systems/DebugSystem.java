package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.sun.xml.internal.ws.dump.LoggingDumpTube.Position;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.DebugComponent;
import net.gustavdahl.echelonengine.components.IDebugRenderable;
import net.gustavdahl.echelonengine.components.IRenderable;
import net.gustavdahl.echelonengine.components.PhysicsBody;

public class DebugSystem extends BaseSystem
{
	protected List<IDebugRenderable> _debugRenderList;

	private SpriteBatch _spriteBatch;
	private ShapeRenderer _shapeRenderer;
	private BitmapFont _font;
	private OrthographicCamera _camera;

	private static Map<Vector2, String> _debugDrawingPosition = new HashMap<Vector2, String>();

	public DebugSystem(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, BitmapFont font, OrthographicCamera cam)
	{
		_spriteBatch = spriteBatch;
		_shapeRenderer = shapeRenderer;
		_font = font;

		_debugRenderList = new ArrayList<IDebugRenderable>();

		_isActive = false;

		_camera = cam;
	}

	public void GetCamera(OrthographicCamera camera)
	{
		_camera = camera;
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

		// debug shapes
		for (int i = 0; i < _debugRenderList.size(); i++)
		{
			if (!((Component) _debugRenderList.get(i)).IsActive())
				continue;

			_debugRenderList.get(i).DebugRender(_spriteBatch, _shapeRenderer, deltaTime);
		}

		// debug text
		_spriteBatch.begin();
		_spriteBatch.setProjectionMatrix(_camera.combined);

		_font.draw(_spriteBatch, "DEBUG MENU", Gdx.graphics.getWidth() / 4, 450);
		// _font.draw(_spriteBatch, _debugText,400, 400);

		if (!_debugDrawingPosition.isEmpty())
		{
			for (Vector2 v : _debugDrawingPosition.keySet())
			{
				// hash map:
				// https://stackoverflow.com/questions/12960265/retrieve-all-values-from-hashmap-keys-in-an-arraylist-java

				// System.out.println("Key: " + v.x); // v = position to draw
				// System.out.println("Value: " + _debugDrawingPosition.get(v));
				// // value = string to draw

				_font.draw(_spriteBatch, _debugDrawingPosition.get(v), v.x, v.y);
			}
			_debugDrawingPosition.clear();
			_textPosition = 0;
		}

		_spriteBatch.end();

	}

	// TODO: make input field that can change variables dynamically (like Unity & TuneFish)
	
	private static int _textPosition = 0;

	public static void AddDebugText(String text, Vector2 position)
	{
		// TODO: dont take in a Vector2
		_debugDrawingPosition.put(position, text);
	}

	public static void AddDebugText(String text)
	{
		AddDebugText(text, new Vector2(30, 400 + _textPosition++ * 14));
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
			throw new RuntimeException(
					"ERROR - component " + c.getClass().getSimpleName() + " doesn't isn't a DebugComponent!");

		return succesfullyAdded;
	}

}
