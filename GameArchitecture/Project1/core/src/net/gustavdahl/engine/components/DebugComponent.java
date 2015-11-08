package net.gustavdahl.engine.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.systems.DebugSystem;
import net.gustavdahl.engine.systems.PhysicsSystem;

public class DebugComponent extends Component implements IDebugRenderable, InputProcessor
{

	private BitmapFont _font;
	private String _debugText = "";
	private ShapeRenderer _shapeRenderer;
	private boolean _projectionMatrixSet;

	private boolean _debugMenuEnabled;
	private boolean _lookingForInputAdd;
	private boolean _lookingForInputRemove;
	private String _commands = "[a] = add; [r] = remove";
	private List<Component> _componentsThatCanBeRemoved;

	boolean Position, Name;

	public DebugComponent(BitmapFont f)
	{
		super();
		_font = f;

		DefaultSystem = DebugSystem.SystemName;

		_shapeRenderer = new ShapeRenderer();
		_projectionMatrixSet = false;

		_componentsThatCanBeRemoved = new ArrayList<Component>();

		Gdx.input.setInputProcessor(this);
	}

	// TODO: pass parameters (e.g. position) by reference instead of values

	public DebugComponent SetRenderPosition(boolean b)
	{
		Position = b;
		return this;
	}

	public DebugComponent SetRenderName(boolean b)
	{
		Name = b;
		return this;
	}

	private void CheckInput()
	{
		if (Gdx.input.isKeyJustPressed(Keys.C))
		{
			Owner.AddComponent(new ConstantForce(Vector2.Zero, 2f), PhysicsSystem.SystemName);
			System.out.println("ConstantForce added");

		}
	}

	@Override
	public void DebugRender(SpriteBatch spriteBatch, float deltaTime)
	{
		CheckInput();

		if (_debugMenuEnabled)
		{
			// TODO: render text above rendershape

			/*
			 * Gdx.gl.glEnable(GL30.GL_BLEND); if (!_projectionMatrixSet) {
			 * _shapeRenderer.setProjectionMatrix(spriteBatch.
			 * getProjectionMatrix()); _projectionMatrixSet = true; }
			 * 
			 * _shapeRenderer.begin(ShapeType.Filled);
			 * _shapeRenderer.setColor(new Color(0f,0f,0f,0.5f));
			 * _shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(),
			 * Gdx.graphics.getHeight()); _shapeRenderer.end();
			 */

			_font.draw(spriteBatch, _commands, 30, 450);
			if (_lookingForInputAdd)
			{
				_font.draw(spriteBatch, "Add ...\n[c] ConstantForce", 30, 420);
			} else if (_lookingForInputRemove)
			{
				_font.draw(spriteBatch, "Remove ...", 30, 420);

				String remove = "";

				for (int i = 0; i < _componentsThatCanBeRemoved.size(); i++)
				{
					remove += "[" + i + "] " + _componentsThatCanBeRemoved.get(i).toString() + " ";
				}

				_font.draw(spriteBatch, remove, 30, 400);
			}

			String s = "";
			if (Name)
				s += Owner.Name + "\n";

			if (Position)
				s += Owner.GetTransform().Position.toString() + "\n";

			_font.draw(spriteBatch, s, 30, 50);

		}

	}

	@Override
	public boolean keyDown(int keycode)
	{
		if (keycode == Keys.F1)
			_debugMenuEnabled = !_debugMenuEnabled;

		if (_debugMenuEnabled)
		{
			if (keycode == Keys.A)
				_lookingForInputAdd = true;
			else if (keycode == Keys.R)
			{
				_lookingForInputRemove = true;
				_componentsThatCanBeRemoved = (List<Component>) Owner.GetAllComponents();
			}
		}

		if (_lookingForInputAdd)
		{
			switch (keycode)
			{
			case Keys.C:
				Owner.AddComponent(new ConstantForce(Vector2.Zero, 2f), PhysicsSystem.SystemName);
				_lookingForInputAdd = false;
				_lookingForInputRemove = false;
				break;
			}
		}

		if (keycode == Keys.ESCAPE)
		{
			_lookingForInputAdd = false;
			_lookingForInputRemove = false;
			//_debugMenuEnabled = false;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{

		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{

		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{

		return false;
	}

}
