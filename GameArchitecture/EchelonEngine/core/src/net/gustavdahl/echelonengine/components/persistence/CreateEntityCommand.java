package net.gustavdahl.echelonengine.components.persistence;

import java.io.Serializable;

public interface CreateEntityCommand extends Serializable
{
	public abstract void Execute();
}
