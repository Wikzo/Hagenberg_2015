package net.gustavdahl.engine.systems;

import net.gustavdahl.engine.entities.Entity;

public class EditorSingleSelectionState implements IEditorSelectionState
{

	@Override
	public IEditorSelectionState HandleInput(EditorSystem editor)
	{

		Entity hit = editor.ClickSelect();
		
		if (hit == null)
		{
			editor.ClearRemoveAllSelectedEntities();
			return new EditorIdleState();
		}
		else
		{
			//System.out.println("Currently selected: " + editor.GetSelected(hit));
			
			if (!editor._ctrlButtonDown)
			{
				boolean alreadySelected = editor.GetSelected(hit);
				editor.ClearRemoveAllSelectedEntities();
				editor.SetSelected(hit, !alreadySelected);
				
				
				if (!editor.GetSelected(hit))
					return new EditorIdleState();
				else
					return this;
			}
			else if (editor._ctrlButtonDown)
			{
				boolean alreadySelected = editor.GetSelected(hit);
				editor.SetSelected(hit, !alreadySelected);
				

					return new EditorMultiSelectionState();

			}
		}
		
		/*Entity hit = editor.ClickSelect();

		if (hit == null) // no selection
		{
			editor.SetSelectedEntities(null, false);

			return new EditorIdleState();
		}

		if (hit != null)
		{
			if (!editor._ctrlButtonDown) // single selection
			{
				//editor.ClearRemoveAllSelectedEntities();
				
				if (editor.SetSelectedEntities(hit, false))
					return new EditorIdleState();
				else
					return this;
			} else if (editor._ctrlButtonDown) // multi selection
			{
				editor.SetSelectedEntities(hit, true);
				return new EditorMultiSelectionState();
			}
		}*/

		return this;

	}

	@Override
	public void Update(EditorSystem editor)
	{
		// System.out.println("Single selection state");

		/*
		 * for (Entity e : editor._selectedEntities) {
		 * System.out.println(e.Name); }
		 */

	}

	@Override
	public void EnterState(EditorSystem editor)
	{
		System.out.println("Entering single selection state");

	}

}
