package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.echelonengine.components.Component;

public abstract class BaseSystem implements ISystem
{
	private int _updatePriority = 1;
	protected boolean _isActive;
	protected List<Component> _componentList; // TODO: make generic T type!

	
	public BaseSystem()
	{
		_updatePriority = 1;
		_componentList = new ArrayList<Component>();
		_isActive = true;
	}

	public void Start() // TODO: should work with any list (not only component list)
	{
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (_componentList.get(i).HasBeenInitialized())
				continue;

			_componentList.get(i).Initialize();
		}
	}

	public void Update(float deltaTime)
	{
		if (!_isActive)
			return;
		
		if (_componentList.size() < 1)
		{
			//System.out.println("ERROR - no components in system! (Update) - " + this.getClass().getSimpleName());
			return;
		}

		for (int i = 0; i < _componentList.size(); i++)
		{
			if (!_componentList.get(i).IsActive())
				continue;

			_componentList.get(i).Update(deltaTime);

		}

	}

	public void SetActive(boolean active) {	_isActive = active; }
	public void SetActive() {	SetActive(_isActive); }
	public boolean GetActive() { return _isActive; }
	public int GetUpdatePriority() { return _updatePriority; }
	public void SetUpdatePriority(int _updatePriority) { this._updatePriority = _updatePriority; }

	public void Destroy()
	{
		System.out.println("[Destroying " + this.getClass().getSimpleName() + "]");

		for (int i = 0; i < _componentList.size(); i++)
		{
			// _componentList.get(i).Owner.RemoveComponentOfType(_componentList.get(i).getClass());
			// _componentList.get(i).Owner.RemoveAllComponents();
			// TODO: also destroy the entity itself! (maybe?)
		}

		// _componentList.clear();
		// _componentList = null;

	}

	
	protected void SuccesfullyAddedToSystem(String name, boolean success)
	{
		if (success)
			System.out.println("Component " + name + "  successfully added to appropiate system");
		else
			System.out.println("Component " + name + " NOT added to appropiate system");
	}

}
