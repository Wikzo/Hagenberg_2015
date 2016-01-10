package net.gustavdahl.echelonengine.systems;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.visual.IDebugRenderable;

public class DebugSystem extends BaseSystem<IDebugRenderable>
{
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

		_isActive = false;

		_camera = cam;
	}

	public void GetCamera(OrthographicCamera camera)
	{
		_camera = camera;
	}

	ShapeRenderer shape = new ShapeRenderer();
	
	@Override
	public void Update(float deltaTime)
	{
		// TODO: show general GUI to enable/disable systems and other things...		
		if (Gdx.input.isKeyJustPressed(Keys.F1))
			_isActive = !_isActive;

		if (!_isActive)
			return;

		if (_componentList.size() < 1)
		{
			//System.out.println("ERROR - no render components in DebugSystem!");
			return;
		}

		// debug shapes
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (!((Component) _componentList.get(i)).IsActive())
				continue;

			_componentList.get(i).DebugRender(_spriteBatch, _shapeRenderer, deltaTime);
		}
		
		
		
		// debug text
		_spriteBatch.begin();
		_spriteBatch.setProjectionMatrix(_camera.combined);

		_font.draw(_spriteBatch, "DEBUG MENU\n(F1 to toggle)", Gdx.graphics.getWidth() / 2, 450);
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

	public Vector3 GetPositionInWorldSpace(Vector3 vec)
	{
		 return _camera.unproject(vec);
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
		AddDebugText(text, new Vector2(30, 460 - _textPosition++ * 14));
	}

	@Override
	public boolean ValidateIfComponentCanBeAddedToSystem(Component c)
	{
		if (c instanceof IDebugRenderable)
			return true;
		else
			return false;
		
	}

}
