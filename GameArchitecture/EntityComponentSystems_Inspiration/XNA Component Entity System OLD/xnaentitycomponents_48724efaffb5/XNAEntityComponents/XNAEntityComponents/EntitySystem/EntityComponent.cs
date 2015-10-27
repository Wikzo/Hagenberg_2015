using System;
using System.Reflection;
using System.Collections.Generic;
using Microsoft.Xna.Framework;

namespace XNAEntityComponents.EntitySystem
{
    public class EntityComponent : IEntityComponent, IEntityUpdateable
    {
        #region Fields
        private string _name = String.Empty;
        private bool _enabled = true;
        private int _updateOrder = 0;

        private readonly Entity _parent;

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

        #region Name Getter and Setter

        /// <summary>
        /// Property for getting and setting whether this component's
        /// name.
        /// </summary>
        public string Name
        {
            get { return _name; }
            protected set { _name = value; }
        }

        /// <summary>
        /// Alternate getter for this component's name.
        /// </summary>
        /// <returns>this component's name</returns>
        public string GetName()
        {
            return _name;
        }

        /// <summary>
        /// Alternate setter for this component's name.
        /// </summary>
        /// <param name="aName">the name to set the component to</param>
        protected void SetName(string aName)
        {
            _name = aName;
        }

        #endregion

        #region Parent Entity getter

        /// <summary>
        /// The entity that contains this component
        /// </summary>
        public Entity Parent
        {
            get { return _parent; }
        }

        /// <summary>
        /// Alternate getter for this Component's parent entity.
        /// </summary>
        /// <returns></returns>
        public Entity GetParent()
        {
            return _parent;
        }

        #endregion

        #endregion

        #region Initialization

        /// <summary>
        /// Constructor for Entity Components
        /// </summary>
        /// <param name="aParent">The entity this component is put in to.</param>
        public EntityComponent(Entity aParent)
        {
            this._parent = aParent;

            //exposed events as virtual methods
            EnabledChanged += OnEnabledChanged;
            UpdateOrderChanged += OnUpdateOrderChanged;
        }

        /// <summary>
        /// Called at start, and whenever a component is added.
        /// </summary>
        public virtual void Initialize() { }

        /// <summary>
        /// Called after initialize.  Use to get references to other components in the parent entity.
        /// </summary>
        public virtual void Start() { }

        #endregion

        #region Update

        /// <summary>
        /// This component's update logic.
        /// </summary>
        /// <param name="gameTime"></param>
        public virtual void Update(GameTime gameTime) { }

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
