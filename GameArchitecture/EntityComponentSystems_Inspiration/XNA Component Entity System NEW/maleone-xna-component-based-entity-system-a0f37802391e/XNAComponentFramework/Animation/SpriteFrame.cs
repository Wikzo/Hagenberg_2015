using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace ComponentFramework.Animation
{
    public struct SpriteFrame
    {
        #region Fields
        private float _frameLength;

        public Rectangle SourceRectangle;

        public float FrameLength
        {
            get { return _frameLength; }
        }

        public Vector2 FrameDrawOffset;
        #endregion

        #region Initialization
        /// <summary>
        /// Constructor for Sprite Frame
        /// </summary>
        /// <param name="aFrameDimensions">the x offset, y offset, width and height of the frame</param>
        /// <param name="aFrameLengthSeconds">how long in seconds this frame should last</param>
        public SpriteFrame(Rectangle aFrameDimensions, float aFrameLengthSeconds)
        {
            this.SourceRectangle = aFrameDimensions;
            this._frameLength = aFrameLengthSeconds;

            this.FrameDrawOffset = new Vector2(SourceRectangle.Width / 2, SourceRectangle.Height / 2);
        }

        public SpriteFrame(Rectangle aFrameDimensions, float aFrameLengthSeconds, Vector2 aFrameDrawOffset)
            : this(aFrameDimensions, aFrameLengthSeconds)
        {
            this.FrameDrawOffset = aFrameDrawOffset;
        }

        /// <summary>
        /// Constructor for Sprite Frame
        /// </summary>
        /// <param name="aFrameDimensions">the x offset, y, offset, width and height of the frame</param>
        /// <param name="aFramesPerSecond">the speed at which the animation containing this frame runs in frames per second.  Ex. 30 </param>
        public SpriteFrame(Rectangle aFrameDimensions, int aFramesPerSecond)
        {
            this.SourceRectangle = aFrameDimensions;
            this._frameLength = 1f / aFramesPerSecond;

            this.FrameDrawOffset = new Vector2(SourceRectangle.Width / 2, SourceRectangle.Height / 2);
        }

        public SpriteFrame(Rectangle aFrameDimensions, int aFramesPerSecond, Vector2 aFrameDrawOffset)
            : this(aFrameDimensions, aFramesPerSecond)
        {
            this.FrameDrawOffset = aFrameDrawOffset;
        }

        /// <summary>
        /// Constructor for Sprite Frame
        /// </summary>
        /// <param name="aX">the x position of this animation's frame, relative to texture's leftmost pixel</param>
        /// <param name="aY">the y position of this animatoin's frame, relative to texture's topmost pixel</param>
        /// <param name="aWidth">the width of this animation frame's rectangle</param>
        /// <param name="aHeight">the height of this animation frame's rectangle</param>
        /// <param name="aFrameLengthSeconds">how long in seconds this frame should last</param>
        public SpriteFrame(int aX, int aY, int aWidth, int aHeight, float aFrameLengthSeconds)
        {
            this.SourceRectangle = new Rectangle(aX, aY, aWidth, aHeight);
            this._frameLength = aFrameLengthSeconds;

            this.FrameDrawOffset = new Vector2(SourceRectangle.Width / 2, SourceRectangle.Height / 2);
        }

        public SpriteFrame(int aX, int aY, int aWidth, int aHeight, float aFrameLengthSeconds, Vector2 aFrameDrawOffset)
            : this(aX, aY, aWidth, aHeight, aFrameLengthSeconds)
        {
            this.FrameDrawOffset = aFrameDrawOffset;
        }

        /// <summary>
        /// Constructor for Sprite Frame
        /// </summary>
        /// <param name="aX">the x position of this animation's frame, relative to texture's leftmost pixel</param>
        /// <param name="aY">the y position of this animatoin's frame, relative to texture's topmost pixel</param>
        /// <param name="aWidth">the width of this animation frame's rectangle</param>
        /// <param name="aHeight">the height of this animation frame's rectangle</param>
        /// <param name="aFrameLengthSeconds">the speed at which the animation containing this frame runs in frames per second.  Ex. 30</param>
        public SpriteFrame(int aX, int aY, int aWidth, int aHeight, int aFramesPerSecond)
        {
            this.SourceRectangle = new Rectangle(aX, aY, aWidth, aHeight);
            this._frameLength = 1f / aFramesPerSecond;

            this.FrameDrawOffset = new Vector2(SourceRectangle.Width / 2, SourceRectangle.Height / 2);
        }

        public SpriteFrame(int aX, int aY, int aWidth, int aHeight, int aFramesPerSecond, Vector2 aFrameDrawoffset)
            : this(aX, aY, aWidth, aHeight, aFramesPerSecond)
        {
            this.FrameDrawOffset = aFrameDrawoffset;
        }

        #endregion
    }
}
