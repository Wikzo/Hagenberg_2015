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

	protected List<EditorComponent> _editorComponents;
	protected Ray _ray;
	protected Vector3 _mousePosition;

	// SELECTION STATES
	protected SelectionState _selectionState;
	protected Entity _currentlySelected;

	protected boolean _ctrlButtonDown;
	protected boolean _mouseDown;
	protected List<Entity> _selectedEntities;
	protected IEditorSelectionState _selection;

	// ACTION STATES
	protected ActionState _actionState = ActionState.Moving;

	public EditorSystem(OrthographicCamera camera)
	{
		super();

		_editorComponents = new ArrayList<EditorComponent>();
		// _ray = new Ray();
		_mousePosition = new Vector3();

		// Gdx.input.setInputProcessor(this);
		ServiceLocator.AssetManager.AddInputListener(this);

		_selectionState = SelectionState.Idle;

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

		_mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);

		// Ray _ray = _camera.getPickRay(_mousePosition.x, _mousePosition.y);
		_ray = MainGameLoopStuff._camera.getPickRay(_mousePosition.x, _mousePosition.y);

		_selection.Update(this);

		for (EditorComponent e : _editorComponents)
			e.Owner.GetComponent(Collider.class).SetHitColor(e.Owner.CurrentlySelectedByEditor);
		
		DebugSystem.AddDebugText("State: " + _selection.getClass().getSimpleName());
		DebugSystem.AddDebugText("List size: " + _selectedEntities.size());

	}

	public void ClearRemoveAllSelectedEntities()
	{
		

		System.out.println("clearing all");

		for (Entity e : _selectedEntities)
			e.CurrentlySelectedByEditor = false;

		_selectedEntities.clear();
	}

	public boolean GetSelected(Entity entity)
	{
		return entity.CurrentlySelectedByEditor;
	}
	
	public void SetSelected(Entity entity, boolean add)
	{
		if (!add) // removing from list
		{
			System.out.println(entity.Name + " already selected!");
			entity.CurrentlySelectedByEditor = false;
			_selectedEntities.remove(entity);
		}
		else if (add) // adding to list
		{
			System.out.println(entity.Name + " NOT already selected");
			entity.CurrentlySelectedByEditor = true;
			_selectedEntities.add(entity);
		}
		
	}
	
	
	public boolean SetSelectedEntities(Entity entity, boolean multiSelection)
	{

		boolean alreadySelected = false;


		
		if (entity == null)
		{
			ClearRemoveAllSelectedEntities();
		}

		else if (!entity.CurrentlySelectedByEditor)
		{
			if (!multiSelection)
				ClearRemoveAllSelectedEntities();

			entity.CurrentlySelectedByEditor = true;
			_selectedEntities.add(entity);
			
			System.out.println("currently selected: FALSE");
		} else if (entity.CurrentlySelectedByEditor)
		{
			if (!multiSelection)
				ClearRemoveAllSelectedEntities();

			entity.CurrentlySelectedByEditor = false;
			_selectedEntities.remove(entity);

			alreadySelected = true;
			
			System.out.println("currently selected: TRUE");
		}

		return alreadySelected;

	}

	protected Entity ClickSelect()
	{
		// List<Entity> hitEntities = new ArrayList<Entity>();

		for (int i = 0; i < _editorComponents.size(); i++)
		{
			Entity e = null;
			if (Collider.MouseIntersectCollider(_ray, _editorComponents.get(i).GetCollider()) != null)
			{
				e = Collider.MouseIntersectCollider(_ray, _editorComponents.get(i).GetCollider()).Owner;
				//System.out.println("Hit stuff");
				return e;
				// hitEntities.add(e);
			}
		}

		//System.out.println("Did NOT hit stuff");
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

		if (keycode == Keys.W)
			_actionState = ActionState.Moving;
		else if (keycode == Keys.E)
			_actionState = ActionState.Rotating;
		else if (keycode == Keys.R)
			_actionState = ActionState.Scaling;

		if (keycode == Keys.CONTROL_LEFT)
			_ctrlButtonDown = true;

		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		if (keycode == Keys.CONTROL_LEFT)
			_ctrlButtonDown = false;

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

		_mouseDown = true;

		IEditorSelectionState temp = _selection.HandleInput(this);

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

		_mouseDown = false;

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
