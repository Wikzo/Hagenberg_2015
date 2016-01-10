package net.gustavdahl.echelonengine.editoractionstates;

import com.badlogic.gdx.Input.Keys;
import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorActionStateManager
{

	IEditorActionState _actionState;

	public EditorActionStateManager()
	{
		_actionState = new EditorIdleActionState();
	}

	public void Update(EditorSystem editor)
	{
		_actionState.Update(editor);
	}

	public IEditorActionState GetCurrentActionState()
	{
		return _actionState;
	}

	public void ResetActionState(EditorSystem editor)
	{
		//System.out.println("reseting");
		HandleInputUpdate(-1, editor);
	}
	
	public boolean HandleInputUpdate(int input, EditorSystem editor)
	{

		IEditorActionState temp = null;

		if (input == Keys.W)
			temp = new EditorMoveActionState();
		else if (input == Keys.E)
			temp = new EditorRotateActionState();
		else if (input == Keys.R)
			temp = new EditorScaleActionState();
		else if (input == Keys.X)
			temp = new EditorDeleteActionState();
		else if (input == Keys.D)
			temp = new EditorDuplicateActionState();
		else if (input == Keys.Z && _actionState != null)
		{
			_actionState.Reset(editor);
			ResetActionState(editor);
		}
		else
			temp = new EditorIdleActionState();

		if (temp != null) // check if temp is null
		{
			if (!_actionState.getClass().equals(temp.getClass())) // check if the same state is already active
			{
				if (temp != null && temp != _actionState) // assign new state
				{
					_actionState = null;
					_actionState = temp;
	
					_actionState.EnterState();
				}
			}
			
			return true;
		}
		else
			return false;

	}

}