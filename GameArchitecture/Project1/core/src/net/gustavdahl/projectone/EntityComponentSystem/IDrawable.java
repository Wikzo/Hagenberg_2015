package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IDrawable
{
	int DrawOrder();
	void Render(SpriteBatch spriteBatch);
	void DebugRender(SpriteBatch spriteBatch);
}
