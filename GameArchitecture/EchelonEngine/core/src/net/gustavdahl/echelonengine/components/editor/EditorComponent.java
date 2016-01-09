package net.gustavdahl.echelonengine.components.editor;

import net.gustavdahl.echelonengine.components.Component;
import net.gustavdahl.echelonengine.components.colliders.Collider;
import net.gustavdahl.echelonengine.components.visual.SpriteComponent;
import net.gustavdahl.echelonengine.systems.EditorSystem;

public class EditorComponent extends Component
{
	protected Collider _collider;
	private SpriteComponent _sprite;
	
	public EditorComponent()
	{
		super();
		
		_defaultSystem = EditorSystem.class;
	}
	
	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();
		
		if ( Owner.GetComponent(Collider.class) == null)
			throw new RuntimeException("ERROR! - " + Owner.Name + " has no Collider component!"); // TODO: this does not work?
		else
			_collider = (Collider) Owner.GetComponent(Collider.class);
		
		if ( Owner.GetComponent(SpriteComponent.class) == null)
			throw new RuntimeException("ERROR! - " + Owner.Name + " has no Sprite component!");
		else
			_sprite = (SpriteComponent) Owner.GetComponent(SpriteComponent.class);
	}
	
	@Override
	public void Update(float deltaTime)
	{
		//System.out.println(_collider.Name());

	}
	
	public Collider GetCollider()
	{
		return _collider;
	}

}
