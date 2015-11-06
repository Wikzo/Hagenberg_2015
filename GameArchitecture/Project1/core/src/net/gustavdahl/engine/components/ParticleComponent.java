package net.gustavdahl.engine.components;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParticleComponent extends SpriteSheetAnimatorComponent
{
	public ParticleComponent(TextureRegion[] regions, float framerate)
	{
		super(regions, framerate);
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
