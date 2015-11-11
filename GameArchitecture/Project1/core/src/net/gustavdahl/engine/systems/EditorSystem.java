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

enum SelectionModifier
{
	None, Control, Move, Rotate, Scale, Duplicate, Remove
}

public class EditorSystem extends BaseSystem implements InputProcessor
{

	private List<EditorComponent> _editorComponents;
	private Ray _ray;
	private Vector3 _mousePosition;
	private Entity _currentlySelected;
	private List<Entity> _selectedEntities;
	private IEditorSelectionState _selection;
	private SelectionModifier _selectionModifier = SelectionModifier.Move;

	public EditorSystem(OrthographicCamera camera)
	{
		super();

		_editorComponents = new ArrayList<EditorComponent>();
		_mousePosition = new Vector3();

		ServiceLocator.AssetManager.AddInputListener(this);

		_selectedEntities = new ArrayList<Entity>();
		_selection = new EditorIdleState();
		_selection.EnterState(this);
	}

	@Override
	public void Update(float deltaTime)
	{

		if (!_isActive)
			return;

		/*
		 * String stateString = "Selection: "; stateString +=
		 * _selectionState.toString();
		 * 
		 * if (_currentlySelected != null) stateString += " [" +
		 * _currentlySelected.Name + "]"; DebugSystem.AddDebugText(stateString);
		 * 
		 * DebugSystem.AddDebugText("Action: " + _actionState.toString());
		 */

		// make mouse raycast
		_mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		_ray = MainGameLoopStuff._camera.getPickRay(_mousePosition.x, _mousePosition.y);

		_selection.Update(this, this._selectionModifier);

		// set selection color
		for (EditorComponent e : _editorComponents)
			e.Owner.GetComponent(Collider.class).SetHitColor(e.Owner.CurrentlySelectedByEditor);

		DebugSystem.AddDebugText("State: " + _selection.getClass().getSimpleName());
		DebugSystem.AddDebugText("List size: " + _selectedEntities.size());
		DebugSystem.AddDebugText("Selection modifier: " + _selectionModifier);

	}

	public void ClearRemoveAllSelectedEntities()
	{
		// System.out.println("clearing all");

		for (Entity e : _selectedEntities)
			e.CurrentlySelectedByEditor = false;

		_selectedEntities.clear();
	}

	public void SetSelected(Entity entity, boolean add)
	{
		if (!add) // removing from list
		{
			entity.CurrentlySelectedByEditor = false;
			_selectedEntities.remove(entity);
		} else if (add) // adding to list
		{
			entity.CurrentlySelectedByEditor = true;
			_selectedEntities.add(entity);
		}

	}

	protected Entity EntityRaycast()
	{

		for (int i = 0; i < _editorComponents.size(); i++)
		{
			Entity e = null;
			if (Collider.MouseIntersectCollider(_ray, _editorComponents.get(i).GetCollider()) != null)
			{
				e = Collider.MouseIntersectCollider(_ray, _editorComponents.get(i).GetCollider()).Owner;
				return e;
			}
		}

		return null;
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
		if (keycode == Keys.Q)
			_selectionModifier = SelectionModifier.None;
		else if (keycode == Keys.W)
			_selectionModifier = SelectionModifier.Move;
		else if (keycode == Keys.E)
			_selectionModifier = SelectionModifier.Rotate;
		else if (keycode == Keys.R)
			_selectionModifier = SelectionModifier.Scale;
		else if (keycode == Keys.CONTROL_LEFT)
			_selectionModifier = SelectionModifier.Control;
		else if (keycode == Keys.X)
			_selectionModifier = SelectionModifier.Remove;
		else if (keycode == Keys.D)
			_selectionModifier = SelectionModifier.Duplicate;

		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		if (keycode == Keys.CONTROL_LEFT && _selectionModifier == SelectionModifier.Control)
			_selectionModifier = SelectionModifier.Move;

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

		IEditorSelectionState temp = _selection.HandleInput(this, this._selectionModifier);

		if (temp != _selection)
		{
			_selection = null;
			_selection = temp;

			_selection.EnterState(this);
		}

		return false;
	}

	@Override
	public void SetActive(boolean active)
	{
		super.SetActive(active);

		if (!active)
		{
			_selection = new EditorIdleState();
			_selection.EnterState(this);
		}
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
