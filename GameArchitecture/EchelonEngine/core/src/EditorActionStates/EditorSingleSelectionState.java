package EditorActionStates;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorSingleSelectionState implements IEditorSelectionState
{

	@Override
	public IEditorSelectionState HandleInput(EditorSystem editor, boolean controlDown)
	{

		Entity hit = editor.EntityRaycast();

		if (hit == null)
		{
			editor.ClearRemoveAllSelectedEntities();
			return new EditorIdleState();
		} else
		{
			if (!controlDown) // selecting without ctrl
			{
				boolean alreadySelected = hit.CurrentlySelectedByEditor;
				editor.ClearRemoveAllSelectedEntities();
				editor.SetSelected(hit, !alreadySelected);

				if (!hit.CurrentlySelectedByEditor) // deselect current one
					return new EditorIdleState();
				else
					return this; // select new one
			} else if (controlDown) // multi selection
			{
				boolean alreadySelected = hit.CurrentlySelectedByEditor;
				editor.SetSelected(hit, !alreadySelected);

				return new EditorMultiSelectionState();

			}
		}

		return this;

	}

	@Override
	public void EnterState(EditorSystem editor)
	{
		System.out.println("Entering single selection state");

	}

}
