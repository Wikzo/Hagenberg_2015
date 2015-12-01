package net.gustavdahl.gummachine;

public interface IGumState
{
	IGumState HandleInput(Input input, GumMachine machine);
}
