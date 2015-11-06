package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.engine.components.Component;

public class ServiceLocator
{

	public static Assets AssetManager; // TODO: change type to AssetManager
	public static EntityManager EntityManager;

	private static ArrayList<BaseSystem> _systems;

	public ServiceLocator(Assets assetManager, EntityManager entityManager)
	{
		this.AssetManager = assetManager;
		// this.EntityManager = entityManager;

		_systems = new ArrayList<BaseSystem>();
	}

	public static void RegisterNewSystem(BaseSystem system)
	{

		_systems.add(system);

	}

	public static void RemoveSystem(BaseSystem s)
	{
		_systems.remove(s);
	}

	public static boolean AddComponentToSystem(Component c, String systemName)
	{

		boolean success = false;

		for (int i = 0; i < _systems.size(); i++)
		{
			if (success)
				break;
			
			// check if name of system in the list matches systemName
			if (_systems.get(i).getClass().getSimpleName().equalsIgnoreCase(systemName))
			{
				
				_systems.get(i).AddToSystem(c);
				success = true;
			}
		}
		if (success)
			System.out.println(c.Name() + " was added to " + systemName);
		else
			System.out.println(c.Name() + " was NOT added to " + systemName);
		
		return success;
	}

	public static BaseSystem GetSystem(String systemName)
	{
		for (int i = 0; i < _systems.size(); i++)
		{
			// check if name of system in the list matches systemName
			if (_systems.get(i).getClass().getSimpleName().equalsIgnoreCase(systemName))
			{
				return _systems.get(i);
			}
		}
		
		return null;
	}
	
	void SetActive(BaseSystem s, boolean active)
	{
	}

	// TODO: distinguish between different systems via update priority/order

	public static void InitializeSystems()
	{
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Start();
	}



	public static void UpdateSystems(float deltaTime)
	{
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Update(deltaTime);
	}

	public void DestroyAllSystems()
	{
		System.out.println("[Destroying all systems]");
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Destroy();
	}

}
