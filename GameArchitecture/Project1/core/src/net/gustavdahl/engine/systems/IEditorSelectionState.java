package net.gustavdahl.engine.systems;

import java.util.List;

import net.gustavdahl.engine.components.EditorComponent;

public interface IEditorSelectionState
{
	IEditorSelectionState HandleInput(EditorSystem editor, SelectionModifier modifier);
	void Update(EditorSystem editor, SelectionModifier modifier);
	void EnterState(EditorSystem editor);
}
