package net.gustavdahl.echelonengine.editoractionstates;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorDuplicateActionState implements IEditorActionState
{


	@Override
	public void Update(EditorSystem editor)
	{
		System.out.println("Duplicating...");
		
	}

	@Override
	public void EnterState()
	{
		System.out.println("Entering duplicate action state");
		
	}

}
