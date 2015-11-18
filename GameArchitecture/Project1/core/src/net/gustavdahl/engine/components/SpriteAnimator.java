package net.gustavdahl.engine.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class SpriteAnimator extends SpriteComponent
{

	private TextureRegion[] _spriteSheet;
	private int _index;
	private float _framerate;
	private float _frameCounter;

	public SpriteAnimator(TextureRegion[] regions, float framerate)
	{
		super(regions[0]);

		_spriteSheet = regions;
		_framerate = framerate;

	}

	// TODO: make repeatable sprites
	
	public static TextureRegion[] CreateSpriteSheet(Texture texture, int numberOfPictures, int columns, int rows)
	{
		// number of pictures can be used if there are more rows/columns than actual pictures
		
		// TODO: maybe specify width/height of each sprite and use that to calculate columns and rows instead
		
		if (numberOfPictures > columns*rows)
			numberOfPictures = columns*rows;
		
		TextureRegion[] spritesheet = new TextureRegion[numberOfPictures];

		float xL = 1f / columns;
		float yL = 1f / rows;

		int index = 0;

		for (int y = 0; y < rows; y++)
		{
			for (int x = 0; x < columns; x++)
			{
				// skip if there aren't enough pictures
				if (index < numberOfPictures)
				{
					spritesheet[index] = new TextureRegion(texture, xL * x, yL * y, xL * (x + 1), yL * (y + 1));
					index++;
				}

			}
		}
		return spritesheet;
	}

	@Override
	public void Render(SpriteBatch batch, float deltaTime)
	{

		// set the frame rate
		_frameCounter += deltaTime;
		if (_frameCounter >= _framerate)
		{
			_index++;
			_frameCounter = 0;

			if (_index >= _spriteSheet.length)
				_index = 0;
			
			this._textureRegion = _spriteSheet[_index]; // set the animation frame
		}
		
		//_offsetX = 0;
		//_halfWidth = 0;
		//_offsetY = 0;
		//_halfHeight = 0;

		super.Render(batch, deltaTime);

	}

}
