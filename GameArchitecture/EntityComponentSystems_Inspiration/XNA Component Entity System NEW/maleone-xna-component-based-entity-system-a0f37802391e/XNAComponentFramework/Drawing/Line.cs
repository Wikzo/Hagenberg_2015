using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework;
using ComponentFramework.Debugging;

namespace ComponentFramework.Drawing
{
    static class Line
    {
        public static void DrawLine(SpriteBatch spriteBatch, Color aColor, Vector2 aPoint1, Vector2 aPoint2, float aLayer)
        {
            Vector2 direction = aPoint1 - aPoint2;
            float lineLength = direction.Length();

            if (direction != Vector2.Zero)
            {
                direction.Normalize();
            }

            float rotation = (float)Math.Atan2(direction.Y, direction.X);

            Rectangle lineRect = new Rectangle((int)(aPoint1.X + aPoint2.X) / 2, 
                                               (int)(aPoint1.Y + aPoint2.Y) / 2, 
                                               (int)lineLength, 
                                               2);

            Vector2 origin = new Vector2(DebugContent.FillTexture.Width / 2f, DebugContent.FillTexture.Height / 2f);

            spriteBatch.Draw(DebugContent.FillTexture, lineRect, null, aColor, rotation, origin, SpriteEffects.None, aLayer);
        }
    }
}
