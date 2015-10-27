using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Content;

namespace XNAEntityComponents.EntitySystem
{
    public static class EntityManager
    {
        #region Fields

        //used for drawing
        private static ContentManager _content;
        private static GraphicsDevice _graphicsDevice;
        private static SpriteBatch _spriteBatch;

        public static List<Entity> MasterList = new List<Entity>();
        private static List<Entity> _currentList = new List<Entity>();

        //alternatively, you could story all entities in a single list to be drawn.
        private static List<Entity> _foregroundEntities = new List<Entity>();
        private static List<Entity> _midgroundEntities = new List<Entity>();
        private static List<Entity> _backgroundEntities = new List<Entity>();

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

        #endregion

        #region Initialize

        /// <summary>
        /// Sets up the Entity Manager by providing it with a Content Manager, Graphics Device, and SpriteBatch
        /// </summary>
        /// <param name="aContent">The Content Manager to be used in loading content</param>
        /// <param name="aGraphics">The Graphics Device Used</param>
        /// <param name="aSpriteBatch">The SpriteBatch used during drawing.
        ///                 Ensure that this SpriteBatch was created with the GraphicsDevice being passed in.</param>
        public static void SetupManager(ContentManager aContent, GraphicsDevice aGraphics, SpriteBatch aSpriteBatch)
        {
            if (_content == null)
            {
                _content = aContent;
            }
            if (_graphicsDevice == null)
            {
                _graphicsDevice = aGraphics;
            }

            if (_spriteBatch == null)
            {
                _spriteBatch = aSpriteBatch;
            }
        }

        /// <summary>
        /// Initializes all entities in the manager
        /// </summary>
        public static void Initialize()
        {
            _currentList.Clear();
            _currentList.AddRange(MasterList);

            for (int i = 0; i < _currentList.Count; i++)
            {
                _currentList[i].Initialize();
            }
        }

        #endregion

        #region Update and Draw

        /// <summary>
        /// Updates All Entities in the manager
        /// </summary>
        /// <param name="gameTime">Timing Values</param>
        public static void Update(GameTime gameTime)
        {
            _currentList.Clear();
            _currentList.AddRange(MasterList);

            for (int i = 0; i < _currentList.Count; i++)
            {
                _currentList[i].Update(gameTime);
            }
        }

        /// <summary>
        /// Draws all entities in the manager
        /// </summary>
        /// <param name="gameTime">Timing Values</param>
        public static void Draw(GameTime gameTime)
        {
            // draw in three passes, background (ground, scene), midground (actors), and foreground (HUD)
            DrawBackgroundEntities(SpriteBatch, gameTime);

            DrawMidgroundEntities(SpriteBatch, gameTime);

            DrawForegroundEntities(SpriteBatch, gameTime);
        }

        /// <summary>
        /// Draws the background Entities in the level.
        /// </summary>
        /// <param name="spriteBatch">Spritebatch to be used in drawing</param>
        /// <param name="gameTime">Timing values.</param>
        private static void DrawBackgroundEntities(SpriteBatch spriteBatch, GameTime gameTime)
        {
            _spriteBatch.Begin();
            for (int i = 0; i < _backgroundEntities.Count; i++)
            {
                _backgroundEntities[i].Draw(SpriteBatch, gameTime);
            }
            _spriteBatch.End();
        }

        /// <summary>
        /// Draws the midground entities in the level
        /// </summary>
        /// <param name="spriteBatch">Spritebatch to be used in drawing</param>
        /// <param name="gameTime">Timing values.</param>
        private static void DrawMidgroundEntities(SpriteBatch spriteBatch, GameTime gameTime)
        {
            _spriteBatch.Begin();
            for (int i = 0; i < _midgroundEntities.Count; i++)
            {
                _midgroundEntities[i].Draw(SpriteBatch, gameTime);
            }
            _spriteBatch.End();
        }

        /// <summary>
        /// Draws the foreground entites in the level
        /// </summary>
        /// <param name="spriteBatch">The SpriteBatch to be used in drawing</param>
        /// <param name="gameTime">Timing Values</param>
        private static void DrawForegroundEntities(SpriteBatch spriteBatch, GameTime gameTime)
        {
            _spriteBatch.Begin();

            for (int i = 0; i < _foregroundEntities.Count; i++)
            {
                _foregroundEntities[i].Draw(SpriteBatch, gameTime);
            }

            SpriteBatch.End();
        }

        #endregion

        #region Methods

        /// <summary>
        /// Creates a New Entity.  Alternately, The entity could be created in the traditional way.
        /// </summary>
        /// <param name="aLayer">The Layer this entity is on.</param>
        /// <returns></returns>
        public static Entity CreateNew(EntityLayer aLayer)
        {
            Entity tempEntity = new Entity(aLayer);

            return tempEntity;
        }

        /// <summary>
        /// Adds an entity to the manager
        /// </summary>
        /// <param name="aEntity"></param>
        public static void AddEntity(Entity aEntity)
        {
            //add entity to the master list
            MasterList.Add(aEntity);

            //add to the appropriate list
            switch (aEntity.Layer)
            {
                case EntityLayer.Foreground:
                    {
                        _foregroundEntities.Add(aEntity);
                    }
                    break;
                case EntityLayer.Midground:
                    {
                        _midgroundEntities.Add(aEntity);
                    }
                    break;
                case EntityLayer.Background:
                    {
                        _backgroundEntities.Add(aEntity);
                    }
                    break;
                default:
                    break;
            }
        }


        /// <summary>
        /// Clear all the lists in the manager.
        /// </summary>
        public static void Clear()
        {
            MasterList.Clear();
            _currentList.Clear();

            _foregroundEntities.Clear();
            _midgroundEntities.Clear();
            _backgroundEntities.Clear();
        }

        #endregion
    }
}
