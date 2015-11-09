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
	Idle, ReadyToSelect, Selected
}

public class EditorSystem extends BaseSystem implements InputProcessor
{

	private List<EditorComponent> _editorComponents;
	private Ray _ray;
	private Vector3 _mousePosition;
	private SelectionState _selectionState;
	private Entity _currentlySelected;

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

		_mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);

		// Ray _ray = _camera.getPickRay(_mousePosition.x, _mousePosition.y);
		Ray _ray = MainGameLoopStuff._camera.getPickRay(_mousePosition.x, _mousePosition.y);

		 System.out.println(_selectionState);
		 
		 if (_selectionState == SelectionState.Selected)
		 {
			 if (_currentlySelected != null)
			 {
				 MainGameLoopStuff._camera.unproject(_mousePosition);
				 
				 _currentlySelected.SetTransform(new Vector2(_mousePosition.x, _mousePosition.y));
				 
				 //_currentlySelected.AddComponent(new ConstantForce(new Vector2(5,0), 0f));
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
					System.out.println("hit: " + e.Name);
				}
			}
		}

	}

	private void ClickOnObject()
	{
		for (int i = 0; i < _editorComponents.size(); i++)
		{
			Entity e = null;
			if (Collider.Intersect(_ray, _editorComponents.get(i).GetCollider()) != null)
			{
				e = Collider.Intersect(_ray, _editorComponents.get(i).GetCollider()).Owner;
				System.out.println("hit: " + e.Name);
			}
			// if (hit)
			{
				// _currentlySelected = _editorComponents.get(i).Owner;
				// _selectionState = SelectionState.Selected;
			}
		}
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

		if (keycode == Keys.CONTROL_LEFT)
			_selectionState = SelectionState.ReadyToSelect;

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
		_selectionState = SelectionState.Idle;
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
