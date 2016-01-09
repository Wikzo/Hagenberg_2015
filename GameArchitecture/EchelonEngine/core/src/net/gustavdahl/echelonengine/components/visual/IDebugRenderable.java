package net.gustavdahl.echelonengine.components.visual;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface IDebugRenderable
{
	void DebugRender(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer, float deltaTime);
	String OnSelectedText();
}
