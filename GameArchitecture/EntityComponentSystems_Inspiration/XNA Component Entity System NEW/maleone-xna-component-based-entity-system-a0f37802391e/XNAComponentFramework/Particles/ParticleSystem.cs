using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;

namespace ComponentFramework.Particles
{
    public class ParticleSystem : GameObject
    {
        public bool isOneShot = false;

        public GameObjectList Particles = new GameObjectList();

        public Texture2D Texture;
        private Vector2 _textureOrigin;

        //minimum and maximum values for particle system
        float _minParticleVelocity = -1f;
        float _maxParticleVeloicty = 1f;

        float _minParticleAcceleration = -1f;
        float _maxParticleAcceleration = 1f;

        float _minParticleLifetime = 0f;
        float _maxParticleLifetime = 10f;

        float _minParticleScale = 0f;
        float _maxParticleScale = 1f;

        float _minParticleRotationSpeed = -1f;
        float _maxParticleRotationSpeed = 1f;

        private float _lowerLimit = 0f;
        private float _upperLimit = MathHelper.TwoPi;

        public float LowerLimit
        {
            get { return _lowerLimit; }
            set
            {
                if (value < this._upperLimit)
                {
                    _lowerLimit = value;
                }
                else
                {
                    this._lowerLimit = _upperLimit;
                    this._upperLimit = value;
                }
            }
        }

        public float UpperLimit
        {
            get { return _upperLimit; }
            set
            {
                if (value > _lowerLimit)
                {
                    _upperLimit = value;
                }
                else
                {
                    this._upperLimit = _lowerLimit;
                    this._lowerLimit = value;
                }
            }
        }

        public ParticleSystem(Texture2D aTexture, Vector2 aPosition)
        {
            this.Texture = aTexture;
            this._textureOrigin = new Vector2(this.Texture.Width / 2, this.Texture.Height / 2);
            this.Position = aPosition;
            this.BlendState = BlendState.AlphaBlend;

            ParticleManager.AddParticleSystem(this);
        }

        public ParticleSystem(Texture2D aTexture, Vector2 aPosition, BlendState aBlendState)
            : this(aTexture, aPosition)
        {
            this.BlendState = aBlendState;
        }

        protected override void Update()
        {
            for (int i = 0; i < Particles.Count; i++)
            {
                Particles[i].PerformUpdate();
            }
        }

        protected override void Draw(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < Particles.Count; i++)
            {
                Particle current = Particles[i] as Particle;

                spriteBatch.Draw(this.Texture, current.Position, null, current.Color * current.Opacity, current.Rotation, _textureOrigin, current.Scale, SpriteEffects.None, 0f); 
            }
        }

        public void Emit()
        {
            Vector2 direction = Helper.GetRandomDirection(LowerLimit, UpperLimit);

            float velocity = Helper.RandomBetween(_minParticleVelocity, _maxParticleVeloicty);
            float acceleration = Helper.RandomBetween(_minParticleAcceleration, _maxParticleAcceleration);
            float lifetime = Helper.RandomBetween(_minParticleLifetime, _maxParticleLifetime);
            float scale = Helper.RandomBetween(_minParticleScale, _maxParticleScale);
            float rotationSpeed = Helper.RandomBetween(_minParticleRotationSpeed, _maxParticleRotationSpeed);

            Particle particle = new Particle(this.Position, direction * velocity, 
                direction * acceleration, new Vector2(scale, scale), rotationSpeed, lifetime);
            //GameObject.AddToList(Particles, particle);
            Particles.Add(particle);
        }
    }
}
