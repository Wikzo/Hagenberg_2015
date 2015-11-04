package net.gustavdahl.engine.systems;

import java.util.List;

import net.gustavdahl.engine.components.Component;

public interface ISystem
{
	void Start();
	void Update();
	void Destroy();
	boolean AddToSystem(Component c);
}
