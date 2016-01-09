package net.gustavdahl.echelonengine.systems;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.persistence.LevelCommand;
import net.gustavdahl.echelonengine.components.persistence.Persistable;
import net.gustavdahl.echelonengine.components.physics.IPhysics;

public class PersistenceSystem extends BaseSystem
{

	private List<Persistable> _persistables;
	private static List<LevelCommand> _quickStore;

	public PersistenceSystem()
	{
		_persistables = new ArrayList<Persistable>();
		_quickStore = new ArrayList<LevelCommand>();
	}

	public void remove(Persistable persistable)
	{
		// silently ignore if persistable is unknown (best practice)
		_persistables.remove(persistable);
	}

	public void store(File file) throws IOException
	{
		store();

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)))
		{

			out.writeObject(_quickStore);
			Gdx.app.log(getClass().getSimpleName(),
					"entities (" + _persistables.size() + ") stored in " + file.getAbsolutePath());
		}
	}

	public void store()
	{
		_quickStore.clear();
		for (int i = 0; i < _persistables.size(); ++i)
		{
			Persistable persistable = _persistables.get(i);
			_quickStore.add(persistable.CreateCommand());
		}
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof Persistable)
		{
			succesfullyAdded = true;
			_persistables.add((Persistable) c);

		} else
			throw new RuntimeException("ERROR - component " + c.getClass().getSimpleName()
					+ " doesn't implement PhysicsComponent interface!");

		return succesfullyAdded;
	}

}
