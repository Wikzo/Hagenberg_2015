package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SpriteComponent extends Component implements IDrawable
{

	Texture Texture;
	Color Color;
	int SortingLayer;
	Vector2 RenderOffset;
	boolean IsVisible;
	
	private Transform _transform;
	
	@Override
	public void GetExternalReferences()
	{
		_transform = Owner.GetTransform();
	}
	
	public SpriteComponent()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void LoadContent()
	{
		// load via asset manager...
	}

	@Override
	public int DrawOrder()
	{
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void Render(SpriteBatch spriteBatch)
	{
		if (!IsVisible)
			return;
		
		// TODO: use offset, rotation, size, etc...
		spriteBatch.draw(this.Texture, this._transform.Position.x, this._transform.Position.y);
	}

	@Override
	public void DebugRender(SpriteBatch spriteBatch)
	{
		// TODO Auto-generated method stub
		
	}

}
