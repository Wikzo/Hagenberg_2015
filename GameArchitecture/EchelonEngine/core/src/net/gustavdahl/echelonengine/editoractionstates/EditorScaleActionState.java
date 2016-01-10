package net.gustavdahl.echelonengine.editoractionstates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorScaleActionState implements IEditorActionState
{
	private final float _scaleSensitivty = 0.1f;

	@Override
	public void Update(EditorSystem editor)
	{
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			editor.GetSelectedEntities().get(i).SetScale(
					editor.GetStartScale(i).x - editor.GetMouseDeltaMovement().x * _scaleSensitivty,
					editor.GetStartScale(i).y - editor.GetMouseDeltaMovement().y * _scaleSensitivty);
		}
	}

	@Override
	public void EnterState()
	{
		// System.out.println("Entering scale action state");

	}

	@Override
	public void Reset(EditorSystem editor)
	{
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			editor.GetSelectedEntities().get(i).ResetScale();
		}
	}

}
