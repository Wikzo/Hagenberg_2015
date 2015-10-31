package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.ArrayList;

public class SystemManager
{

	ArrayList<ISystem> Systems; 
	
	public SystemManager()
	{
		// TODO Auto-generated constructor stub
	}
	
	void RegisterSystem(ISystem s)
	{
		Systems.add(s);
		
	}
	
	void UnregisterSystem(ISystem s)
	{
		Systems.remove(s);
	}
	
	void SetActive(ISystem s, boolean active) {}
	
	// TODO: distinguish between different systems via update priority/order
	
	void InitializeSystems()
	{
		for (int i = 0; i < Systems.size(); i++)
			Systems.get(i).Initialize();
	}
	
	void StartSystems()
	{
		for (int i = 0; i < Systems.size(); i++)
			Systems.get(i).Start();
	}
	
	void UpdateSystems()
	{
		for (int i = 0; i < Systems.size(); i++)
			Systems.get(i).Update();
	}


}
