package net.gustavdahl.echelonengine.editoractionstates;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorRotateActionState implements IEditorActionState
{
	private final float _rotationSensitivity = 0.1f;

	@Override
	public void Update(EditorSystem editor)
	{
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			editor.GetSelectedEntities().get(i)
					.SetRotation(editor.GetStartRotation(i) - editor.GetMouseDeltaMovement().y * _rotationSensitivity);
		}
		
	}

	@Override
	public void EnterState()
	{
		//System.out.println("Entering rotate action state");
		
	}
	
	@Override
	public void Reset(EditorSystem editor)
	{
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			editor.GetSelectedEntities().get(i).ResetRotation();
		}
	}

}
