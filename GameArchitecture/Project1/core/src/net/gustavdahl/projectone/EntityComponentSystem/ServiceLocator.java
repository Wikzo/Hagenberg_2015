package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.List;

import net.gustavdahl.projectone.Assets;

public class ServiceLocator
{

	public static SystemManager SystemManager;
	public static Assets AssetManager; // TODO: change type to AssetManager
	public static EntityManager EntityManager;
	
	public ServiceLocator(SystemManager systemManager, Assets assetManager, EntityManager entityManager)
	{
		this.SystemManager = systemManager;
		this.AssetManager = assetManager;
		this.EntityManager = entityManager;
	}

}
