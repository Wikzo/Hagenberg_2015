package net.gustavdahl.echelonengine.systems;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.persistence.LevelCommand;
import net.gustavdahl.echelonengine.components.persistence.Persistable;

public class PersistenceSystem extends BaseSystem<Persistable>
{

	//private List<Persistable> _persistables;
	private static List<LevelCommand> _quickStore;

	public PersistenceSystem()
	{
		_quickStore = new ArrayList<LevelCommand>();
	}

	public void remove(Persistable persistable)
	{
		// silently ignore if persistable is unknown (best practice)
		_componentList.remove(persistable);
	}

	public void store(File file) throws IOException
	{
		store();

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)))
		{

			out.writeObject(_quickStore);
			Gdx.app.log(getClass().getSimpleName(),
					"entities (" + _componentList.size() + ") stored in " + file.getAbsolutePath());
		}
	}

	public void restore(File file) throws FileNotFoundException, IOException
	{
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file)))
		{

			@SuppressWarnings("unchecked")
			List<LevelCommand> actions = (List<LevelCommand>) in.readObject();
			for (LevelCommand action : actions)
			{
				action.Execute();
			}
			Gdx.app.log(getClass().getSimpleName(),
					"entities (" + actions.size() + ") restored from " + file.getAbsolutePath());

		} catch (ClassNotFoundException e)
		{
			throw new IOException("invalid world file ", e);
		}
	}

	public void store()
	{
		_quickStore.clear();
		for (int i = 0; i < _componentList.size(); ++i)
		{
			Persistable persistable = _componentList.get(i);
			_quickStore.add(persistable.CreateCommand());
		}
	}

	public void restore()
	{
		for (int i = 0; i < _quickStore.size(); ++i)
		{
			_quickStore.get(i).Execute();
		}
	}


	@Override
	public boolean ValidateIfComponentCanBeAddedToSystem(Component c)
	{
		if (c instanceof Persistable)
			return true;
		else
			return false;
	}

}
