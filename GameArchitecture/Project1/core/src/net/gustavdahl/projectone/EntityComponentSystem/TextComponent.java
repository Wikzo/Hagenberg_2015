package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextComponent extends SpriteComponent
{

	public TextComponent(SpriteBatch spriteBatch)
	{
		super(spriteBatch);
		// TODO Auto-generated constructor stub
	}

	private String _text = "test text";
	
	void PrintText()
	{
		System.out.println(_text);
	}
	
	@Override
	public String Name()
	{
		return "TextComponent";
	}
	
	@Override
	public void Render()
	{
		// TODO Auto-generated method stub
		//super.Render();
		
		PrintText();
	}

}
