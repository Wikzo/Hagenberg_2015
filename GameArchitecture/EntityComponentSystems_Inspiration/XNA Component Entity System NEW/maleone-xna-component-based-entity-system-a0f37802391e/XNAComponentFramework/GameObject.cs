using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework;

namespace ComponentFramework
{
    /// <summary>
    /// base class for all run time objects.  Stores basic information like position, 
    /// scale, and other data common to most objects.
    /// </summary>
    public abstract class GameObject
    {
        public static GameObjectList MasterList = new GameObjectList();

        #region State Data

        /// <summary>
        /// determines if this object should be updated 
        /// </summary>
        public bool isActive = true;
        /// <summary>
        /// the system time at which this object was created
        /// </summary>
        protected double timeCreated = 0f;
        /// <summary>
        /// the amount of time this object has been alive for.
        /// </summary>
        protected double timeAlive = 0f;
        /// <summary>
        /// The position of this object in the world
        /// </summary>
        private Vector2 _position = Vector2.Zero;
        /// <summary>
        /// The Velocity at which this object is moving.  Applied to position each update
        /// </summary>
        public Vector2 Velocity = Vector2.Zero;
        /// <summary>
        /// the accelerationa applied to this object's velocity
        /// </summary>
        public Vector2 Acceleration = Vector2.Zero;
        /// <summary>
        /// damping applied to the velocity, acceleration calculation
        /// </summary>
        public float Drag = 2f;
        /// <summary>
        /// the rotation of this object in radians
        /// </summary>
        private float _rotation = 0f;
        /// <summary>
        /// the Scale of this object in X and Y
        /// </summary>
        public Vector2 Scale = Vector2.One;
        /// <summary>
        /// The maximum velocity at which this object can travel.
        /// Limits the magnitude of this object's velocity to be less than
        /// positive terminal velocity, and greater than negative terminal
        /// velocity.
        /// </summary>
        public Vector2 TerminalVelocity = new Vector2(10000, 10000);

        private List<List<GameObject>> _listsBelongingTo = new List<List<GameObject>>();

        #endregion

        #region Parenting Data

        /// <summary>
        /// the immediate parent of this game object. exists one level
        /// above in the parenting hierarchy.
        /// </summary>
        private GameObject _parent = null;
        /// <summary>
        /// the top most parent of this object, at the highest
        /// position in the parenting hierarchy.
        /// </summary>
        private GameObject _topParent = null;
        /// <summary>
        /// all the game objects that are children of this.
        /// </summary>
        protected GameObjectList _children = new GameObjectList();

        #endregion

        #region Drawing Data

        private bool _flipHorizontally;
        private bool _flipVertically;
        private bool _isUseSeparateDrawCall = false;
        private float _opacity = 1f;

        public bool IsUseSeparateDrawCall
        {
            get { return _isUseSeparateDrawCall; }
        }

        public Color Color = Color.White;
        public SpriteEffects SpriteEffects = SpriteEffects.None;

        public bool FlipHorizontally
        {
            get { return _flipHorizontally; }
            set
            {
                this._flipHorizontally = value;

                if (_flipHorizontally == true)
                {
                    this.SpriteEffects = SpriteEffects.FlipHorizontally;
                }
                else
                {
                    this.SpriteEffects = SpriteEffects.None;
                }
            }
        }

        public bool FlipVertically
        {
            get { return this._flipVertically; }
            set
            {
                this._flipVertically = true;

                if (_flipVertically == true)
                {
                    this.SpriteEffects = SpriteEffects.FlipVertically;
                }
                else
                {
                    this.SpriteEffects = SpriteEffects.None;
                }
            }
        }

        public float DrawLayer = 0f;
        public bool isVisible = true;

        //drawing data, can be overridden if necessary
        public BlendState BlendState = BlendState.AlphaBlend;
        public SamplerState SamplerState = null;
        public DepthStencilState DepthStencilState = null;
        public RasterizerState RasterizerState = null;

        #endregion

        #region Public Properties and Accessors

        #region Parent

        /// <summary>
        /// Accessor for this object's parent
        /// </summary>
        public GameObject Parent
        {
            get { return this.GetParent(); }
        }
        /// <summary>
        /// alternate accessor for this object's parent
        /// </summary>
        /// <returns></returns>
        public GameObject GetParent()
        {
            return this._parent;
        }

        #endregion

        #region Top Parent

        /// <summary>
        /// Public Property for this object's top parent.
        /// </summary>
        public GameObject TopParent
        {
            get { return this.GetTopParent(); }
        }
        /// <summary>
        /// Alternate accessor for this object's top parent.
        /// </summary>
        /// <returns></returns>
        public GameObject GetTopParent()
        {
            return this._topParent;
        }

        #endregion

        #region Local Direction Vectors

        /// <summary>
        /// A Vector pointing directly to the right in this object's local space.
        /// </summary>
        public Vector2 LocalRightVector
        {
            get { return new Vector2((float)Math.Cos(Rotation), (float)Math.Sin(Rotation)); }
        }

        /// <summary>
        /// a Vector pointing directly downward in this object's local space.
        /// </summary>
        public Vector2 LocalDownVector
        {
            get { return new Vector2((float)Math.Cos(Rotation + MathHelper.PiOver2), (float)Math.Sin(Rotation + MathHelper.PiOver2)); }
        }

        /// <summary>
        /// a Vector pointing directly to the left in this object's local space.
        /// </summary>
        public Vector2 LocalLeftVector
        {
            get { return new Vector2((float)Math.Cos(Rotation + MathHelper.Pi), (float)Math.Sin(Rotation + MathHelper.Pi)); }
        }

        /// <summary>
        /// a Vector pointing directly up in this object's local space.
        /// </summary>
        public Vector2 LocalUpVector
        {
            get { return new Vector2((float)Math.Cos(Rotation - (Math.PI / 2)), (float)Math.Sin(Rotation - (Math.PI / 2))); }
        }

        #endregion

        #region Position

        /// <summary>
        /// Property for this object's position in the world.
        /// </summary>
        public Vector2 Position
        {
            get { return this.GetPosition(); }
            set
            {
                this.Move(this._position - value);
            }
        }

        /// <summary>
        /// Alternate accessor for this object's position.
        /// </summary>
        /// <returns></returns>
        public Vector2 GetPosition()
        {
            return this._position;
        }

        /// <summary>
        /// Property for this object's X Position.
        /// </summary>
        public float X
        {
            get { return this.GetX(); }
            set
            {
                this.Move(new Vector2(value, this._position.Y));
            }
        }

        /// <summary>
        /// Alternate accessor for this object's X Position.
        /// </summary>
        /// <returns></returns>
        public float GetX()
        {
            return this._position.X;
        }

        /// <summary>
        /// Public Property for this object's Y Position.
        /// </summary>
        public float Y
        {
            get { return this.GetY(); }
            set
            {
                this.Move(new Vector2(this._position.X, value));
            }
        }

        /// <summary>
        /// Alternate Accessor for this object's Y Position
        /// </summary>
        /// <returns></returns>
        public float GetY()
        {
            return this._position.Y;
        }

        #endregion

        #region Rotation

        /// <summary>
        /// public property for this object's rotation
        /// </summary>
        public float Rotation
        {
            get { return this.GetRotation(); }
            set
            {
                this.Rotate(value);
            }
        }

        /// <summary>
        /// Alternate accessor for this object's rotation.
        /// </summary>
        /// <returns></returns>
        public float GetRotation()
        {
            return this._rotation;
        }

        #endregion

        #region Velocity

        /// <summary>
        /// This object's Velocity in X in world space.
        /// </summary>
        public float XVelocity
        {
            get { return this.GetXVelocity(); }
            set { this.Velocity.X = value; }
        }

        /// <summary>
        /// Alternate accessor for the XVelocity Property
        /// </summary>
        /// <returns></returns>
        public float GetXVelocity()
        {
            return this.Velocity.X;
        }

        /// <summary>
        /// Property for this object's velocity in Y in world space.
        /// </summary>
        public float YVelocity
        {
            get { return this.GetYVelocity(); }
            set { this.Velocity.Y = value; }
        }

        /// <summary>
        /// Alternate accessor for this object's Y Velocity
        /// </summary>
        /// <returns></returns>
        public float GetYVelocity()
        {
            return this.Velocity.Y;
        }

        #endregion

        #region Acceleration Properties

        /// <summary>
        /// This Object's Acceleration in X in world space.
        /// </summary>
        public float XAcceleration
        {
            get { return this.GetXAcceleration(); }
            set { this.Acceleration.X = value; }
        }

        /// <summary>
        /// Alternate accessor for this object's X Acceleration
        /// </summary>
        /// <returns></returns>
        public float GetXAcceleration()
        {
            return this.Acceleration.X;
        }

        /// <summary>
        /// This object's acceleration in Y in world space.
        /// </summary>
        public float YAcceleration
        {
            get { return this.GetYAcceleration(); }
            set { this.Acceleration.Y = value; }
        }

        /// <summary>
        /// Alternate Accessor for Y Acceleration
        /// </summary>
        /// <returns></returns>
        public float GetYAcceleration()
        {
            return this.Acceleration.Y;
        }

        #endregion

        #region Scale Properties

        /// <summary>
        /// The X Component of this object's scale
        /// </summary>
        public float XScale
        {
            get { return this.GetXScale(); }
            set { this.Scale.X = value; }
        }

        /// <summary>
        /// Alternate accessor for X Scale Property
        /// </summary>
        /// <returns></returns>
        public float GetXScale()
        {
            return this.Scale.X;
        }

        /// <summary>
        /// The Y Component of this object's scale.
        /// </summary>
        public float YScale
        {
            get { return this.GetYScale(); }
            set { this.Scale.Y = value; }
        }

        /// <summary>
        /// Alternate Accessor for this object's Y Scale
        /// </summary>
        /// <returns></returns>
        public float GetYScale()
        {
            return this.Scale.Y;
        }

        #endregion

        #region Life Time

        /// <summary>
        /// The amount of time in seconds this object has been alive for
        /// </summary>
        public double LifeTime
        {
            get { return this.GetLifeTime(); }
        }

        /// <summary>
        /// Alternate Accessor for LifeTime Property
        /// </summary>
        /// <returns>the amount of time this object has been alive</returns>
        public double GetLifeTime()
        {
            return this.timeAlive;
        }

        #endregion

        #region Opacity

        public float Opacity
        {
            get { return _opacity; }
            set
            {
                _opacity = value;

                for (int i = 0; i < _children.Count; i++)
                {
                    _children[i].Opacity = value;
                }
            }
        }

        #endregion

        #endregion

        #region Initialization

        /// <summary>
        /// Constructor for Game Objects.  Sets up the time created so the life
        /// time of the object can be calculated
        /// </summary>
        public GameObject()
            : this(false)
        {
        }

        /// <summary>
        /// Alternate constructor for game objects, used for items that want their own spriteBatch.Begin calls
        /// </summary>
        /// <param name="addToDrawManager">true if this item should be added to the basic draw pump, false otherwise.</param>
        public GameObject(bool addToDrawManager)
        {
            //set the time created
            timeCreated = TimeManager.CurrentTime;
            _listsBelongingTo.Clear();
            _children.Clear();

            //whether or not to add this item to the basic draw pump
            _isUseSeparateDrawCall = addToDrawManager;

            //add this item to the master list of all gameobjects
            //GameObject.AddToList(MasterList, this);
            GameObject.MasterList.Add(this);

            if (_isUseSeparateDrawCall)
            {
                //GameObject.AddToList(DrawManager.UniqueRenderList, this);
                DrawManager.UniqueRenderList.Add(this);
            }
            else
            {
                //GameObject.AddToList(DrawManager.NormalRenderList, this);
                DrawManager.NormalRenderList.Add(this);
            }
        }

        #endregion

        #region Update and Draw

        /// <summary>
        /// Master Update Call, updates the values for this object and calls the custom abstract Update Logic
        /// </summary>
        public void PerformUpdate()
        {
            // if this game object is active
            if (isActive)
            {
                //perform the custom update logic of the entity
                //this method must be overridden
                this.Update();

                //update time alive value

                //apply velocity and acceleration values to position
                this.Velocity += this.Acceleration * (float)TimeManager.SecondDifference;
                this.Velocity -= this.Velocity * Drag * (float)TimeManager.SecondDifference;
                //limit velocity to be within the acceptable range
                this.Velocity = Vector2.Clamp(this.Velocity, -TerminalVelocity, TerminalVelocity);
                //increment position by velocity calculation
                this.Position += Velocity;

                //increment the amount of time this object has been alive
                this.timeAlive += TimeManager.SecondDifference;
            }
        }

        /// <summary>
        /// Master Draw Method, calls the object's custom Draw Logic
        /// </summary>
        /// <param name="spriteBatch">the spritebatch used for drawing</param>
        public void PerformDraw(SpriteBatch spriteBatch)
        {
            //if this object is currently visible
            if (isVisible)
            {
                //perform the custom draw logic
                this.Draw(spriteBatch);
            }
        }

        //Update Method containing this entity's custom logic, must be overridden
        protected abstract void Update();
        //Draw method containing this entity's custom logic, must be overridden
        protected abstract void Draw(SpriteBatch spriteBatch);
        //DrawHUD method used to draw relative to the game window, rather than the camera
        public virtual void DrawHUD(SpriteBatch spriteBatch) { }
        //DebugDraw method used to draw out debuginfo for the entity. 
        public virtual void DebugDraw(SpriteBatch spriteBatch) { }

        #endregion

        #region Public Methods

        /// <summary>
        /// Sets the Parent of this object to the given argument
        /// </summary>
        /// <param name="aParent">The parent of this object</param>
        public void SetParent(GameObject aParent)
        {
            if (this._parent != null)
            {
                this._parent._children.Remove(this);
            }

            this._parent = aParent;
            if (_parent != null)
            {
                _parent._children.Add(this);
            }

            this._topParent = GetHighestParent();
        }

        /// <summary>
        /// Gets the Highest Parent in this object's hierarchy
        /// </summary>
        /// <returns>returns the highest parent, and if it has none returns the entity itself</returns>
        public GameObject GetHighestParent()
        {
            GameObject current = this;
            GameObject parent;

            do
            {
                parent = current.Parent;

                if (parent == null)
                {
                    return current;
                }
                else
                {
                    current = parent;
                }

            } while (parent != null);
            //if the target has no parent, return it as the highest thing in the hierarchy

            return null;
        }

        /// <summary>
        /// Method that can be overriden if the object needs to have all values reset to a standard
        /// </summary>
        public virtual void Reset() { }

        /// <summary>
        /// Generic method for destroying game objects
        /// </summary>
        public virtual void Destroy()
        {
            this.RemoveFromListsBelongingTo();

            for (int i = 0; i < _children.Count; i++)
            {
                _children[i].Destroy();
            }
        }

        #endregion

        #region Private Methods

        /// <summary>
        /// Used to adjust the position of this object and all 
        /// it's children
        /// </summary>
        /// <param name="aAmount">The amount to move moved</param>
        private void Move(Vector2 aAmount)
        {
            //TODO: Solve Parenting issue where children do not retain their relative positions
            this._position -= aAmount;
            for (int i = 0; i < _children.Count; i++)
            {
                _children[i].Move(aAmount);
            }
        }

        /// <summary>
        /// Used to Adjust the rotation of this object and all
        /// it's children.
        /// </summary>
        /// <param name="aAmount"></param>
        private void Rotate(float aAmount)
        {
            this._rotation = aAmount;

            this._rotation %= MathHelper.TwoPi;
            for (int i = 0; i < _children.Count; i++)
            {
                _children[i].Rotate(aAmount);
            }
        }

        private void RemoveFromListsBelongingTo()
        {
            for (int i = _listsBelongingTo.Count - 1; i >= 0; i--)
            {
                GameObject.RemoveFromList(_listsBelongingTo[i], this);
            }
        }

        #endregion

        #region Static methods

        /// <summary>
        /// Establishes a two way relationship between the list the game object is being added to,
        /// and the object being added to the list
        /// </summary>
        /// <param name="list">The list to add the game object to</param>
        /// <param name="gameObject">the object to add</param>
        protected internal static void AddToList(List<GameObject> list, GameObject gameObject)
        {
            ///the two checks ensure an object is only ever added to a list once,
            ///and that the lists belonging to only every contains one instance of the list
            if (!list.Contains(gameObject))
            {
                //add the entity to the list
                list.Add(gameObject);
            }

            if (!gameObject._listsBelongingTo.Contains(list))
            {
                //add the list to the collection of lists the gameobject belongs to
                gameObject._listsBelongingTo.Add(list);
            }
        }

        /// <summary>
        /// Destroys the two way relationship between the object and the list.
        /// </summary>
        /// <param name="list">the list to remove the game object from</param>
        /// <param name="gameObject">the game object to remove</param>
        /// <returns>true if the removal was successful, false otherwise</returns>
        protected internal static bool RemoveFromList(List<GameObject> list, GameObject gameObject)
        {
            //remove the game object from the list only if that list contains the object,
            //and that objects lists belonging to field contains the list
            if (gameObject._listsBelongingTo.Contains(list) && list.Contains(gameObject))
            {
                list.Remove(gameObject);
                gameObject._listsBelongingTo.Remove(list);

                //return true, as the removal was successful
                return true;
            }

            //return false if there was no removal
            return false;
        }

        #endregion
    }
}
