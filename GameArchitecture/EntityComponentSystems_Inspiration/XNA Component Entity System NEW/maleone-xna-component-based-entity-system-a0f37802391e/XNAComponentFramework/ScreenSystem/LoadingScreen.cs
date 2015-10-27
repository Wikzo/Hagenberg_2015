using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using ComponentFramework;
using Microsoft.Xna.Framework.Graphics;

namespace ComponentFramework.ScreenSystem
{
    /// <summary>
    /// An example implementation of a loading screen, taken from the creator's club.
    /// </summary>
    public class LoadingScreen : GameScreen
    {
        #region Fields

        private bool _loadingIsSlow;
        private bool otherScreensAreGone;

        private GameScreen[] _screensToLoad;

        #endregion

        #region Initialization

        private LoadingScreen(bool aLoadingIsSlow, GameScreen[] aScreensToLoad)
        {
            this._loadingIsSlow = aLoadingIsSlow;
            this._screensToLoad = aScreensToLoad;

            IsSerializable = false;

            TransitionOnTime = TimeSpan.FromSeconds(0.5);
        }

        public static void Load(bool aLoadingIsSlow, params GameScreen[] aScreensToLoad)
        {
            foreach (GameScreen screen in ScreenManager.GetScreens())
            {
                screen.ExitScreen();
            }

            LoadingScreen loadingScreen = new LoadingScreen(aLoadingIsSlow, aScreensToLoad);

            ScreenManager.AddScreen(loadingScreen);
        }

        #endregion

        #region Update and Draw

        public override void Update(bool otherScreenHasFocus, bool coveredByOtherScreen)
        {
            base.Update(otherScreenHasFocus, coveredByOtherScreen);

            if (otherScreensAreGone)
            {
                ScreenManager.RemoveScreen(this);

                for (int i = 0; i < _screensToLoad.Length; i++)
                {
                    GameScreen screen = _screensToLoad[i];

                    if (screen != null)
                    {
                        ScreenManager.AddScreen(screen);
                    }
                }

                FrameworkServices.Game.ResetElapsedTime();
            }
        }

        public override void Draw(SpriteBatch spriteBatch)
        {
            // If we are the only active screen, that means all the previous screens
            // must have finished transitioning off. We check for this in the Draw
            // method, rather than in Update, because it isn't enough just for the
            // screens to be gone: in order for the transition to look good we must
            // have actually drawn a frame without them before we perform the load.
            if ((ScreenState == ScreenState.Active) &&
                (ScreenManager.GetScreens().Length == 1))
            {
                otherScreensAreGone = true;
            }

            // The gameplay screen takes a while to load, so we display a loading
            // message while that is going on, but the menus load very quickly, and
            // it would look silly if we flashed this up for just a fraction of a
            // second while returning from the game to the menus. This parameter
            // tells us how long the loading is going to take, so we know whether
            // to bother drawing the message.
            if (_loadingIsSlow)
            {
                SpriteFont font = ScreenManager.MenuFont;

                const string message = "Loading...";

                // Center the text in the viewport.
                Viewport viewport = FrameworkServices.GraphicsDevice.Viewport;
                Vector2 viewportSize = new Vector2(viewport.Width, viewport.Height);
                Vector2 textSize = font.MeasureString(message);
                Vector2 textPosition = (viewportSize - textSize) / 2;

                Color color = Color.White * TransitionAlpha;

                // Draw the text.
                spriteBatch.DrawString(font, message, textPosition, color);
            }
        }

        #endregion
    }
}
