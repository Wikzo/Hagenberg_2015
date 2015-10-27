using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Content;

namespace ComponentFramework
{
    public static class GlobalContent
    {
        public static SpriteFont GameFont;
        public static Texture2D BlankTexture;

        public static void LoadContent()
        {
            BlankTexture = new Texture2D(FrameworkServices.GraphicsDevice, 1, 1);
            BlankTexture.SetData<Color>(new Color[] { Color.White });
        }
    }
}
