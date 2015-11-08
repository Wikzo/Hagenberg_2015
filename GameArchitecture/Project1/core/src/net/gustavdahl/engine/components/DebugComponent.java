package net.gustavdahl.engine.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import net.gustavdahl.engine.systems.PhysicsSystem;


public class DebugComponent extends Component implements IDebugRenderable
{

	private BitmapFont _font;
	private String _debugText = "";
	
	boolean Position, Name, Components;
	
	public DebugComponent(BitmapFont f)
	{
		super();
		_font = f;
		
	}
	
	
	// TODO: pass parameters (e.g. position) by reference instead of values
	
	public DebugComponent SetRenderPosition(boolean b)
	{
		Position = b;
		return this;
	}
	
	public DebugComponent SetRenderName(boolean b)
	{
		Name = b;
		return this;
	}
	
	public DebugComponent SetRenderAllComponents(boolean b)
	{
		Components = b;
		return this;
	}
	
	private void CheckInput()
	{
		if (Gdx.input.isKeyJustPressed(Keys.C))
		{			
			Owner.AddComponent(new ConstantForce(Vector2.Zero, 2f), PhysicsSystem.SystemName);
			System.out.println("ConstantForce added");
		
		}
	}


	@Override
	public void DebugRender(SpriteBatch spriteBatch, float deltaTime)
	{
		CheckInput();
		
		String s = "";
		if (Name)
			s += Owner.Name + ";";
		
		if (Position)
			s += Owner.GetTransform().Position.toString() + "\n";
		
		if (Components)
			s += Owner.GetAllComponents().toString() + ";";
		
			
		_font.draw(spriteBatch, s, 30,50);
		

	}

}
