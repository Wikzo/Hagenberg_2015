package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.ui.Table.DebugRect;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.colliders.Collider;
import net.gustavdahl.echelonengine.components.editor.EditorComponent;
import net.gustavdahl.echelonengine.components.visual.IDebugRenderable;
import net.gustavdahl.echelonengine.editoractionstates.EditorActionStateManager;
import net.gustavdahl.echelonengine.editoractionstates.EditorIdleState;
import net.gustavdahl.echelonengine.editoractionstates.EditorRotateActionState;
import net.gustavdahl.echelonengine.editoractionstates.EditorScaleActionState;
import net.gustavdahl.echelonengine.editoractionstates.IEditorActionState;
import net.gustavdahl.echelonengine.editoractionstates.IEditorSelectionState;
import net.gustavdahl.echelonengine.entities.Entity;

enum SelectionModifier
{
	None, Control, Move, Rotate, Scale, Duplicate, Remove
}

public class EditorSystem extends BaseSystem<EditorComponent> implements InputProcessor
{

	private Ray _ray;
	private Vector3 _currentMousePosition;
	Vector3 _previousMousePosition;
	private List<Entity> _selectedEntities;
	private IEditorSelectionState _editorSelectionState;
	public EditorActionStateManager _editorActionStateManager;
	private boolean _controlDown;
	private OrthographicCamera _camera;
	
	private List<Float> _savedXpositions = new ArrayList<>();
	private List<Float> _savedYPositions = new ArrayList<>();
	private List<Float> _savedXScales = new ArrayList<>();
	private List<Float> _savedYScales = new ArrayList<>();
	private List<Float> _savedRotations = new ArrayList<>();

	public EditorSystem(OrthographicCamera camera)
	{
		super();

		_currentMousePosition = new Vector3();
		SavePreviousMousePosition();

		ServiceLocator.AssetManager.AddInputListener(this);

		_selectedEntities = new ArrayList<Entity>();
		
		_editorSelectionState = new EditorIdleState();
		_editorSelectionState.EnterState(this);

		_editorActionStateManager = new EditorActionStateManager();
		
		_camera= camera;
	}

	public String GetEditorState()
	{
		String s = "Current Action State: " + _editorActionStateManager.GetCurrentActionState().getClass().getSimpleName().toString();
		s += "\nCurrent Selection State: " + _editorSelectionState.getClass().getSimpleName().toString();
		
		return s;
	}
	
	public void SetCamera(OrthographicCamera camera)
	{
		_camera = camera;
	}
	
	@Override
	public void Update(float deltaTime)
	{

		if (!_isActive)
			return;
		
		// make mouse raycast
		_currentMousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		_ray = _camera.getPickRay(_currentMousePosition.x, _currentMousePosition.y);
		
		// update action states
		_editorActionStateManager.Update(this);
		
		// set selection color
		for (EditorComponent e : _componentList)
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
		for (int i = 0; i < _componentList.size(); i++)
		{
			Entity e = null;
			if (Collider.MouseIntersectCollider(_ray, _componentList.get(i).GetCollider()) != null)
			{
				e = Collider.MouseIntersectCollider(_ray, _componentList.get(i).GetCollider()).Owner;
				return e;
			}
		}
		return null;
	}

	@Override
	public boolean ValidateIfComponentCanBeAddedToSystem(Component c)
	{
		if (c instanceof EditorComponent)
			return true;
		else
			return false;
	}

	public Vector3 UnprojectVector(Vector3 vector)
	{
		return _camera.unproject(vector);
	}

	public List<Entity> GetSelectedEntities()
	{
		return _selectedEntities;
	}
	
	public Vector2 GetStartPosition(int i)
	{
		Vector2 pos = new Vector2(_savedXpositions.get(i), _savedYPositions.get(i));
		return pos;
	}
	
	public Vector2 GetStartScale(int i)
	{
		Vector2 scale = new Vector2(_savedXScales.get(i), _savedYScales.get(i));
		return scale;
	}
	
	public float GetStartRotation(int i)
	{
		return _savedRotations.get(i);
	}
	
	private void SaveTransformDataBeforeChanging()
	{
		// positions
		_savedXpositions = new ArrayList<>(_selectedEntities.size());
		_savedYPositions = new ArrayList<>(_selectedEntities.size());
		
		// scales
		_savedXScales = new ArrayList<>(_selectedEntities.size());
		_savedYScales = new ArrayList<>(_selectedEntities.size());
		
		// rotations
		_savedRotations = new ArrayList<>(_selectedEntities.size());
		
		for (int i = 0; i < _selectedEntities.size(); i++)
		{
			float posX = _selectedEntities.get(i).GetTransform().PositionX;
			float posY = _selectedEntities.get(i).GetTransform().PositionY;
			float scaleX = _selectedEntities.get(i).GetTransform().ScaleX;
			float scaleY = _selectedEntities.get(i).GetTransform().ScaleY;
			float rotation = _selectedEntities.get(i).GetTransform().Rotation;
			
			_savedXpositions.add(posX);
			_savedYPositions.add(posY);
			_savedXScales.add(scaleX);
			_savedYScales.add(scaleY);
			_savedRotations.add(rotation);
		}
	}

	public Vector3 GetMouseDeltaMovement()
	{
		Vector3 difference = UnprojectVector(_previousMousePosition.cpy()).sub(UnprojectVector(_currentMousePosition.cpy()));
		
		return difference;
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if (keycode == Keys.CONTROL_LEFT)
			_controlDown = true;

		boolean validInput = _editorActionStateManager.HandleInputUpdate(keycode, this);
		
		if (validInput)
			SavePreviousMousePosition();

		return false;
	}

	private void SavePreviousMousePosition()
	{
		_previousMousePosition =  new Vector3(_currentMousePosition.x, _currentMousePosition.y, _currentMousePosition.z);
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
		
		if (temp != null)
			SaveTransformDataBeforeChanging();
		
		if (temp instanceof EditorIdleState)
			_editorActionStateManager.ResetActionState(this);

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
