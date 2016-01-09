package net.gustavdahl.echelonengine.systems;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.IComponent;

public interface ISystem<T extends Component>
{
	public boolean ValidateIfComponentCanBeAddedToSystem(T component);
}