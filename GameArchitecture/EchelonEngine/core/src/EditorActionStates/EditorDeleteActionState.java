package EditorActionStates;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorDeleteActionState implements IEditorActionState
{

	@Override
	public void Update(EditorSystem editor)
	{
		System.out.println("Deleting...");
		
	}

	@Override
	public void EnterState()
	{
		System.out.println("Entering delete action state");
		
	}
	
}
