using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework;

namespace ComponentFramework.Debugging
{
    public static class Style
    {
        public static Color PathNodeColor = new Color(100, 200, 200);
        public static Color SelectionColor = new Color(144, 238, 144);
        public static Color PathLineColor = new Color(80, 80, 80);
        public static Color NodeClosedColor = new Color(255, 200, 160);
        public static Color PathColor = new Color(180, 180, 255);

        public static Color BrightText = new Color(255, 255, 255);
        public static Color DarkText = new Color(150, 150, 150);

        public static float LineLayer = 0f;
        public static float ParentLayer = 0.25f;
        public static float NodeLayer = 0.5f;
        public static float MarkerLayer = 0.75f;
        public static float TextLayer = 1f;

        public static SpriteFont FontLarge;
        public static SpriteFont FontSmall;

        public static Texture2D FillTexture;
        public static Texture2D PathNodeTexture;

        public static Texture2D MarkerTexture;
        public static Vector2 MarkerOrigin;

        public static Texture2D TailTexture;
        public static Vector2 TailOrigin;

        public static void LoadContent()
        {
            FontLarge = FileManager.Load<SpriteFont>("Fonts/debug_font_large");
            FontSmall = FileManager.Load<SpriteFont>("Fonts/debug_font_small");

            FillTexture = new Texture2D(FrameworkServices.GraphicsDevice, 1, 1);
            FillTexture.SetData<Color>(new Color[] {Color.White});

            PathNodeTexture = FileManager.Load<Texture2D>("Textures/Debug/circle16");

            MarkerTexture = FileManager.Load<Texture2D>("textures/Debug/ring24");
            MarkerOrigin = new Vector2(MarkerTexture.Width / 2, MarkerTexture.Height / 2);

            TailTexture = FileManager.Load<Texture2D>("textures/Debug/line22x3");
            TailOrigin = new Vector2(0, TailTexture.Height / 2);

        }
    }
}
