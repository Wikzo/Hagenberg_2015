using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Content;
using ComponentFramework.Debugging;
using Microsoft.Xna.Framework.Input;

namespace ComponentFramework.Managers
{
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

        private static Camera2D _camera;

        #endregion

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
            get { return _camera; }
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

            _camera = new Camera2D(GraphicsDevice.Viewport, Vector2.Zero);

            InitializeManagers();
        }

        #endregion

        #region Update and Draw

        public static void Update(GameTime gameTime)
        {
            TimeManager.Update(gameTime);

            InputManager.Update();

            ScreenManager.Update();

            AudioManager.Update();

            Timer.Update();

            EntityManager.Update();

            XConsole.Instance.Update();
        }

        public static void Draw()
        {
            _spriteBatch.Begin(SpriteSortMode.FrontToBack, null, null, null, null, null, _camera.Transform);

            ScreenManager.Draw(_spriteBatch);

            EntityManager.Draw(_spriteBatch);

            _spriteBatch.End();

            //debug drawing batch, draw relative to screen here
            _spriteBatch.Begin();

            ScreenManager.DrawHUD(_spriteBatch);

            EntityManager.DrawHUD(_spriteBatch);

            if (IsDebug)
            {
                //put debug drawing methods here
                EntityManager.DebugDraw(_spriteBatch);
                ScreenManager.DebugDraw(_spriteBatch);
            }

            _spriteBatch.End();

            XConsole.Instance.Draw(_spriteBatch);
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

            EntityManager.Initialize();

            XConsole.CreateConsole();
        }

        #endregion
    }
}