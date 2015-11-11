package net.gustavdahl.engine.systems;

import net.gustavdahl.engine.entities.Entity;

public class EditorSingleSelectionState implements IEditorSelectionState
{

	@Override
	public IEditorSelectionState HandleInput(EditorSystem editor, SelectionModifier modifier)
	{

		Entity hit = editor.EntityRaycast();

		if (hit == null)
		{
			editor.ClearRemoveAllSelectedEntities();
			return new EditorIdleState();
		} else
		{
			if (modifier != SelectionModifier.Control)
			{
				boolean alreadySelected = hit.CurrentlySelectedByEditor;
				editor.ClearRemoveAllSelectedEntities();
				editor.SetSelected(hit, !alreadySelected);

				if (!hit.CurrentlySelectedByEditor)
					return new EditorIdleState();
				else
					return this;
			} else if (modifier == SelectionModifier.Control)
			{
				boolean alreadySelected = hit.CurrentlySelectedByEditor;
				editor.SetSelected(hit, !alreadySelected);

				return new EditorMultiSelectionState();

			}
		}

		return this;

	}

	@Override
	public void Update(EditorSystem editor, SelectionModifier modifier)
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
