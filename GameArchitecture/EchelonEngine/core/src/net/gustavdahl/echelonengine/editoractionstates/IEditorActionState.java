package net.gustavdahl.echelonengine.editoractionstates;

import net.gustavdahl.echelonengine.systems.EditorSystem;

public interface IEditorActionState
{
	void Update(EditorSystem editor);
	void EnterState();
}
