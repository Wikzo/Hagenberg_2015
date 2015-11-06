package net.gustavdahl.engine.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IRenderable
{
	void Render(SpriteBatch spriteBatch, float deltaTime);
	void DebugRender();
}
