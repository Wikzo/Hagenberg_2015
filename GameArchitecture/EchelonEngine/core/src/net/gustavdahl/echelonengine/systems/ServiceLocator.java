package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.echelonengine.components.Component;

public class ServiceLocator
{

	public static MyAssetManager AssetManager; // TODO: change type to AssetManager
	public static EntityManager EntityManager;

	private static ArrayList<BaseSystem> _systems;

	public ServiceLocator(MyAssetManager assetManager, EntityManager entityManager)
	{
		this.AssetManager = assetManager;
		this.EntityManager = entityManager;

		_systems = new ArrayList<BaseSystem>();
	}

	public static void RegisterNewSystem(BaseSystem system)
	{
		_systems.add(system);
	}

	public static void RemoveSystem(BaseSystem system)
	{
		_systems.remove(system);
	}

	public static <T extends BaseSystem> T GetSystem(Class<T> clazz) 
	{
		for (int i = 0; i < _systems.size(); i++)
		{
			// check if clazz and system's class match
			if (_systems.get(i).getClass().isAssignableFrom(clazz))
			{
				//System.out.println(clazz.getSimpleName() + " was added to " + _systems.get(i).getClass().getSimpleName());
				return clazz.cast(_systems.get(i));
				//return (T) _systems.get(i);
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
		if (_systems.size() < 1)
		{
			System.out.println("ERROR - no systems! (InitializeSystems)");
			return;
		}
		
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Start();
	}



	public static void UpdateSystems(float deltaTime)
	{
		if (_systems.size() < 1)
		{
			System.out.println("ERROR - no systems! (UpdateSystems)");
			return;
		}
		
		
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Update(deltaTime);
	}

	public void DestroyAllSystems()
	{
		if (_systems.size() < 1)
		{
			System.out.println("ERROR - no systems! (DestroyAllSystems)");
			return;
		}
		
		System.out.println("[Destroying all systems]");
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Destroy();
	}

}
