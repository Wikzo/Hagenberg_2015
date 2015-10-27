using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework;

namespace ComponentFramework.Animation
{
    public class FrameAnimation
    {
        public EventHandler<EventArgs> AnimationLooped;

        //list to hold all frames in this animation
        private List<SpriteFrame> _frames = new List<SpriteFrame>();

        private const int START_FRAME = 0;
        private int _currentFrame;
        private int _lastFrame;

        private float _animationTimer = 0.0f;

        public Texture2D Texture;

        public bool JustLooped = false;

        public SpriteFrame CurrentFrame
        {
            get { return _frames[_currentFrame]; }
        }

        public int CurrentFrameIndex
        {
            get { return _currentFrame; }
        }

        /// <summary>
        /// Basic constructor for FrameAnimation
        /// </summary>
        public FrameAnimation()
        {
            //always have the current frame be the start frame
            _currentFrame = START_FRAME;

            //hook up the event handler basic method
            AnimationLooped += OnAnimationLooped;
        }

        public FrameAnimation(Texture2D aTexture)
        {
            this.Texture = aTexture;
        }

        public void Destroy()
        {
            this.Texture = null;
            AnimationLooped -= OnAnimationLooped;
        }

        public FrameAnimation(Texture2D aTexture, params SpriteFrame[] aFramesInOrder)
        {
            this.Texture = aTexture;

            //set the current frame to the start frame
            _currentFrame = START_FRAME;

            for (int i = 0; i < aFramesInOrder.Length; i++)
            {
                _frames.Add(aFramesInOrder[i]);
            }

            //set the last frame to the last item in the list
            _lastFrame = (_frames.Count - 1);
        }

        public void Update()
        {
            //if the timer is greater than the current frame's frame length, increase current frame by 1
            if (_animationTimer > _frames[_currentFrame].FrameLength)
            {
                _currentFrame++;
                _animationTimer = 0.0f;
            }

            //check to see if the animation has just finished playing
            if (_currentFrame >= _lastFrame)
            {
                _currentFrame = START_FRAME;
                if (AnimationLooped != null)
                {
                    AnimationLooped(this, EventArgs.Empty);
                }
                JustLooped = true;
            }
            else
            {
                JustLooped = false;
            }

            //increase the animation timer by the game time
            _animationTimer += (float)TimeManager.SecondDifference;
        }

        public void Draw(SpriteBatch spriteBatch, Vector2 position,  Color color, float rotation, Vector2 scale, SpriteEffects spriteEffects, float drawLayer)
        {
            if (this.Texture != null)
            {
                SpriteFrame currentFrame = _frames[_currentFrame];

                spriteBatch.Draw(this.Texture, position, currentFrame.SourceRectangle, color, rotation, currentFrame.FrameDrawOffset, scale, spriteEffects, drawLayer); 
            }
        }

        protected virtual void OnAnimationLooped(object sender, EventArgs e) { }

        public void AddAnimationFrame(SpriteFrame aFrameToAdd)
        {
            _frames.Add(aFrameToAdd);
            _lastFrame = (_frames.Count - 1);
        }
    }
}
