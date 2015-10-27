using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ComponentFramework.EntitySystem
{
    public class DrawableEntityComponent : UpdateableEntityComponent, IEntityDrawable
    {
        #region Fields

        private bool _visible = true;
        private int _drawOrder = 0;

        public event EventHandler<EventArgs> VisibleChanged;
        public event EventHandler<EventArgs> DrawOrderChanged;

        public Vector2 DrawOffset = Vector2.Zero;
        public Vector2 DrawPosition = Vector2.Zero;

        #endregion

        #region Public Properties and Accessors

        #region Visible Property

        /// <summary>
        /// Determines whether this component should be drawn or not.
        /// </summary>
        public bool Visible
        {
            get 
            {
                return _visible; 
            }
            set
            {
                if (_visible != value)
                {
                    _visible = value;
                    if (VisibleChanged != null)
                    {
                        VisibleChanged(this, EventArgs.Empty);
                    }
                }
            }
        }

        /// <summary>
        /// Alternate Getter for this component's visible property
        /// </summary>
        /// <returns>true if component is visible, false otherwise.</returns>
        public bool GetVisible()
        {
            return _visible;
        }

        /// <summary>
        /// Alternate setter for this component's visible property
        /// </summary>
        /// <param name="aIsVisible">true if the component should be visible, false otherwise.</param>
        public void SetVisible(bool aIsVisible)
        {
            if (_visible != aIsVisible)
            {
                _visible = aIsVisible;
                if (VisibleChanged != null)
                {
                    VisibleChanged(this, EventArgs.Empty);
                }
            }
        }

        #endregion

        #region Draw Order

        /// <summary>
        /// The order in which this component should be drawn, relative to other components in the parent entity.
        /// </summary>
        public int DrawOrder
        {
            get 
            { 
                return _drawOrder; 
            }
            set
            {
                if (_drawOrder != value)
                {
                    _drawOrder = value;
                    if (DrawOrderChanged != null)
                    {
                        DrawOrderChanged(this, EventArgs.Empty);
                    }
                }
            }
        }

        /// <summary>
        /// Alternate getter for this component's draw order
        /// </summary>
        /// <returns>the draw order as an integer.</returns>
        public int GetDrawOrder()
        {
            return _drawOrder;
        }

        /// <summary>
        /// Alternate setter for this component's draw order
        /// </summary>
        /// <param name="aDrawOrder">the order in which this component should be drawn.  Smaller numbers go first.</param>
        public void SetDrawOrder(int aDrawOrder)
        {
            if (_drawOrder != aDrawOrder)
            {
                _drawOrder = aDrawOrder;
                if (DrawOrderChanged != null)
                {
                    DrawOrderChanged(this, EventArgs.Empty);
                }
            }
        }

        #endregion

        #endregion

        #region Initialization

        /// <summary>
        /// Constructor for the drawable component.
        /// </summary>
        /// <param name="aParent">the parent entity.  Chains to base constructor</param>
        public DrawableEntityComponent(Entity aParent) : base (aParent)
        {
            VisibleChanged += OnVisibleChanged;
            DrawOrderChanged += OnDrawOrderChanged;
        }

        /// <summary>
        /// Initialize values here, calls LoadContent at the end.
        /// </summary>
        public override void Initialize() 
        {
            LoadContent();
        }

        /// <summary>
        /// Gather references to other components here, called after Initialize.
        /// </summary>
        public override void Start()
        {
            this.DrawPosition = this.Entity.Position - DrawOffset;
        }

        /// <summary>
        /// Load the content for this entity, use the Services content manager.
        /// </summary>
        protected virtual void LoadContent() { }

        /// <summary>
        /// Unload this entity's content.
        /// </summary>
        public virtual void UnloadContent() { }

        #endregion

        #region Update and Draw

        public override void Update()
        {
            this.DrawPosition = (Entity.Position - DrawOffset);
        }

        /// <summary>
        /// Used to draw the component.
        /// </summary>
        /// <param name="spriteBatch">Spritebatch to be used in drawing (from EntityManager)</param>
        /// <param name="gameTime">drawing timing values</param>
        public virtual void Draw(SpriteBatch spriteBatch) { }

        public virtual void DrawHUD(SpriteBatch spriteBatch) { }

        public virtual void DebugDraw(SpriteBatch spriteBatch) { }

        #endregion

        #region  Event Handler methods

        /// <summary>
        /// Called when the visible value is changed.
        /// </summary>
        /// <param name="sender">the component in question.</param>
        /// <param name="e">Empty Event Args</param>
        protected virtual void OnVisibleChanged(object sender, EventArgs e) { }

        /// <summary>
        /// Called when the draw order is changed
        /// </summary>
        /// <param name="sender">the component in question.</param>
        /// <param name="e">Empty Event Args</param>
        protected virtual void OnDrawOrderChanged(object sender, EventArgs e) { }

        #endregion
    }
}
