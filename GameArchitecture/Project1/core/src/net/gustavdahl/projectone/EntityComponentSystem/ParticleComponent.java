package net.gustavdahl.projectone.EntityComponentSystem;

import java.util.List;

public class ParticleComponent extends SpriteComponent
{
	int NumberOfParticles;
	float Duration;
	boolean ShouldLoop;
	float Size;

	List<SpriteComponent> ParticleSprites;

	void PrebakeParticlePool()
	{
		for (int i = 0; i < NumberOfParticles; i++)
		{
			SpriteComponent s = new SpriteComponent();
			
			ParticleSprites.add(s);
		}
	}

	void EmitParticles()
	{
	}

	void DestroyParticles()
	{
	}

}
