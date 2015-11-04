package net.gustavdahl.engine.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IRenderable
{
	int DrawOrder();
	void Render();
	void DebugRender();
}
