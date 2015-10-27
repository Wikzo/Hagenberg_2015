using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Content;
using ComponentFramework.EntitySystem;

namespace ComponentFramework
{
    /// <summary>
    /// Manages all the entities in the game
    /// </summary>
    public static class EntityManager
    {
        #region Fields

        private static GameObjectList _masterList = new GameObjectList();
        private static GameObjectList _currentList = new GameObjectList();

        private static bool _isInitialized = false;

        #endregion

        #region Properties

        public static GameObjectList MasterList
        {
            get { return _masterList; }
        }

        public static int Count
        {
            get { return _masterList.Count; }
        }

        #endregion

        #region Initialize

        /// <summary>
        /// Initializes all entities in the manager
        /// </summary>
        public static void Initialize()
        {
            _currentList.Clear();
            _currentList.AddRange(_masterList);

            for (int i = 0; i < _currentList.Count; i++)
            {
                Entity current = _currentList[i] as Entity;

                current.Initialize();
            }

            _isInitialized = true;
        }

        #endregion

        #region Update and Draw

        /// <summary>
        /// Updates All Entities in the manager
        /// </summary>
        /// <param name="gameTime">Timing Values</param>
        public static void Update()
        {
            _currentList.Clear();
            _currentList.AddRange(_masterList);

            for (int i = 0; i < _currentList.Count; i++)
            {
                _currentList[i].PerformUpdate();
            }
        }

        /// <summary>
        /// Draws all entities in the manager
        /// </summary>
        /// <param name="gameTime">Timing Values</param>
        public static void Draw(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _currentList.Count; i++)
            {
                _currentList[i].PerformDraw(spriteBatch);
            }
        }

        public static void DrawHUD(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _currentList.Count; i++)
            {
                _currentList[i].DrawHUD(spriteBatch);
            }
        }

        public static void DebugDraw(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _currentList.Count; i++)
            {
                _currentList[i].DebugDraw(spriteBatch);
            }
        }

        #endregion

        #region Public Methods

        /// <summary>
        /// Creates a New Entity.  Alternately, The entity could be created in the traditional way.
        /// </summary>
        /// <param name="aLayer">The Layer this entity is on.</param>
        /// <returns></returns>
        public static Entity CreateNew()
        {
            return new Entity();
        }

        public static Entity CreateNew(params GameObjectList[] ListsToAddTo)
        {
            Entity tempEntity = new Entity();

            for (int i = 0; i < ListsToAddTo.Length; i++)
            {
                //GameObject.AddToList(ListsToAddTo[i], tempEntity);
                ListsToAddTo[i].Add(tempEntity);
            }

            return tempEntity;
        }

        /// <summary>
        /// Adds an entity to the manager
        /// </summary>
        /// <param name="aEntity"></param>
        public static void AddEntity(Entity aEntity)
        {
            //add entity to the master list
            if (!_masterList.Contains(aEntity))
            {
                //TODO: Double check this, essentially in order to perform this operation I have to temporarily cast
                //the master list to a list of game objects
                //GameObject.AddToList(_masterList, aEntity);
                _masterList.Add(aEntity);
            }

            if (_isInitialized)
            {
                aEntity.Initialize();
            }
        }

        public static bool Contains(Entity aEntity)
        {
            return _masterList.Contains(aEntity);
        }

        public static void Remove(Entity aEntity)
        {
            _masterList.Remove(aEntity);
            _currentList.Remove(aEntity);
        }

        /// <summary>
        /// Clear all the lists in the manager.
        /// </summary>
        public static void Clear()
        {
            _masterList.Clear();
            _currentList.Clear();
        }

        #endregion
    }
}
