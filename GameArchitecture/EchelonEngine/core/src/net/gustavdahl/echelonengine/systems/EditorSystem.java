package net.gustavdahl.echelonengine.systems;

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

import net.gustavdahl.echelonengine.scenes.TestLevel;
import net.gustavdahl.echelonengine.components.Collider;
import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.ConstantForce;
import net.gustavdahl.echelonengine.components.EditorComponent;
import net.gustavdahl.echelonengine.components.IDebugRenderable;
import net.gustavdahl.echelonengine.components.IRenderable;
import net.gustavdahl.echelonengine.editoractionstates.EditorActionStateManager;
import net.gustavdahl.echelonengine.editoractionstates.EditorIdleState;
import net.gustavdahl.echelonengine.editoractionstates.EditorRotateActionState;
import net.gustavdahl.echelonengine.editoractionstates.EditorScaleActionState;
import net.gustavdahl.echelonengine.editoractionstates.IEditorActionState;
import net.gustavdahl.echelonengine.editoractionstates.IEditorSelectionState;
import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.entities.*;

enum SelectionModifier
{
	None, Control, Move, Rotate, Scale, Duplicate, Remove
}

public class EditorSystem extends BaseSystem implements InputProcessor
{

	private List<EditorComponent> _editorComponents;
	private Ray _ray;
	private Vector3 _mousePosition;
	private List<Entity> _selectedEntities;
	private IEditorSelectionState _editorSelectionState;
	private EditorActionStateManager _editorActionStateManager;
	private boolean _controlDown;

	public EditorSystem(OrthographicCamera camera)
	{
		super();

		_editorComponents = new ArrayList<EditorComponent>();
		_mousePosition = new Vector3();

		ServiceLocator.AssetManager.AddInputListener(this);

		_selectedEntities = new ArrayList<Entity>();
		
		_editorSelectionState = new EditorIdleState();
		_editorSelectionState.EnterState(this);

		_editorActionStateManager = new EditorActionStateManager();
	}

	@Override
	public void Update(float deltaTime)
	{

		if (!_isActive)
			return;

		// make mouse raycast
		_mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		_ray = TestLevel._camera.getPickRay(GetMousePosition().x, GetMousePosition().y);

		// update action states
		_editorActionStateManager.Update(this);

		// set selection color
		for (EditorComponent e : _editorComponents)
		{
			e.Owner.GetComponent(Collider.class).SetHitColor(e.Owner.CurrentlySelectedByEditor);
			
			IEditorActionState currentActionState = _editorActionStateManager.GetCurrentActionState();
			
			String data = "Position: " + e.Owner.GetPositionString();
			
			if (currentActionState instanceof EditorRotateActionState)
				data = "Rotation: " + Float.toString(e.Owner.GetTransform().Rotation);
			else if (currentActionState instanceof EditorScaleActionState)
				data = "Scale: " + e.Owner.GetScaleString();
					
			if (e.Owner.CurrentlySelectedByEditor)
			{
				String debugTextToShow = "\n" + e.Owner.Name + ":\n" + data;

				// look for all possible debug text in the components
				for (Component c : e.Owner.GetAllComponents())
					if (c instanceof IDebugRenderable)
					{
						String componentDebugText = ((IDebugRenderable) c).OnSelectedText();
						if (componentDebugText != null && !componentDebugText.equals("null"))
							debugTextToShow += "\n---\n" + componentDebugText + "\n---\n";
					}
						
				
				DebugSystem.AddDebugText(debugTextToShow, new Vector2(e.Owner.GetPositionX(), e.Owner.GetPositionY()));
			}
		}

	}

	public void ClearRemoveAllSelectedEntities()
	{
		for (Entity e : GetSelectedEntities())
			e.CurrentlySelectedByEditor = false;

		GetSelectedEntities().clear();
	}

	public void SetSelected(Entity entity, boolean add)
	{
		if (!add) // removing from list
		{
			entity.CurrentlySelectedByEditor = false;
			GetSelectedEntities().remove(entity);
		} else if (add) // adding to list
		{
			entity.CurrentlySelectedByEditor = true;
			GetSelectedEntities().add(entity);
		}

	}

	public Entity EntityRaycast()
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

	public void UnprojectMouse()
	{
		TestLevel._camera.unproject(GetMousePosition());
	}

	public List<Entity> GetSelectedEntities()
	{
		return _selectedEntities;
	}


	public Vector3 GetMousePosition()
	{
		return _mousePosition;
	}

	@Override
	public boolean keyDown(int keycode)
	{

		if (keycode == Keys.CONTROL_LEFT)
			_controlDown = true;

		_editorActionStateManager.HandleInputUpdate(keycode);

		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{

		if (keycode == Keys.CONTROL_LEFT)
			_controlDown = false;

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

		IEditorSelectionState temp = _editorSelectionState.HandleInput(this, _controlDown);

		if (temp != _editorSelectionState)
		{
			_editorSelectionState = null;
			_editorSelectionState = temp;

			_editorSelectionState.EnterState(this);
		}

		return false;
	}

	@Override
	public void SetActive(boolean active)
	{
		super.SetActive(active);

		if (!active)
		{
			_editorSelectionState = new EditorIdleState();
			_editorSelectionState.EnterState(this);

			_editorActionStateManager = new EditorActionStateManager();
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
