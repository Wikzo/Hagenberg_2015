package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import net.gustavdahl.engine.MainGameLoopStuff;
import net.gustavdahl.engine.components.Collider;
import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.ConstantForce;
import net.gustavdahl.engine.components.EditorComponent;
import net.gustavdahl.engine.components.IRenderable;
import net.gustavdahl.engine.entities.*;

enum SelectionState
{
	Idle, ReadyToSelect, Selected, MultiSelection
}

enum ActionState
{
	Moving, Rotating, Scaling, Duplicating, Removing
}

public class EditorSystem extends BaseSystem implements InputProcessor
{

	private List<EditorComponent> _editorComponents;
	private Ray _ray;
	private Vector3 _mousePosition;

	// SELECTION STATES
	private SelectionState _selectionState;
	private Entity _currentlySelected;

	// ACTION STATES
	private ActionState _actionState = ActionState.Moving;

	public EditorSystem(OrthographicCamera camera)
	{
		super();

		_editorComponents = new ArrayList<EditorComponent>();
		// _ray = new Ray();
		_mousePosition = new Vector3();

		// Gdx.input.setInputProcessor(this);
		ServiceLocator.AssetManager.AddInputListener(this);

		_selectionState = SelectionState.Idle;
	}

	@Override
	public void Update(float deltaTime)
	{

		if (!_isActive)
			return;

		String stateString = "Selection: ";
		stateString += _selectionState.toString();

		if (_currentlySelected != null)
			stateString += " [" + _currentlySelected.Name + "]";
		DebugSystem.AddDebugText(stateString);

		DebugSystem.AddDebugText("Action: " + _actionState.toString());

		_mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		

		// Ray _ray = _camera.getPickRay(_mousePosition.x, _mousePosition.y);
		Ray _ray = MainGameLoopStuff._camera.getPickRay(_mousePosition.x, _mousePosition.y);

		// System.out.println(_selectionState);

		if (_selectionState == SelectionState.Selected)
		{
			if (_currentlySelected != null)
			{
				if (_actionState == ActionState.Moving)
				{
					MainGameLoopStuff._camera.unproject(_mousePosition);
					
					_currentlySelected.SetPosition(new Vector2(_mousePosition.x, _mousePosition.y));
				}
				else if (_actionState == ActionState.Rotating)
				{
					_currentlySelected.SetRotation(_mousePosition.y / 10f);
				}
				else if (_actionState == ActionState.Scaling)
				{
					// TODO: make mouse movement relative
					
					// mapping from world space to clip space (-1 to 1)
					// new_value = (old_value - old_bottom) / (old_top - old_bottom) * (new_top - new_bottom) + new_bottom
					
					float newY = (_mousePosition.y - 768) / (0 - 768) * (1 - 0) + 0;
					
					Vector2 m = new Vector2(newY*2f, newY*2f);
					_currentlySelected.SetScale(m);
					DebugSystem.AddDebugText(_currentlySelected.GetScale().toString());
				}

			}

		}

		if (_selectionState == SelectionState.ReadyToSelect)
		{
			for (int i = 0; i < _editorComponents.size(); i++)
			{
				Entity e = null;
				if (Collider.Intersect(_ray, _editorComponents.get(i).GetCollider()) != null)
				{
					e = Collider.Intersect(_ray, _editorComponents.get(i).GetCollider()).Owner;

					_currentlySelected = e;
					_selectionState = SelectionState.Selected;
					//System.out.println("hit: " + e.Name);
				}
			}
		}

	}

	private void ClickOnObject()
	{
		// TODO: put above code from Update() into this method instead (but
		// doesn't work??)
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof EditorComponent)
		{
			succesfullyAdded = true;
			_editorComponents.add((EditorComponent) c);

		} else
			throw new RuntimeException("ERROR - " + c.Name() + " is not a EditorComponent!");

		return succesfullyAdded;

	}

	@Override
	public boolean keyDown(int keycode)
	{

		if (keycode == Keys.W)
			_actionState = ActionState.Moving;
		else if (keycode == Keys.E)
			_actionState = ActionState.Rotating;
		else if (keycode == Keys.R)
			_actionState = ActionState.Scaling;

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

	boolean c = false;

	private void SetIdle()
	{
		if (_selectionState == SelectionState.Idle)
			return;

		_selectionState = SelectionState.Idle;

		if (_currentlySelected != null)
			_currentlySelected.GetComponent(Collider.class).SetHitColor(false);

		_currentlySelected = null;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{

		if (_selectionState == SelectionState.Idle)
			_selectionState = SelectionState.ReadyToSelect;

		if (_selectionState == SelectionState.Selected)
			SetIdle();

		return false;
	}

	@Override
	public void SetActive(boolean active)
	{
		super.SetActive(active);

		if (active)
		{
			SetIdle();
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if (_selectionState != SelectionState.Selected)
			SetIdle();
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
