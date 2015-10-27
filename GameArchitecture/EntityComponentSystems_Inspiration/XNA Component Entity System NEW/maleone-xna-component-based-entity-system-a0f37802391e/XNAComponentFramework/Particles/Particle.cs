using System;
using System.Collections.Generic;
using ComponentFramework;
using Microsoft.Xna.Framework;

namespace ComponentFramework.Particles
{
    class Particle : GameObject
    {
        public float RotationSpeed = 0.1f;
        public float MaxLifeTime = 10000f;

        public Particle(Vector2 aPosition)
        {
            this.Position = aPosition;
        }

        public Particle(Vector2 aPosition, Vector2 aVelocity, Vector2 aAcceleration, Vector2 aScale, float aRotationSpeed, float aMaxLifeTime)
        {
            this.Position = aPosition;
            this.Velocity = aVelocity;
            this.Acceleration = aAcceleration;
            this.Scale = aScale;
            this.RotationSpeed = aRotationSpeed;
            this.MaxLifeTime = aMaxLifeTime;
        }

        protected override void Update()
        {
            this.Rotation += RotationSpeed * (float)TimeManager.SecondDifference;

            if (this.LifeTime > MaxLifeTime)
            {
                this.Destroy();
            }
        }

        protected override void Draw(Microsoft.Xna.Framework.Graphics.SpriteBatch spriteBatch)
        {
            
        }
    }
}
