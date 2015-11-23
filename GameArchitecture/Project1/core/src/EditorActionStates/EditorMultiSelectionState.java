package EditorActionStates;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.EditorSystem;

public class EditorMultiSelectionState implements IEditorSelectionState
{

	public EditorMultiSelectionState()
	{
	}

	@Override
	public IEditorSelectionState HandleInput(EditorSystem editor, boolean controlDown)
	{

		Entity hit = editor.EntityRaycast();

		if (hit == null) // select none
		{
			editor.ClearRemoveAllSelectedEntities();
			return new EditorIdleState();
		} else
		{
			if (controlDown) // select more
			{
				boolean alreadySelected = hit.CurrentlySelectedByEditor;
				editor.SetSelected(hit, !alreadySelected);
				return this;
			} else if (!controlDown) // select one																								// one
			{
				editor.ClearRemoveAllSelectedEntities();
				editor.SetSelected(hit, true);
				return new EditorSingleSelectionState();
			}
		}

		return this;
	}

	@Override
	public void EnterState(EditorSystem editor)
	{
		System.out.println("Entering multi selection state");

	}

}
