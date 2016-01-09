package net.gustavdahl.echelonengine.components;

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

import net.gustavdahl.echelonengine.components.physics.ConstantForce;
import net.gustavdahl.echelonengine.components.visual.IDebugRenderable;
import net.gustavdahl.echelonengine.systems.DebugSystem;
import net.gustavdahl.echelonengine.systems.MyAssetManager;
import net.gustavdahl.echelonengine.systems.PhysicsSystem;
import net.gustavdahl.echelonengine.systems.ServiceLocator;

public class DebugComponent extends Component implements IDebugRenderable, InputProcessor
{

	private BitmapFont _font;
	private String _debugText = ""; // old for "menu stuff"
	private boolean _projectionMatrixSet;
	private static String _generalDebugText = "";

	private boolean _lookingForInputAdd;
	private boolean _lookingForInputRemove;
	private String _commands = "[a] = add; [r] = remove";
	private List<Component> _componentsThatCanBeRemoved;

	boolean Position, Name;

	public DebugComponent(BitmapFont f)
	{
		super();
		_font = f;

		DefaultSystem = DebugSystem.class;

		_projectionMatrixSet = false;

		_componentsThatCanBeRemoved = new ArrayList<Component>();

		ServiceLocator.AssetManager.AddInputListener(this);
		//Gdx.input.setInputProcessor(this);
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
			Owner.AddComponent(new ConstantForce(Vector2.Zero, 2f), PhysicsSystem.class);
			System.out.println("ConstantForce added");

		}
	}

	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{
	}
	
	
	// TODO: component adding stuff should be in own class/component

	@Override
	public boolean keyDown(int keycode)
	{

		if (keycode == Keys.A)
			_lookingForInputAdd = true;
		else if (keycode == Keys.R)
		{
			_lookingForInputRemove = true;
			_componentsThatCanBeRemoved = (List<Component>) Owner.GetAllComponents();
		}

		if (_lookingForInputAdd)
		{
			switch (keycode)
			{
			case Keys.C:
				Owner.AddComponent(new ConstantForce(Vector2.Zero, 2f), PhysicsSystem.class);
				_lookingForInputAdd = false;
				_lookingForInputRemove = false;
				break;
			}
		}

		if (keycode == Keys.ESCAPE)
		{
			_lookingForInputAdd = false;
			_lookingForInputRemove = false;
			// _debugMenuEnabled = false;
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

	@Override
	public void Update(float deltaTime)
	{
		
	}

	@Override
	public String OnSelectedText()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
