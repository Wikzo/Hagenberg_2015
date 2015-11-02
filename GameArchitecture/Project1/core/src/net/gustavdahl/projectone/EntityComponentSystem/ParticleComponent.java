package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleComponent extends SpriteComponent
{
	public ParticleComponent(SpriteBatch spriteBatch)
	{
		super(spriteBatch);
		// TODO Auto-generated constructor stub
	}

	int NumberOfParticles;
	float Duration;
	boolean ShouldLoop;
	float Size;

	List<SpriteComponent> ParticleSprites;

	void PrebakeParticlePool()
	{
		/*for (int i = 0; i < NumberOfParticles; i++)
		{
			SpriteComponent s = new SpriteComponent();
			
			ParticleSprites.add(s);
		}*/
	}

	void EmitParticles()
	{
	}

	void DestroyParticles()
	{
	}
	
	@Override
	public String Name()
	{
		return "ParticleComponent";
	}

}
