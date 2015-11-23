package EditorActionStates;

import net.gustavdahl.engine.systems.EditorSystem;

public interface IEditorActionState
{
	void Update(EditorSystem editor);
	void EnterState();
}
