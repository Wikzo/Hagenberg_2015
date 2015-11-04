package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.engine.Assets;
import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.ISystem;
import net.gustavdahl.engine.entities.EntityManager;

public class ServiceLocator
{

	public static Assets AssetManager; // TODO: change type to AssetManager
	public static EntityManager EntityManager;
	
	private static ArrayList<ISystem> _systems; 
	
	public ServiceLocator(Assets assetManager, EntityManager entityManager)
	{
		this.AssetManager = assetManager;
		//this.EntityManager = entityManager;
		
		_systems = new ArrayList<ISystem>();
	}
	

	
	public static void RegisterNewSystem(ISystem system)
	{
		
			_systems.add(system);
		
	}
	
	public static void RemoveSystem(ISystem s)
	{
		_systems.remove(s);
	}
	
	public static void AddComponentToSystem(Component c, String systemName)
	{
		//if (_components.get(i).getClass().getSimpleName().equalsIgnoreCase(componentType))

			for (int i = 0; i < _systems.size(); i++)
			{
				if (_systems.get(i).getClass().getSimpleName().equalsIgnoreCase(systemName))
					_systems.get(i).AddToSystem(c);
			}
	}
	
	void SetActive(ISystem s, boolean active) {}
	
	// TODO: distinguish between different systems via update priority/order
	
	public static void InitializeSystems()
	{
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Initialize();
	}
	
	public static void StartSystems()
	{
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Start();
	}
	
	public static void UpdateSystems()
	{
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Update();
	}
	
	public void DestroyAllSystems()
	{
		System.out.println("[Destroying all systems]");
		for (int i = 0; i < _systems.size(); i++)
			_systems.get(i).Destroy();
	}

}
