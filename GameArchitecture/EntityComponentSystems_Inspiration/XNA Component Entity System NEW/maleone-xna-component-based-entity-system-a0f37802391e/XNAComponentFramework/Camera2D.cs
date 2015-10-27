using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using ComponentFramework;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework.EntitySystem;

namespace ComponentFramework
{
    public class Camera2D : GameObject
    {
        #region Fields

        //The viewport we want the camera to use (holds dimensions and so on)
        public Viewport Viewport
        {
            get;
            private set;
        }

        //Copy of the old position when we start to shake
        public Vector2 SavedPosition
        {
            get;
            private set;
        }

        public Vector2 UpVector
        {
            get { return new Vector2((float)Math.Cos(Rotation), (float)Math.Sin(Rotation)); }
        }

        //center of focus for the camera
        public Vector2 FocusPoint
        {
            get;
            private set;
        }

        public Vector2 OffsetVector
        {
            get;
            set;
        }

        //The zoom scalar (1.0f = 100% zoom level)
        public float Zoom
        {
            get;
            set;
        }

        //Copy of the old rotation when we start to shake
        public float SavedRotation
        {
            get;
            private set;
        }

        //The amount to shake the camera in terms of position
        public float PositionShakeAmount
        {
            get;
            private set;
        }

        //The amount to shake the camera in terms of rotation
        public float RotationShakeAmount
        {
            get;
            private set;
        }

        //The maximum time the shake will last
        public float MaxShakeTime
        {
            get;
            private set;
        }

        //Our camera's transform matrix
        public Matrix Transform
        {
            get;
            private set;
        }

        public Matrix InverseTransform
        {
            get { return Matrix.Invert(Transform); }
        }

        public Matrix GetTransform()
        {
            return Transform;
        }

        //The source object to follow
        public GameObject Target
        {
            get;
            private set;
        }

        public float FollowOffset
        {
            get;
            private set;
        }

        //Used to matching the rotation of the object, or any value you wish
        public float SourceRotationOffset
        {
            get;
            private set;
        }

        TimeSpan shaketimer;

        #endregion

        #region Initialization

        /// <summary>
        /// Initialize a new Camera object
        /// </summary>
        /// <param name="view">The viewport we want the camera to use (holds dimensions and so on)</param>
        /// <param name="position">Where to point the center of the camera (0x0 will be the center of the viewport)</param>
        public Camera2D(Viewport view, Vector2 position)
        {
            Viewport = view;
            Position = position;
            Zoom = 1.0f;
            Rotation = 0;
            FocusPoint = new Vector2(view.Width / 2, view.Height / 2);
        }

        /// <summary>
        /// Initialize a new Camera object
        /// </summary>
        /// <param name="view">The viewport we want the camera to use (holds dimensions and so on)</param>
        /// <param name="position">Where to position our camera relative to the focus point</param>
        /// <param name="focus">Where to point the center of the camera (0x0 will be the center of the viewport)</param>
        /// <param name="zoom">How much we want the camera zoomed by default</param>
        /// <param name="rotation">How much we want the camera to be rotated by default</param>
        public Camera2D(Viewport view, Vector2 position, Vector2 focus, float zoom, float rotation)
        {
            Viewport = view;
            Position = position;
            Zoom = zoom;
            Rotation = rotation;
            FocusPoint = focus;
        }

        #endregion

        #region Update and Draw

        protected override void Update()
        {
            if (shaketimer.TotalSeconds > 0)
            {
                PerformShake();
            }

            /* Create a transform matrix through position, scale, rotation, and translation to the focus point
             * We use Math.Pow on the zoom to speed up or slow down the zoom.  Both X and Y will have the same zoom levels
             * so there will be no stretching.
             */
            Vector2 objectPosition;
            float objectRotation;
            float deltaRotation;

            if (Target != null)
            {
                objectPosition = Target.Position + (Target.LocalUpVector * FollowOffset) + OffsetVector;
                objectRotation = Target.Rotation;
                deltaRotation = SourceRotationOffset;
            }
            else
            {
                objectPosition = Position;
                objectRotation = Rotation;
                deltaRotation = 0.0f;
            }

            Transform = Matrix.CreateTranslation(new Vector3(-objectPosition, 0)) * 
                Matrix.CreateScale(new Vector3((float)Math.Pow(Zoom, 10), (float)Math.Pow(Zoom, 10), 1)) *
                Matrix.CreateRotationZ(-objectRotation + deltaRotation) *
                Matrix.CreateTranslation(new Vector3(FocusPoint.X, FocusPoint.Y, 0));

            //update the position values
            this.Position = objectPosition;
            this.Rotation = objectRotation + deltaRotation;
        }

        protected override void Draw(SpriteBatch spriteBatch) { }

        #endregion

        #region Public Methods

        /// <summary>
        /// Resets the camera to default values
        /// </summary>
        public override void Reset()
        {
            Position = Vector2.Zero;
            Rotation = 0;
            Zoom = 1.0f;
            shaketimer = TimeSpan.FromSeconds(0);
            Target = null;
        }

        /// <summary>
        /// Perform a camera shake
        /// </summary>
        /// <param name="shakeTime">The amount of time to shake the camera</param>
        /// <param name="min">The maximum amount to offset the camera</param>
        public void Shake(float shakeTime, float positionAmount, float rotationAmount)
        {
            //We only want to perform one shake.  If one is going on currently, we have to
            //wait for that shake to be over before we can do another one.
            if (shaketimer.TotalSeconds <= 0)
            {
                MaxShakeTime = shakeTime;
                shaketimer = TimeSpan.FromSeconds(MaxShakeTime);
                PositionShakeAmount = positionAmount;
                RotationShakeAmount = rotationAmount;

                SavedPosition = FocusPoint;
                SavedRotation = Rotation;
            }
        }

        /// <summary>
        /// Sets this Camera to follow a game object
        /// </summary>
        /// <param name="aTarget">The object to follow</param>
        /// <param name="aOffsetVector">The offset of this camera's position relative to the target's position</param>
        /// <param name="aFollowOffset">distance the camera should be from the target</param>
        /// <param name="rotationOffset">the difference in rotation between the target and camera</param>
        public void Follow(GameObject aTarget, Vector2 aOffsetVector, float aFollowOffset, float rotationOffset)
        {
            Target = aTarget;
            OffsetVector = aOffsetVector;
            FollowOffset = aFollowOffset;
            SourceRotationOffset = rotationOffset;
        }

        #endregion

        #region Private Methods

        private void PerformShake()
        {
            //Perform a camera shake

            /* We want to restore the saved positions and rotations
             * so we do not go far from the center point
             */ 
            FocusPoint = SavedPosition;
            Rotation = SavedRotation;

            /* We want to subtract the elapsed time with the shaketimer
             * If it is still above 0. we will perform the camera shake
             * Otherwise, we will be done for this loop and the next loop
             * the saved data will be restored and it will go in the main 'else'
             * block below
             */
            shaketimer = shaketimer.Subtract(TimeManager.ElapsedGameTime);
            if (shaketimer.TotalSeconds > 0)
            {
                FocusPoint += new Vector2((float)((FrameworkServices.Random.NextDouble() * 2) - 1) * PositionShakeAmount,
                    (float)((FrameworkServices.Random.NextDouble() * 2) - 1) * PositionShakeAmount);
                Rotation += (float)((FrameworkServices.Random.NextDouble() * 2) - 1) * RotationShakeAmount;
            }
        }

        #endregion
    }
}
