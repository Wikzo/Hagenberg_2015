package EditorActionStates;
import net.gustavdahl.engine.components.EditorComponent;
import net.gustavdahl.engine.systems.EditorSystem;

public interface IEditorSelectionState
{
	IEditorSelectionState HandleInput(EditorSystem editor, boolean controlDown);
	void EnterState(EditorSystem editor);
}
