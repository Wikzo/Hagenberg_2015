package net.gustavdahl.engine.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.systems.Assets;

public class SpriteComponent extends Component implements IRenderable
{

	SpriteBatch _spriteBatch;
	Texture Texture;
	Color Color;
	int SortingLayer;
	Vector2 RenderOffset;
	boolean IsVisible;

	
	public SpriteComponent(SpriteBatch spriteBatch, Texture t)
	{
		super();
		IsVisible = true;
		this._spriteBatch = spriteBatch;
		LoadContent(t);

		
	}
	
	public void LoadContent(Texture t)
	{
		// load via asset manager...
		if (t != null)
			Texture = t;
		
	//_transform = Owner._transform;
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
		
		if (!IsActive())
			return;
		
		//_transform = Owner._transform;
		// TODO: use offset, rotation, size, etc...
		//System.out.println("Texture: " + this.Texture);
		//System.out.println("trans: " + this.Owner._transform);
		//System.out.println("PosX: " + this._transform.Position.x);

		_spriteBatch.draw(this.Texture, this._transform.Position.x, this._transform.Position.y);
	}

	@Override
	public void DebugRender()
	{
		// TODO Auto-generated method stub
		
	}
	


}
