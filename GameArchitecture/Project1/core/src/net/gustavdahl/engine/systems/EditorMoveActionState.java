package net.gustavdahl.engine.systems;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class EditorMoveActionState implements IEditorActionState
{

	@Override
	public void Update(EditorSystem editor)
	{
		editor.UnprojectMouse();
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			editor.GetSelectedEntities().get(i)
					.SetPosition(new Vector2(editor.GetMousePosition().x + (i*20) + 10, editor.GetMousePosition().y));
		}
		
	}

	@Override
	public void EnterState()
	{
		System.out.println("Entering move action state");
		
	}
}
