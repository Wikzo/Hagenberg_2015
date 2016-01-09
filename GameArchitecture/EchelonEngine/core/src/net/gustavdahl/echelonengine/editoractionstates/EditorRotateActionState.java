package net.gustavdahl.echelonengine.editoractionstates;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorRotateActionState implements IEditorActionState
{

	@Override
	public void Update(EditorSystem editor)
	{
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			editor.GetSelectedEntities().get(i)
					.SetRotation(editor.GetMousePosition().y / 10f);
		}
		
	}

	@Override
	public void EnterState()
	{
		//System.out.println("Entering rotate action state");
		
	}

}
