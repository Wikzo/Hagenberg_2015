using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNAEntityComponents.EntitySystem;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace XNAEntityComponents.Components
{
    class StageBackground : DrawableEntityComponent
    {
        public Texture2D BackgroundTexture;
        public Texture2D PositionTexture;

        public StageBackground(Entity aParent)
            : base(aParent)
        {
            this.Name = "StageBackground";
        }

        public override void Initialize()
        {
            this.Parent.Transform.Position = new Vector2(400, 200);

            base.Initialize();
        }

        public override void Start()
        {
            this.DrawOffset = new Vector2(BackgroundTexture.Width / 2, BackgroundTexture.Height / 2);

            base.Start();
        }

        protected override void LoadContent()
        {
            BackgroundTexture = EntityManager.Content.Load<Texture2D>("level");
            PositionTexture = EntityManager.Content.Load<Texture2D>("cursor");
        }

        public override void Draw(SpriteBatch spriteBatch, GameTime gameTime)
        {
            spriteBatch.Draw(BackgroundTexture, DrawPosition, Color.White);
            spriteBatch.Draw(PositionTexture, this.Parent.Transform.Position, Color.White);
        }
    }
}
