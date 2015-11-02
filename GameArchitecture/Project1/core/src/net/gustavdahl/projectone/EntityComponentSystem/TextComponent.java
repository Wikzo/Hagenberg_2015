package net.gustavdahl.projectone.EntityComponentSystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextComponent extends SpriteComponent
{

	public static String Name = "TextComponent";
	
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
	public void Render()
	{
		// TODO Auto-generated method stub
		//super.Render();
		
		//PrintText();
	}

}
