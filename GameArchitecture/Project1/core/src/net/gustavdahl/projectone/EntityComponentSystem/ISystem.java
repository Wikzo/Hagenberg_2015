package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.List;

public interface ISystem
{
	// TODO: make generic list
	List<Component> ComponentList();
	
	void Initialize();
	void Start();
	void Update();
}
