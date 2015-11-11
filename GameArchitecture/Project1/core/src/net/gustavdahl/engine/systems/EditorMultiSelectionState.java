package net.gustavdahl.engine.systems;

import net.gustavdahl.engine.entities.Entity;

public class EditorMultiSelectionState implements IEditorSelectionState
{

	public EditorMultiSelectionState()
	{
		// TODO Auto-generated constructor stub
	}

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
			if (modifier == SelectionModifier.Control)
			{
				boolean alreadySelected = hit.CurrentlySelectedByEditor;
				editor.SetSelected(hit, !alreadySelected);
				return this;
			} else if (modifier != SelectionModifier.Control)
			{
				editor.ClearRemoveAllSelectedEntities();
				editor.SetSelected(hit, true);
				return new EditorSingleSelectionState();
			}
		}

		return this;
	}

	@Override
	public void Update(EditorSystem editor, SelectionModifier modifier)
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
