package EditorActionStates;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorIdleActionState implements IEditorActionState
{


	@Override
	public void Update(EditorSystem editor)
	{
		
		
	}

	@Override
	public void EnterState()
	{
		System.out.println("Entering idle action state");
		
	}

}
