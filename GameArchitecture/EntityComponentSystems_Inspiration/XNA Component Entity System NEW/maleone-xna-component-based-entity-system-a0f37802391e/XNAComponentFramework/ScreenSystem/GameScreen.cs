using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using ComponentFramework;
using System.IO;
using Microsoft.Xna.Framework.Graphics;

namespace ComponentFramework.ScreenSystem
{
    public enum ScreenState
    {
        TransitionOn,
        Active,
        TransitionOff,
        Hidden
    }

    public abstract class GameScreen
    {
        #region Fields

        private bool _isPopup = false;
        private TimeSpan _transitionOnTime = TimeSpan.Zero;
        private TimeSpan _transitionOffTime = TimeSpan.Zero;
        private float _transitionPosition = 1f;
        private ScreenState _screenState = ScreenState.TransitionOn;
        private bool _isExiting = false;
        private bool _otherScreenHasFocus;
        private bool _isSerializable;

        #endregion

        #region Public Properties

        public bool isPopup
        {
            get { return _isPopup; }
            protected set { _isPopup = value; }
        }

        public TimeSpan TransitionOnTime
        {
            get { return _transitionOnTime; }
            protected set { _transitionOnTime = value; }
        }

        public TimeSpan TransitionOffTime
        {
            get { return _transitionOffTime; }
            protected set { _transitionOffTime = value; }
        }

        public float TransitionPosition
        {
            get { return _transitionPosition; }
            protected set { _transitionPosition = value; }
        }

        public float TransitionAlpha
        {
            get { return 1f - TransitionPosition; }
        }

        public ScreenState ScreenState
        {
            get { return _screenState; }
            protected set { _screenState = value; }
        }

        public bool IsExiting
        {
            get { return _isExiting; }
            protected internal set { _isExiting = value; }
        }

        public bool isActive
        {
            get
            {
                return !_otherScreenHasFocus && (_screenState == ScreenState.TransitionOn || _screenState == ScreenState.Active);
            }
        }

        public bool IsSerializable
        {
            get { return _isSerializable; }
            protected set { _isSerializable = value; }
        }

        #endregion

        #region Initialization

        public virtual void LoadContent() { }

        public virtual void UnloadContent() { }

        #endregion

        #region Update and Draw

        public virtual void Update(bool otherScreenHasFocus,
                                              bool coveredByOtherScreen)
        {
            this._otherScreenHasFocus = otherScreenHasFocus;

            if (_isExiting)
            {
                // If the screen is going away to die, it should transition off.
                _screenState = ScreenState.TransitionOff;

                if (!UpdateTransition(_transitionOffTime, 1))
                {
                    // When the transition finishes, remove the screen.
                    ScreenManager.RemoveScreen(this);
                }
            }
            else if (coveredByOtherScreen)
            {
                // If the screen is covered by another, it should transition off.
                if (UpdateTransition(_transitionOffTime, 1))
                {
                    // Still busy transitioning.
                    _screenState = ScreenState.TransitionOff;
                }
                else
                {
                    // Transition finished!
                    _screenState = ScreenState.Hidden;
                }
            }
            else
            {
                // Otherwise the screen should transition on and become active.
                if (UpdateTransition(_transitionOnTime, -1))
                {
                    // Still busy transitioning.
                    _screenState = ScreenState.TransitionOn;
                }
                else
                {
                    // Transition finished!
                    _screenState = ScreenState.Active;
                }
            }
        }

        public virtual void Draw(SpriteBatch spriteBatch) { }

        public virtual void DrawHUD(SpriteBatch spriteBatch) { }

        public virtual void DebugDraw(SpriteBatch spriteBatch) { }

        #endregion

        #region Helper Methods

        /// <summary>
        /// Helper for updating the screen transition position.
        /// </summary>
        bool UpdateTransition(TimeSpan time, int direction)
        {
            // How much should we move by?
            float transitionDelta;

            if (time == TimeSpan.Zero)
                transitionDelta = 1;
            else
                transitionDelta = (float)(TimeManager.ElapsedGameTime.TotalMilliseconds /
                                          time.TotalMilliseconds);

            // Update the transition position.
            _transitionPosition += transitionDelta * direction;

            // Did we reach the end of the transition?
            if (((direction < 0) && (_transitionPosition <= 0)) ||
                ((direction > 0) && (_transitionPosition >= 1)))
            {
                _transitionPosition = MathHelper.Clamp(_transitionPosition, 0, 1);
                return false;
            }

            // Otherwise we are still busy transitioning.
            return true;
        }

        public virtual void HandleInput() { }

        /// <summary>
        /// Tells the screen to serialize its state into the given stream.
        /// </summary>
        public virtual void Serialize(Stream stream) { }

        /// <summary>
        /// Tells the screen to deserialize its state from the given stream.
        /// </summary>
        public virtual void Deserialize(Stream stream) { }

        /// <summary>
        /// Tells the screen to go away. Unlike ScreenManager.RemoveScreen, which
        /// instantly kills the screen, this method respects the transition timings
        /// and will give the screen a chance to gradually transition off.
        /// </summary>
        public virtual void ExitScreen()
        {
            if (TransitionOffTime == TimeSpan.Zero)
            {
                // If the screen has a zero transition time, remove it immediately.
                ScreenManager.RemoveScreen(this);
            }
            else
            {
                // Otherwise flag that it should transition off and then exit.
                _isExiting = true;
            }
        }

        #endregion
    }
}
