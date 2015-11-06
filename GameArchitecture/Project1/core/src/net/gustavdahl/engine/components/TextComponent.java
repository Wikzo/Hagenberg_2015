package net.gustavdahl.engine.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextComponent extends SpriteComponent
{
	
	// TODO: don't extend SpriteComponent (no texture)!

	private String _text = "";
	
	public TextComponent(SpriteBatch spriteBatch, String text)
	{
		super(null);
		_text = text;
	}
	
	public TextComponent(SpriteBatch spriteBatch)
	{
		this(spriteBatch, "DummyText");
	}

	
	
	public void PrintText(String text)
	{
		System.out.println(text);
	}
	

	
	@Override
	public void Render(SpriteBatch spriteBatch, float deltaTime)
	{
		
		super.Render(spriteBatch, deltaTime);
		
		PrintText(_text);
	}

}
