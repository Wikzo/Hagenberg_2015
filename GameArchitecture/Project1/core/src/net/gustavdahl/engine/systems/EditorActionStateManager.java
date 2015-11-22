package net.gustavdahl.engine.systems;

import com.badlogic.gdx.Input.Keys;

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

	public String GetCurrentActionState()
	{
		return _actionState.getClass().getSimpleName();
	}

	public void HandleInputUpdate(int input)
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
		else
			temp = new EditorIdleActionState();

		if (!_actionState.getClass().equals(temp.getClass())) // check if the same state is already active
		{
			if (temp != null && temp != _actionState) // assign new state
			{
				_actionState = null;
				_actionState = temp;

				_actionState.EnterState();
			}
		}

	}

}
