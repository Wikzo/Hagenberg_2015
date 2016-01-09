package net.gustavdahl.echelonengine.editoractionstates;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public interface IEditorSelectionState
{
	IEditorSelectionState HandleInput(EditorSystem editor, boolean controlDown);
	void EnterState(EditorSystem editor);
}
