package net.gustavdahl.projectone.EntityComponentSystem;

public interface IUpdatable
{
	int UpdateOrder();
	void Update();
}
