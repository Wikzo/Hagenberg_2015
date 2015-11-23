package EditorActionStates;

import java.util.List;

import com.badlogic.gdx.Input.Keys;

import net.gustavdahl.engine.components.Collider;
import net.gustavdahl.engine.components.EditorComponent;
import net.gustavdahl.engine.entities.Entity;
import net.gustavdahl.engine.systems.EditorSystem;

public class EditorIdleState implements IEditorSelectionState
{

	@Override
	public void EnterState(EditorSystem editor)
	{
		editor.ClearRemoveAllSelectedEntities();
		System.out.println("Entering Idle state");
	}

	@Override
	public IEditorSelectionState HandleInput(EditorSystem editor, boolean controlDown)
	{

		Entity hit = editor.EntityRaycast();

		if (hit != null)
		{

			editor.SetSelected(hit, true);
			if (!controlDown)
				return new EditorSingleSelectionState();
			else
				return new EditorMultiSelectionState();
		}

		// idle
		return this;
	}
}
