using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework.EntitySystem;
using ComponentFramework;
using ComponentFramework.Drawing;

namespace ComponentFramework.Debugging
{
    public class TransformWidget : DrawableEntityComponent
    {
        #region Fields

        Texture2D _widgetTexture;

        #endregion

        #region Properties

        #endregion

        #region Initialization

        /*********************************************
         * The constructor for all entity components.
         * 
         * Make sure to set the name, which will be
         * used for accessing this component inside
         * the parent entity.
         ********************************************/
        public TransformWidget(Entity aParent)
            : base(aParent)
        {
            //set this name to whatever you would like it to be
            this.Name = "Transform Widget";
        }

        /*********************************************
         * Initialize values for this component here.
         * 
         * The LoadContent Method is called from base
         ********************************************/
        public override void Initialize()
        {
            base.Initialize();
        }

        protected override void LoadContent()
        {
            _widgetTexture = FileManager.Load<Texture2D>("Textures/Debug/widget_texture_128");
        }

        /*********************************************
         * Grab References to other components in the
         * parent entity here.
         * 
         * set up your draw offset here.
         * 
         * the draw position is calculated from the 
         * draw offset inside base as 
         * DrawPosition = parent position - DrawOffset
         ********************************************/
        public override void Start()
        {
            DrawOffset = new Vector2(_widgetTexture.Width / 2, _widgetTexture.Height / 2);

            base.Start();
        }

        #endregion

        /*********************************************
         * Specific Update Logic
         ********************************************/
        #region Update and Draw

        public override void Update()
        {
            base.Update();
        }

        public override void Draw(SpriteBatch spriteBatch)
        {
            if (FrameworkServices.IsDebug)
            {
                spriteBatch.Draw(_widgetTexture, Entity.Position, null, Color.White, 0f, DrawOffset, 1f, SpriteEffects.None, 0.999999f);
                PrimitiveBrush.DrawLine(spriteBatch, Color.Green, Entity.Position, Entity.Position + Entity.LocalDownVector * 50f, 1f);
                PrimitiveBrush.DrawLine(spriteBatch, Color.Red, Entity.Position, Entity.Position + Entity.LocalRightVector * 50f, 1f);
                PrimitiveBrush.DrawLine(spriteBatch, Color.Red, Entity.Position, Entity.Position + Entity.LocalLeftVector * 50f, 1f);
                PrimitiveBrush.DrawLine(spriteBatch, Color.Green, Entity.Position, Entity.Position + Entity.LocalUpVector * 50f, 1f);
                PrimitiveBrush.DrawLine(spriteBatch, Color.White, Entity.Position, Entity.Position + Entity.Velocity, 1f);
                PrimitiveBrush.DrawLine(spriteBatch, Color.Yellow, Entity.Position, Entity.Position + Entity.Acceleration, 1f);
            }
        }

        #endregion

        #region Methods

        #endregion
    }
}
