package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SpriteComponent extends Component implements IRenderable
{

	SpriteBatch _spriteBatch;
	Texture Texture;
	Color Color;
	int SortingLayer;
	Vector2 RenderOffset;
	boolean IsVisible;
	
	private TransFormComponent _transform;
	
	@Override
	public void GetExternalReferences()
	{
		// grab spritebatch?
	}
	
	public SpriteComponent(SpriteBatch spriteBatch)
	{
		this._spriteBatch = spriteBatch;
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
	public void Render()
	{
		if (!IsVisible)
			return;
		
		// TODO: use offset, rotation, size, etc...
		_spriteBatch.draw(this.Texture, this._transform.Position.x, this._transform.Position.y);
	}

	@Override
	public void DebugRender()
	{
		// TODO Auto-generated method stub
		
	}
	


}
