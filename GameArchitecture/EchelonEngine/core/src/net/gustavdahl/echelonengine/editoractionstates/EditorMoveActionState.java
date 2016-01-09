package net.gustavdahl.echelonengine.editoractionstates;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorMoveActionState implements IEditorActionState
{

	@Override
	public void Update(EditorSystem editor)
	{
		editor.UnprojectMouse();
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			editor.GetSelectedEntities().get(i)
					.SetPosition(editor.GetMousePosition().x + (i*20) + 10, editor.GetMousePosition().y);
		}
		
	}

	@Override
	public void EnterState()
	{
		//System.out.println("Entering move action state");
		
	}
}
