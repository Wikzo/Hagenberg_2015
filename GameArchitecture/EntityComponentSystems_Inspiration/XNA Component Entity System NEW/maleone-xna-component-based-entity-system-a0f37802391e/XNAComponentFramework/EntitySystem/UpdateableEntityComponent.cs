using System;
using System.Reflection;
using System.Collections.Generic;
using Microsoft.Xna.Framework;

namespace ComponentFramework.EntitySystem
{
    public class UpdateableEntityComponent : EntityComponent, IEntityUpdateable
    {
        #region Fields
        private bool _enabled = true;
        private int _updateOrder = 0;

        private readonly Entity _entity;

        public event EventHandler<EventArgs> EnabledChanged;
        public event EventHandler<EventArgs> UpdateOrderChanged;

        #endregion

        #region Public Properties and Accessors

        #region Enabled Getter and Setter
        /// <summary>
        /// Property for getting and setting whether this component
        /// should be updated.
        /// </summary>
        public bool Enabled
        {
            get
            {
                return _enabled;
            }
            set
            {
                if (_enabled != value)
                {
                    _enabled = value;
                    if (EnabledChanged != null)
                    {
                        EnabledChanged(this, EventArgs.Empty);
                    }
                }
            }
        }

        /// <summary>
        /// Alternate accessor for this component's enabled property.
        /// </summary>
        /// <returns>true if component is enabled, false otherwise.</returns>
        public bool GetEnabled()
        {
            return _enabled;
        }

        /// <summary>
        /// Alternate setter for this component's enabled property.
        /// </summary>
        /// <param name="aEnabled"></param>
        public void SetEnabled(bool aEnabled)
        {
            if (_enabled != aEnabled)
            {
                _enabled = aEnabled;
                if (EnabledChanged != null)
                {
                    EnabledChanged(this, EventArgs.Empty);
                }
            }
        }

        #endregion

        #region Update Order Getter and Setter

        /// <summary>
        /// Property used for getting and setting this component's
        /// Update Order.
        /// </summary>
        public int UpdateOrder
        {
            get
            {
                return _updateOrder;
            }
            set
            {
                if (_updateOrder != value)
                {
                    _updateOrder = value;
                    UpdateOrderChanged(this, EventArgs.Empty);
                }
            }
        }

        /// <summary>
        /// Alternate accessor for this component's update order.
        /// </summary>
        /// <returns>the update order of the component.</returns>
        public int GetUpdateOrder()
        {
            return _updateOrder;
        }

        /// <summary>
        /// Alternate setter for this component's update order
        /// </summary>
        /// <param name="aUpdateOrder">the update order of this component.  Lower Numbers get updated first.</param>
        public void SetUpdateOrder(int aUpdateOrder)
        {
            if (_updateOrder != aUpdateOrder)
            {
                _updateOrder = aUpdateOrder;
                UpdateOrderChanged(this, EventArgs.Empty);
            }
        }

        #endregion

        #region Parent Entity getter

        /// <summary>
        /// Alternate getter for this Component's parent entity.
        /// </summary>
        /// <returns></returns>
        public Entity GetParent()
        {
            return _entity;
        }

        #endregion

        #endregion

        #region Initialization

        /// <summary>
        /// Constructor for Entity Components
        /// </summary>
        /// <param name="aParent">The entity this component is put in to.</param>
        public UpdateableEntityComponent(Entity aParent) 
            : base(aParent)
        {
            //exposed events as virtual methods
            EnabledChanged += OnEnabledChanged;
            UpdateOrderChanged += OnUpdateOrderChanged;
        }

        ///// <summary>
        ///// Called at start, and whenever a component is added.
        ///// </summary>
        //public virtual void Initialize() { }

        ///// <summary>
        ///// Called after initialize.  Use to get references to other components in the parent entity.
        ///// </summary>
        //public virtual void Start() { }

        #endregion

        #region Update

        /// <summary>
        /// This component's update logic.
        /// </summary>
        /// <param name="gameTime"></param>
        public virtual void Update() { }

        #endregion

        #region Event Handler Methods

        /// <summary>
        /// Called when the enabled value is changed
        /// </summary>
        /// <param name="sender">the component in question</param>
        /// <param name="e">Empty Event Args</param>
        protected virtual void OnEnabledChanged(object sender, EventArgs e) { }

        /// <summary>
        /// called when the update order is changed
        /// </summary>
        /// <param name="sender">the component in question</param>
        /// <param name="e">Empty Event Args</param>
        protected virtual void OnUpdateOrderChanged(object sender, EventArgs e) { }

        #endregion
    }
}
