package net.gustavdahl.engine.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sun.javafx.webkit.theme.Renderer;

import net.gustavdahl.engine.systems.MyAssetManager;
import net.gustavdahl.engine.systems.ServiceLocator;

public class SpriteComponent extends Component implements IRenderable
{

	protected Texture _texture;
	protected Color _color = new Color(Color.WHITE);
	protected int _sortingLayer;
	protected float _offsetX, _offsetY;
	protected TextureRegion _textureRegion;
	protected float _originX, _originY;
	protected float _width, _height;
	protected float _halfWidth, _halfHeight;
	protected float _scaleX = 1f, _scaleY = 1f;

	boolean IsVisible;


	public SpriteComponent(TextureRegion region)
	{
		super();
		this._textureRegion = region;
		_width = region.getRegionWidth();
		_height = region.getRegionHeight();
		
		LoadContent(ServiceLocator.AssetManager.DummyTexture);
	}

	public SpriteComponent Layer(int value)
	{
		_sortingLayer = value;
		return this;
	}

	public SpriteComponent Color(Color value)
	{
		_color.set(value);
		return this;
	}

	public SpriteComponent SetOriginCenter()
	{
		_originX = _width / 2;
		_originY = _height / 2;
		return this;
	}

	public SpriteComponent Origin(float ox, float oy)
	{
		_originX = ox;
		_originY = oy;
		return this;
	}

	public SpriteComponent Offset(float ox, float oy)
	{
		_offsetX = ox;
		_offsetY = oy;
		return this;
	}

	public SpriteComponent Width(float value)
	{
		_width = value;
		_halfWidth = _width / 2;
		return this;
	}

	public SpriteComponent Heighth(float value)
	{
		_height = value;
		_halfHeight = _height / 2;
		return this;
	}

	public SpriteComponent Scale(float sx, float sy)
	{
		_scaleX = sx;
		_scaleY = sy;
		return this;
	}

	public void LoadContent(Texture t)
	{
		// TODO: load via asset manager...
		if (t != null)
			_texture = t;
	}

	public void Render_old(SpriteBatch spriteBatch)
	{
		if (!IsVisible)
			return;

		if (!IsActive())
			return;

		spriteBatch.draw(this._texture, this.Transform.Position.x, this.Transform.Position.y);
	}
	
	@Override
	public void Render(SpriteBatch batch, float deltaTime)
	{
		

		// Texture Regions: https://github.com/libgdx/libgdx/wiki/Textures,-textureregion-and-spritebatch
		// Remember that the number goes from X to width
		// e.g.  regions[3] = new TextureRegion(texture, 0.5f, 0.5f, 1f, 1f);
		// ... will go from halfway to the end of the texture
		
		batch.setColor(_color);
		batch.draw(_textureRegion,
		_offsetX + Transform.Position.x - _halfWidth, // x position
		_offsetY + Transform.Position.x - _halfHeight, // y position
		_originX, _originY, // origin
		_width, _height, // size in world space
		_scaleX, _scaleY, // scaling factor
		MathUtils.radDeg * Transform.Rotation);
		
		
		
	}

	@Override
	public void DebugRender()
	{

	}

}
