package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextComponent extends SpriteComponent
{

	private String _text = "";
	
	public TextComponent(SpriteBatch spriteBatch, String text)
	{
		super(spriteBatch);
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
	public void Render()
	{
		// TODO Auto-generated method stub
		super.Render();
		
		PrintText(_text);
	}

}
