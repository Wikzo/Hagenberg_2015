package net.gustavdahl.engine.systems;

import java.util.List;

import net.gustavdahl.engine.components.Collider;
import net.gustavdahl.engine.components.EditorComponent;
import net.gustavdahl.engine.entities.Entity;

public class EditorIdleState implements IEditorSelectionState
{

	@Override
	public void EnterState(EditorSystem editor)
	{
		editor.ClearRemoveAllSelectedEntities();
		System.out.println("Entering Idle state");
	}

	@Override
	public IEditorSelectionState HandleInput(EditorSystem editor)
	{
		
		
		Entity hit = editor.ClickSelect();
		
		if (hit != null)
		{
			
			editor.SetSelected(hit, true);
			if (!editor._ctrlButtonDown)
				return new EditorSingleSelectionState();
			else
				return new EditorMultiSelectionState();
			
			/*editor.SetSelectedEntities(hit, editor._ctrlButtonDown);
			
			if (!editor._ctrlButtonDown)
				return new EditorSingleSelectionState();
			else
				return new EditorMultiSelectionState();*/
		}
		
		// idle
		return this;
	}

	@Override
	public void Update(EditorSystem editor)
	{
		//
		
	}

}
