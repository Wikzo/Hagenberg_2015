package net.gustavdahl.echelonengine.editoractionstates;


import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorMoveActionState implements IEditorActionState
{

	
	@Override
	public void Update(EditorSystem editor)
	{
		//editor.UnprojectMouse();
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			editor.GetSelectedEntities().get(i)
					.SetPosition(editor.GetStartPosition(i).x - editor.GetMouseDeltaMovement().x,
							editor.GetStartPosition(i).y - editor.GetMouseDeltaMovement().y);
		}
		
	}

	@Override
	public void EnterState()
	{
		//System.out.println("Entering move action state");
	}
}
