package net.gustavdahl.engine.systems;
import net.gustavdahl.engine.components.EditorComponent;

public interface IEditorSelectionState
{
	IEditorSelectionState HandleInput(EditorSystem editor, boolean controlDown);
	void EnterState(EditorSystem editor);
}
