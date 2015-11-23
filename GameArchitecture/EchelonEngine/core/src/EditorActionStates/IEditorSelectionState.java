package EditorActionStates;
import net.gustavdahl.echelonengine.components.EditorComponent;
import net.gustavdahl.echelonengine.systems.EditorSystem;

public interface IEditorSelectionState
{
	IEditorSelectionState HandleInput(EditorSystem editor, boolean controlDown);
	void EnterState(EditorSystem editor);
}
