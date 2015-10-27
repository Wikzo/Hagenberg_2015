using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Content;
using XNAEntityComponents.Components;


namespace XNAEntityComponents.EntitySystem
{
    /// <summary>
    /// Determines which layer the entity is in.  This may not be necessary for your project and can be removed if not needed.
    /// </summary>
    public enum EntityLayer
    {
        Foreground,
        Midground,
        Background
    }

    public class Entity
    {
        #region Fields

        //lists used for storing components, updateable componenets, and drawable components
        private List<IEntityComponent> _components = new List<IEntityComponent>();
        private List<IEntityUpdateable> _updateableComponents = new List<IEntityUpdateable>();
        private List<IEntityDrawable> _drawableComponents = new List<IEntityDrawable>();

        //lists the updating and drawing are performed on
        private List<IEntityComponent> _tempComponents = new List<IEntityComponent>();
        private List<IEntityUpdateable> _tempUpdateableComponents = new List<IEntityUpdateable>();
        private List<IEntityDrawable> _tempDrawableComponents = new List<IEntityDrawable>();

        //whether or not the game has been initialized
        private bool _isInitialized = false;

        /// <summary>
        /// Listing of all components in the entity, used for lookups.
        /// </summary>
        public Dictionary<string, IEntityComponent> Components = new Dictionary<string, IEntityComponent>();

        //this entity's layer.  Again, not absolutely necessary.
        private EntityLayer _layer;

        //All entities are given a transform component, which retains the position, rotation and scale.
        //Note that this implementation is for a 2D game, so for 3D you can either create a different
        //component or throw this out entirely.
        public TransformComponent Transform;

        #endregion

        #region Properties and Accessors

        #region Layer Property

        /// <summary>
        /// Getter for this entity's layer.
        /// </summary>
        public EntityLayer Layer
        {
            get { return _layer; }
        }

        /// <summary>
        /// Alternate getter for this entity's layer.
        /// </summary>
        /// <returns>the layer the entity is on</returns>
        public EntityLayer GetLayer()
        {
            return _layer;
        }
        #endregion

        #region Count Property

        /// <summary>
        /// The amount of components this entity contains
        /// </summary>
        public int Count
        {
            get { return _components.Count; }
        }

        /// <summary>
        /// Alternate getter for this entity's component count
        /// </summary>
        /// <returns>the amount of components inside this entity.</returns>
        public int GetCount()
        {
            return _components.Count;
        }

        #endregion

        #endregion

        #region Initialization

        /// <summary>
        /// Constructor for entity
        /// </summary>
        /// <param name="aLayer">Which layer this entity belongs to.</param>
        public Entity(EntityLayer aLayer)
        {
            this._layer = aLayer;

            //always give the entity a transform
            Transform = new TransformComponent(this);
        }

        /// <summary>
        /// Initialize method for this entity
        /// </summary>
        public void Initialize()
        {
            if (_isInitialized)
            {
                return;
            }

            _tempComponents.Clear();
            _tempComponents.AddRange(_components);

            for (int i = 0; i < _tempComponents.Count; i++)
            {
                _tempComponents[i].Initialize();
            }

            for (int i = 0; i < _tempComponents.Count; i++)
            {
                _tempComponents[i].Start();
            }

            _isInitialized = true;
        }

        #endregion

        #region Update and Draw

        /// <summary>
        /// Update all updateable components in the entity
        /// </summary>
        /// <param name="gameTime">Timing Values</param>
        public void Update(GameTime gameTime)
        {
            _tempUpdateableComponents.Clear();
            _tempUpdateableComponents.AddRange(_updateableComponents);

            for (int i = 0; i < _tempUpdateableComponents.Count; i++)
            {
                if (_tempUpdateableComponents[i].Enabled)
                {
                    _tempUpdateableComponents[i].Update(gameTime);
                }
            }
        }

        /// <summary>
        /// Draws all drawable components in the entity
        /// </summary>
        /// <param name="spriteBatch">the spritebatch used for drawing</param>
        /// <param name="gameTime">timing values</param>
        public void Draw(SpriteBatch spriteBatch, GameTime gameTime)
        {
            _tempDrawableComponents.Clear();
            _tempDrawableComponents.AddRange(_drawableComponents);

            for (int i = 0; i < _tempDrawableComponents.Count; i++)
            {
                if (_tempDrawableComponents[i].Visible)
                {
                    _tempDrawableComponents[i].Draw(spriteBatch, gameTime);
                }
            }
        }

        #endregion

        #region Adding and Removing Components

        /// <summary>
        /// Adds a component to this entity.  Only allows one component of each type
        /// to be in a single entity.
        /// </summary>
        /// <param name="aComponent">The component to add</param>
        public void AddComponent(IEntityComponent aComponent)
        {
            if (aComponent == null)
            {
                throw new ArgumentNullException("component is null");
            }

            //if you would like more than one of any type of component into this entity,
            //remove this if block.
            if (_components.Contains(aComponent))
            {
                return;
            }

            //add to master and lookup list
            _components.Add(aComponent);
            Components.Add(aComponent.Name, aComponent);

            IEntityUpdateable updateable = aComponent as IEntityUpdateable;
            IEntityDrawable drawable = aComponent as IEntityDrawable;

            //if the component can be updated, add it to that list
            if (updateable != null)
            {
                _updateableComponents.Add(updateable);
                updateable.UpdateOrderChanged += OnComponentUpdateOrderChanged;
                OnComponentUpdateOrderChanged(this, EventArgs.Empty);
            }

            //if the component can be draw, add it to that list
            if (drawable != null)
            {
                _drawableComponents.Add(drawable);
                drawable.DrawOrderChanged += OnComponentDrawOrderChanged;
                OnComponentDrawOrderChanged(this, EventArgs.Empty);
            }

            //if the entity has already initialized, call this item's initialize and start methods
            if (_isInitialized)
            {
                aComponent.Initialize();

                aComponent.Start();
            }
        }

        /// <summary>
        /// Removes a component from the entity
        /// </summary>
        /// <param name="aComponent">The component to remove</param>
        /// <returns>true if a component was removed, false otherwise</returns>
        public bool RemoveComponent(IEntityComponent aComponent)
        {
            if (aComponent == null)
            {
                throw new ArgumentNullException("component was null");
            }

            if (_components.Remove(aComponent))
            {
                IEntityUpdateable updateable = aComponent as IEntityUpdateable;
                IEntityDrawable drawable = aComponent as IEntityDrawable;

                //if the component was updateable, remove it from that list
                if (updateable != null)
                {
                    _updateableComponents.Remove(updateable);
                    updateable.UpdateOrderChanged -= OnComponentUpdateOrderChanged;
                }

                //if the component was drawable, remove it from that list
                if (drawable != null)
                {
                    _drawableComponents.Remove(drawable);
                    drawable.DrawOrderChanged -= OnComponentDrawOrderChanged;
                }

                return true;
            }

            return false;
        }

        #endregion

        #region Helper Methods

        /// <summary>
        /// Gets a component inside the entity based on that component's name property.
        /// </summary>
        /// <param name="aComponentName">The name of the desired component</param>
        /// <returns>the component as an entity component.</returns>
        public EntityComponent GetComponent(string aComponentName)
        {
            if (this.Components.ContainsKey(aComponentName))
            {
                return Components[aComponentName] as EntityComponent;
            }
            else
            {
                throw new ArgumentOutOfRangeException(aComponentName);
            }
        }

        /// <summary>
        /// what to do when the component's update order is changed.
        /// </summary>
        /// <param name="sender">the component in question</param>
        /// <param name="e">Empty Event Args</param>
        void OnComponentUpdateOrderChanged(object sender, EventArgs e)
        {
            _updateableComponents.Sort(UpdateableSort);
        }

        /// <summary>
        /// What to do when the component's draw order is changed.
        /// </summary>
        /// <param name="sender">the component in question</param>
        /// <param name="e"></param>
        void OnComponentDrawOrderChanged(object sender, EventArgs e)
        {
            _drawableComponents.Sort(DrawableSort);
        }

        /// <summary>
        /// Helper used in the sorting process
        /// </summary>
        /// <param name="aEntityA">first entity in question</param>
        /// <param name="aEntityB">second entity in question</param>
        /// <returns>the component's update order</returns>
        private static int UpdateableSort(IEntityUpdateable aEntityA, IEntityUpdateable aEntityB)
        {
            return aEntityA.UpdateOrder.CompareTo(aEntityB.UpdateOrder);
        }

        /// <summary>
        /// Helper used in the sorting process
        /// </summary>
        /// <param name="aEntityA">first entity in question</param>
        /// <param name="aEntityB">second entity in question</param>
        /// <returns>the component's update order</returns>
        private static int DrawableSort(IEntityDrawable aEntityA, IEntityDrawable aEntityB)
        {
            return aEntityA.DrawOrder.CompareTo(aEntityB.DrawOrder);
        }

        #endregion
    }
}