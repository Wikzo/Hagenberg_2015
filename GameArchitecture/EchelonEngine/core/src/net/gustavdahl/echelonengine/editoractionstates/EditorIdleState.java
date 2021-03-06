package net.gustavdahl.echelonengine.editoractionstates;

import net.gustavdahl.echelonengine.entities.Entity;
import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorIdleState implements IEditorSelectionState
{

	@Override
	public void EnterState(EditorSystem editor)
	{
		editor.ClearRemoveAllSelectedEntities();
		//System.out.println("Entering Idle state");
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
