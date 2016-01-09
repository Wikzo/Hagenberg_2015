package net.gustavdahl.echelonengine.components.persistence;

import java.io.Serializable;

public interface LevelCommand extends Serializable
{
	public abstract void Execute();
}
