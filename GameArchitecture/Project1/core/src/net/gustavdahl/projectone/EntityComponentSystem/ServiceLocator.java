package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.projectone.Assets;

public class ServiceLocator
{

	public static Assets AssetManager; // TODO: change type to AssetManager
	public static EntityManager EntityManager;
	
	static ArrayList<ISystem> Systems; 
	
	public ServiceLocator(Assets assetManager, EntityManager entityManager)
	{
		this.AssetManager = assetManager;
		//this.EntityManager = entityManager;
		
		Systems = new ArrayList<ISystem>();
	}
	

	
	public static void RegisterSystem(ISystem s)
	{
		Systems.add(s);
		
	}
	
	public static void UnregisterSystem(ISystem s)
	{
		Systems.remove(s);
	}
	
	void SetActive(ISystem s, boolean active) {}
	
	// TODO: distinguish between different systems via update priority/order
	
	public static void InitializeSystems()
	{
		for (int i = 0; i < Systems.size(); i++)
			Systems.get(i).Initialize();
	}
	
	public static void StartSystems()
	{
		for (int i = 0; i < Systems.size(); i++)
			Systems.get(i).Start();
	}
	
	public static void UpdateSystems()
	{
		for (int i = 0; i < Systems.size(); i++)
			Systems.get(i).Update();
	}
	
	public void DestroyAllSystems()
	{
		System.out.println("[Destroying all systems]");
		for (int i = 0; i < Systems.size(); i++)
			Systems.get(i).Destroy();
	}

}
