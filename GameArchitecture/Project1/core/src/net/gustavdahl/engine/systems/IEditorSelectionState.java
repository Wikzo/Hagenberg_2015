package net.gustavdahl.engine.systems;

import java.util.List;

import net.gustavdahl.engine.components.EditorComponent;

public interface IEditorSelectionState
{
	IEditorSelectionState HandleInput(EditorSystem editor);
	void Update(EditorSystem editor);
	void EnterState(EditorSystem editor);
}
