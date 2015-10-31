package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.List;

public class RenderSystem implements ISystem
{

	public RenderSystem()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Component> ComponentList()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Initialize()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Start()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Update()
	{
		// TODO Auto-generated method stub
		
	}
	
	public void Render()
	{
		Update();
	}
	
	public void AddToRenderSystem() {}

}
