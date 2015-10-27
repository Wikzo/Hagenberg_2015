using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Content;
using ComponentFramework.Debugging;
using Microsoft.Xna.Framework.Input;

namespace ComponentFramework
{
    /// <summary>
    /// Calls all update and draw methods of all managers present in the framework
    /// </summary>
    public static class FrameworkServices
    {
        #region Fields

        private static bool _isDebug = false;

        public static Random Random;

        private static Game _game;

        private static ContentManager _content;

        private static GraphicsDevice _graphicsDevice;

        private static SpriteBatch _spriteBatch;

        public static bool IsDebug
        {
            get { return _isDebug; }
            set
            {
                _isDebug = value;
            }
        }

        public static bool IsPauseGame = false;

        private static Camera2D _mainCamera;

        //FPS Counter Fields, from Sean Hargreaves' blog
        private static int _frameRate = 0;
        private static int _frameCounter = 0;
        private static TimeSpan elapsedTime = TimeSpan.Zero;

        #endregion

        public static string DebugInfo = String.Empty;

        #region Public Properties and Accessors

        #region Content Property

        /// <summary>
        /// The Content Manager used to load all entity's content
        /// </summary>
        public static ContentManager Content
        {
            get { return _content; }
        }

        /// <summary>
        /// Alternate getter for the content manager
        /// </summary>
        /// <returns>the content manager</returns>
        public static ContentManager GetContent()
        {
            return _content;
        }

        #endregion

        #region SpriteBatch Property

        /// <summary>
        /// The SpriteBatch used in drawing operations
        /// </summary>
        public static SpriteBatch SpriteBatch
        {
            get { return _spriteBatch; }
        }

        /// <summary>
        /// Alternate getter for the spritebatch
        /// </summary>
        /// <returns></returns>
        public static SpriteBatch GetSpriteBatch()
        {
            return _spriteBatch;
        }

        #endregion

        #region GraphicsDevice Property

        /// <summary>
        /// The Graphics device used  in the game
        /// </summary>
        public static GraphicsDevice GraphicsDevice
        {
            get { return _graphicsDevice; }
        }

        /// <summary>
        /// Alternate getter for the graphics device
        /// </summary>
        /// <returns></returns>
        public static GraphicsDevice GetGraphicsDevice()
        {
            return _graphicsDevice;
        }

        #endregion

        #region Game Property

        public static Game Game
        {
            get { return _game; }
        }

        #endregion

        #region Camera Property

        public static Camera2D MainCamera
        {
            get { return _mainCamera; }
        }

        #endregion

        #endregion

        #region Initialization

        public static void Initialize(Game aGame, GraphicsDevice aGraphicsDevice)
        {
            Random = new Random();

            _game = aGame;
            _graphicsDevice = aGraphicsDevice;
            _spriteBatch = new SpriteBatch(_graphicsDevice);
            _content = _game.Content;

            _mainCamera = new Camera2D(GraphicsDevice.Viewport, Vector2.Zero);

            InitializeManagers();

            DebugInfo = String.Format("FPS: {0}", _frameRate);
        }

        #endregion

        #region Update and Draw

        public static void Update(GameTime gameTime)
        {
            //Always update the time manager, input manager, screen manager, and console
            TimeManager.Update(gameTime);
            InputManager.Update();
            ScreenManager.Update();
            XConsole.Instance.Update();

            //if the game isn't paused, update the rest of the managers
            if (!IsPauseGame)
            {
                //Update all the timers in the timers list
                Timer.Update();

                for (int i = 0; i < GameObject.MasterList.Count; i++)
                {
                    GameObject go = GameObject.MasterList[i];
                    go.PerformUpdate();
                }
            }

            HandleDebugInput();

            UpdateFPSCounter();
        }

        public static void Draw()
        {
            DrawManager.Draw(_spriteBatch);
        }

        #endregion

        #region Private Methods

        private static void HandleDebugInput()
        {
            //check to see if the debug information should be drawn to the screen
            if (InputManager.WasKeyPressed(Keys.Home))
            {
                IsDebug = !IsDebug;
            }
        }

        private static void UpdateFPSCounter()
        {
            elapsedTime += TimeManager.ElapsedGameTime;

            if (elapsedTime > TimeSpan.FromSeconds(1))
            {
                elapsedTime -= TimeSpan.FromSeconds(1);
                _frameRate = _frameCounter;
                _frameCounter = 0;
            }
        }

        #endregion

        #region Public Methods

        public static void DrawDebugInfo(SpriteBatch spriteBatch)
        {
            //increment the frame counter here, otherwise the test gives inaccurate results
            _frameCounter++;

            if (DebugInfo != String.Empty)
            {
                //draw the FPS
                spriteBatch.DrawString(DebugContent.DebugFont, String.Format("FPS: {0}", _frameRate), new Vector2(MainCamera.Viewport.Width * 0.8f, 10), Color.White);
            }
        }

        #endregion

        #region Helper Methods

        private static void InitializeManagers()
        {
            TimeManager.Initialize();

            InputManager.Initialize();

            ScreenManager.Initialize();

            AudioManager.Initialize();

            DebugContent.LoadContent();

            GlobalContent.LoadContent();

            EntityManager.Initialize();

            ParticleManager.Initialize();

            XConsole.CreateConsole();
        }

        #endregion
    }
}