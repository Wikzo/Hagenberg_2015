package net.gustavdahl.engine.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

public class BoxCollider extends Collider implements IDebugRenderable
{

	protected float _width, _height;
	protected float _halfWidth, _halfHeight;
	private Rectangle _bounds;

	public BoxCollider(float width, float height)
	{
		super();

		_width = width;
		_height = height;
		_halfWidth = _width / 2;
		_halfHeight = _height / 2;

	}

	@Override
	public void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime)
	{
		Gdx.gl.glEnable(GL30.GL_BLEND);


		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(_currentDebugColor);
		
		// TODO: make box rotate as well
		// https://stackoverflow.com/questions/14091734/libgdx-shaperenderer-how-to-rotate-rectangle-around-its-center
		//shapeRenderer.translate(Transform.Position.x, Transform.Position.y, 0);
		//shapeRenderer.rotate(0f, 0f, 1f, Transform.Rotation);
		
		shapeRenderer.box(Bounds().x, Bounds().y, 0, Bounds().width, Bounds().height, 1);
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL30.GL_BLEND);

	}

	@Override
	public void GetExternalReferences()
	{
		super.GetExternalReferences();
	}

	public Rectangle Bounds()
	{
		return new Rectangle(Transform.Position.x - _halfWidth * Transform.Scale.x,
				Transform.Position.y - _halfHeight * Transform.Scale.y,
				_width * Transform.Scale.x,
				_height * Transform.Scale.y);
	}

	@Override
	public boolean IsHit(ICollider collider)
	{
		// http://www.wildbunny.co.uk/blog/2011/04/20/collision-detection-for-dummies/comment-page-1/
		
		if (collider instanceof BoxCollider)
		{

			BoxCollider otherBox = (BoxCollider) collider;
			
			float distance = this.GetCenter().dst(otherBox.GetCenter());
			
			Vector2 distances = this.GetCenter().sub(otherBox.GetCenter());
			// absolute value
			if (distances.x < 0)
				distances.x = -distances.x;
			
			if (distances.y < 0)
				distances.y = -distances.y;
			
			Vector2 halfA = new Vector2(this.Bounds().width/2, this.Bounds().height/2);
			Vector2 halfB = new Vector2(otherBox.Bounds().width/2, otherBox.Bounds().height/2);
			
			Vector2 d = distances.sub(halfA.add(halfB));
			
			//binary overlap = D_x < 0 && D_y < 0;
			
			//System.out.println(d);
			
			if (d.x < 0 && d.y < 0)
				return true;
			else
				return false;

		}
		return false;
	}

	@Override
	public void Update(float deltaTime)
	{
		// TODO Auto-generated method stub
		
	}

}
