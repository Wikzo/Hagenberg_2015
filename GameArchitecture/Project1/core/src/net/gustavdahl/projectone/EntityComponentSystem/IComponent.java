package net.gustavdahl.projectone.EntityComponentSystem;

public interface IComponent
{
	String Name();
	void Initialize();
	void GetExternalReferences();
}
