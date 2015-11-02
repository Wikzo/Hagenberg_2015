package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IRenderable
{
	int DrawOrder();
	void Render();
	void DebugRender();
}
