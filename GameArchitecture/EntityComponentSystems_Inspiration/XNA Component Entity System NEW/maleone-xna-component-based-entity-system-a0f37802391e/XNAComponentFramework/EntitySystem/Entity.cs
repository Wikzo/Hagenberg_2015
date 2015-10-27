using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Content;
using ComponentFramework;
using ComponentFramework.Debugging;

namespace ComponentFramework.EntitySystem
{
    public class Entity : GameObject
    {
        #region Fields

        /// <summary>
        /// Listing of all components in the entity, used for lookups.
        /// </summary>
        public Dictionary<string, IEntityComponent> Components = new Dictionary<string, IEntityComponent>();

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

        #endregion

        #region Properties and Accessors

        #region Count Property

        /// <summary>
        /// The amount of components this entity contains
        /// </summary>
        public int ComponentCount
        {
            get { return _components.Count; }
        }

        /// <summary>
        /// Alternate getter for this entity's component count
        /// </summary>
        /// <returns>the amount of components inside this entity.</returns>
        public int GetComponentCount()
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
        public Entity()
        {
            //zero out all values
            this.Position = Vector2.Zero;
            Rotation = 0f;
            Scale = Vector2.One;
            Velocity = Vector2.Zero;
            Acceleration = Vector2.Zero;

            _components.Clear();
            _updateableComponents.Clear();
            _drawableComponents.Clear();

            AddComponent(new TransformWidget(this));
            
            EntityManager.AddEntity(this);
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
        protected override void Update()
        {
            _tempUpdateableComponents.Clear();
            _tempUpdateableComponents.AddRange(_updateableComponents);

            for (int i = 0; i < _tempUpdateableComponents.Count; i++)
            {
                if (_tempUpdateableComponents[i].Enabled)
                {
                    _tempUpdateableComponents[i].Update();
                }
            }
        }

        /// <summary>
        /// Draws all drawable components in the entity
        /// </summary>
        /// <param name="spriteBatch">the spritebatch used for drawing</param>
        /// <param name="gameTime">timing values</param>
        protected override void Draw(SpriteBatch spriteBatch)
        {
            _tempDrawableComponents.Clear();
            _tempDrawableComponents.AddRange(_drawableComponents);

            for (int i = 0; i < _tempDrawableComponents.Count; i++)
            {
                if (_tempDrawableComponents[i].Visible)
                {
                    _tempDrawableComponents[i].Draw(spriteBatch);
                }
            }
        }

        public override void DrawHUD(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _tempDrawableComponents.Count; i++)
            {
                if (_tempDrawableComponents[i].Visible)
                {
                    _tempDrawableComponents[i].DrawHUD(spriteBatch);
                }
            }
        }

        public override void DebugDraw(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _tempDrawableComponents.Count; i++)
            {
                if (_tempDrawableComponents[i].Visible)
                {
                    _tempDrawableComponents[i].DebugDraw(spriteBatch);
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
        public void AddComponent(EntityComponent aComponent)
        {
            if (aComponent == null)
            {
                throw new ArgumentNullException("component");
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

        #region Public Methods

        /// <summary>
        /// Checks to see if this entity has the component in question
        /// </summary>
        /// <param name="aComponent">The component to check for</param>
        /// <returns>true if the entity has the component, false otherwise</returns>
        public bool HasComponent(IEntityComponent aComponent)
        {
            if (this.Components.ContainsKey(aComponent.Name))
            {
                return true;
            }

            return false;
        }

        /// <summary>
        /// Gets a component inside the entity based on that component's name property.
        /// </summary>
        /// <param name="aComponentName">The name of the desired component</param>
        /// <returns>the component as an entity component.</returns>
        public UpdateableEntityComponent GetComponent<T>(string aComponentName)
        {
            if (this.Components.ContainsKey(aComponentName))
            {
                return Components[aComponentName] as UpdateableEntityComponent;
            }
            else
            {
                throw new ArgumentOutOfRangeException(aComponentName);
            }
        }

        public void JumpTo(int X, int Y)
        {
            this.Position = new Vector2(X, Y);
        }

        public T GetComponent<T>() where T : UpdateableEntityComponent
        {
            for (int i = 0; i < _components.Count; i++)
            {
                if (_components[i].GetType() == typeof(T))
                {
                    return _components[i] as T;
                }
            }

            return null;
        }

        public override void Destroy()
        {
            //unload all the content for any drawable components
            for (int i = 0; i < _drawableComponents.Count; i++)
            {
                DrawableEntityComponent drawable = _drawableComponents[i] as DrawableEntityComponent;
                drawable.UnloadContent();
            }
            //remove all the components from this entity
            RemoveAllComponents();
            //remove from all the lists this entity belongs to
            base.Destroy();
        }

        public override void Reset()
        {
            this.Position = Vector2.Zero;
            this.Acceleration = Vector2.Zero;
            this.Velocity = Vector2.Zero;
            this.Scale = Vector2.Zero;
            this.Rotation = 0f;
            this.Drag = 0f;
        }

        public void ReplaceComponent(EntityComponent aComponentToAdd, EntityComponent aComponentToRemove)
        {
            RemoveComponent(aComponentToRemove);
            AddComponent(aComponentToAdd);
        }

        public void RemoveFromList(List<Entity> aListToRemoveFrom)
        {
            aListToRemoveFrom.Remove(this);
        }

        #endregion

        #region Helper Methods

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

        private void RemoveAllComponents()
        {
            for (int i = _components.Count - 1; i >= 0; i--)
            {
                RemoveComponent(_components[i]);
            }

            _components.Clear();
        }

        #endregion
    }
}