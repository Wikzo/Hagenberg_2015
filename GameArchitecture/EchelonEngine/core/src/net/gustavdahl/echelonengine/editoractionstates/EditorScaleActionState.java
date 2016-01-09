package net.gustavdahl.echelonengine.editoractionstates;

import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorScaleActionState implements IEditorActionState
{

	@Override
	public void Update(EditorSystem editor)
	{
		for (int i = 0; i < editor.GetSelectedEntities().size(); i++)
		{
			// TODO: make mouse movement relative
			
			// mapping from world space to clip space (-1 to 1)
			// new_value = (old_value - old_bottom) / (old_top - old_bottom) * (new_top - new_bottom) + new_bottom
			
			float newY = (editor.GetMousePosition().y - 768) / (0 - 768) * (1 - 0) + 0;
			
			Vector2 m = new Vector2(newY*2f, newY*2f);
			//System.out.println(m);
			editor.GetSelectedEntities().get(i).SetScale(m.x, m.y);
		}
		
	}

	@Override
	public void EnterState()
	{
		//System.out.println("Entering scale action state");
		
	}


}
