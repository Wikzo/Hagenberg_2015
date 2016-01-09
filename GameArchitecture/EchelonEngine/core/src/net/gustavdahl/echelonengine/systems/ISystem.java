package net.gustavdahl.echelonengine.systems;

import net.gustavdahl.echelonengine.components.Component;

public interface ISystem<T extends Component>
{
	public boolean ValidateIfComponentCanBeAddedToSystem(T component);
}