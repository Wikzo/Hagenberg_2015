package net.gustavdahl.engine.systems;

import net.gustavdahl.engine.entities.Entity;

public class EditorMultiSelectionState implements IEditorSelectionState
{

	public EditorMultiSelectionState()
	{
		// TODO Auto-generated constructor stub
	}

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
			if (editor._ctrlButtonDown)
			{
				boolean alreadySelected = editor.GetSelected(hit);
				editor.SetSelected(hit, !alreadySelected);
				return this;
			}
			else if (!editor._ctrlButtonDown)
			{
				editor.ClearRemoveAllSelectedEntities();
				editor.SetSelected(hit, true);
				return new EditorSingleSelectionState();
			}
		}
		/*Entity hit = editor.ClickSelect();

		if (hit == null && !editor._ctrlButtonDown) // no selection
		{
			editor.SetSelectedEntities(null, false);

			return new EditorIdleState();
		}

		if (hit != null)
		{
			if (editor._ctrlButtonDown) // multi selection
			{
				editor.SetSelectedEntities(hit, true);
				return this;
				
			} else if (!editor._ctrlButtonDown) // single selection
			{
				editor.SetSelectedEntities(hit, true);
					return new EditorSingleSelectionState();
					
			}
		}*/

		return this;
	}

	@Override
	public void Update(EditorSystem editor)
	{
		// System.out.println("Multi selection state");

		/*
		 * for (Entity e : editor._selectedEntities) {
		 * System.out.println(e.Name); }
		 */

	}

	@Override
	public void EnterState(EditorSystem editor)
	{
		System.out.println("Entering multi selection state");

	}

}
