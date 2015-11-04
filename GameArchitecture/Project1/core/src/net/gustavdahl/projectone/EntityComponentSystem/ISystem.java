package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.List;

public interface ISystem
{
	void Initialize();
	void Start();
	void Update();
	void Destroy();
	boolean AddToSystem(Component c);
}
