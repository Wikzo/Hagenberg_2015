package net.gustavdahl.engine.systems;

import com.badlogic.gdx.math.Vector2;

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
		switch (modifier)
		{
		case Control:
			break;
		case Duplicate:
			break;
		case Move:
			editor.UnprojectMouse();
			for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
			{
				editor.GetSelectedEntities().get(i)
						.SetPosition(new Vector2(editor.GetMousePosition().x + (i*20) + 10, editor.GetMousePosition().y));
			}

			break;
		case None:
			break;
		case Remove:
			break;
		case Rotate:
			for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
			{
				editor.GetSelectedEntities().get(i)
						.SetRotation(editor.GetMousePosition().y / 10f);
			}
			break;
		case Scale:
			for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
			{
				// TODO: make mouse movement relative
				
				// mapping from world space to clip space (-1 to 1)
				// new_value = (old_value - old_bottom) / (old_top - old_bottom) * (new_top - new_bottom) + new_bottom
				
				float newY = (editor.GetMousePosition().y - 768) / (0 - 768) * (1 - 0) + 0;
				
				Vector2 m = new Vector2(newY*2f, newY*2f);
				System.out.println(m);
				editor.GetSelectedEntities().get(i).SetScale(m);
			}
			break;
		default:
			break;

		}

	}

	@Override
	public void EnterState(EditorSystem editor)
	{
		System.out.println("Entering single selection state");

	}

}
