package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;
import java.util.List;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.IComponent;

public abstract class BaseSystem<T> implements ISystem
{
	private int _updatePriority = 1;
	protected boolean _isActive;
	protected List<T> _componentList = new ArrayList<T>();

	public BaseSystem()
	{
		_updatePriority = 1;
		_isActive = true;

	}

	public void Start()
					
	{
		for (int i = 0; i < _componentList.size(); i++)
		{
			if (((Component) _componentList.get(i)).HasBeenInitialized())
				continue;

			((Component) _componentList.get(i)).Initialize();
		}
	}

	public void Update(float deltaTime)
	{
		if (!_isActive)
			return;

		if (_componentList.size() < 1)
		{
			// System.out.println("ERROR - no components in system! (Update) - "
			// + this.getClass().getSimpleName());
			return;
		}

		for (int i = 0; i < _componentList.size(); i++)
		{
			if (!((Component) _componentList.get(i)).IsActive())
				continue;

			((Component) _componentList.get(i)).Update(deltaTime);

		}

	}

	public boolean AddComponentToSystem(Component component)
	{
		boolean canBeAdded = ValidateIfComponentCanBeAddedToSystem(component);

		if (canBeAdded)
		{
			if (!_componentList.contains(component))
				_componentList.add((T) component);
		}

		return canBeAdded;
	}

	public void SetActive(boolean active)
	{
		_isActive = active;
	}

	public void SetActive()
	{
		SetActive(_isActive);
	}

	public boolean GetActive()
	{
		return _isActive;
	}

	public int GetUpdatePriority()
	{
		return _updatePriority;
	}

	public void SetUpdatePriority(int _updatePriority)
	{
		this._updatePriority = _updatePriority;
	}

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

}
