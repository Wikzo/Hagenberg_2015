package net.gustavdahl.echelonengine.systems;

import net.gustavdahl.echelonengine.components.Component;

public interface ISystem
{
	// TODO: use generic type
	boolean AddToSystem(Component c);
}
