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
    /// <summary>
    /// Used to draw basic shapes
    /// </summary>
    public static class PrimitiveBrush
    {
        public static void DrawLine(SpriteBatch spriteBatch, Color aColor, Vector2 aPoint1, Vector2 aPoint2, float aLayer)
        {
            DrawLine(spriteBatch, aColor, aPoint1, aPoint2, aLayer, 3);
        }

        public static void DrawLine(SpriteBatch spriteBatch, Color aColor, Vector2 aPoint1, Vector2 aPoint2, float aLayer, int lineWidth)
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
                                               lineWidth);

            Vector2 origin = new Vector2(DebugContent.FillTexture.Width / 2f, DebugContent.FillTexture.Height / 2f);

            spriteBatch.Draw(DebugContent.FillTexture, lineRect, null, aColor, rotation, origin, SpriteEffects.None, aLayer);
        }

        public static void DrawCircle(SpriteBatch spriteBatch, Color aColor, Vector2 aPosition, float aRadius, float layer)
        {
            double angleStep = 1f / aRadius;
            Vector2 previous = Vector2.Zero;
            Vector2 next;

            for (double angle = 0; angle < Math.PI * 2; angle += angleStep)
            {
                // Use the parametric definition of a circle: http://en.wikipedia.org/wiki/Circle#Cartesian_coordinates
                int x = (int)Math.Round(aPosition.X + (aRadius * Math.Cos(angle)));
                int y = (int)Math.Round(aPosition.Y + (aRadius * Math.Sin(angle)));

                next = new Vector2(x, y);

                if (previous != Vector2.Zero)
                {
                    DrawLine(spriteBatch, aColor, previous, next, layer);
                }
                previous = next;
            }
        }

        public static void DrawRectangle(SpriteBatch spriteBatch, Color aColor, Vector2 position, float width, float height, float layer)
        {
            Vector2 topLeft = position - new Vector2(width / 2, height / 2);
            Vector2 topRight = position + new Vector2(width / 2, -height/ 2);
            Vector2 bottomRight = position + new Vector2(width / 2, height / 2);
            Vector2 bottomLeft = position + new Vector2(-width / 2, height / 2);

            DrawRectangle(spriteBatch, aColor, topLeft, topRight, bottomRight, bottomLeft, layer);
        }

        public static void DrawRectangle(SpriteBatch spriteBatch, Color aColor, float x, float y, float width, float height, float layer)
        {
            DrawRectangle(spriteBatch, aColor, x, y, width, height, layer, 3);
        }

        public static void DrawRectangle(SpriteBatch spriteBatch, Color aColor, float x, float y, float width, float height, float layer, int lineWidth)
        {
            Vector2 topLeft = new Vector2(x, y);
            Vector2 topRight = new Vector2(x + width, y);
            Vector2 bottomRight = new Vector2(x + width, y + height);
            Vector2 bottomLeft = new Vector2(x, y + height);

            DrawRectangle(spriteBatch, aColor, topLeft, topRight, bottomRight, bottomLeft, layer);
        }

        public static void DrawRectangle(SpriteBatch spriteBatch, Color aColor, Vector2 topLeft, Vector2 topRight, Vector2 bottomRight, Vector2 bottomLeft, float layer)
        {
            DrawLine(spriteBatch, aColor, topLeft, topRight, layer);
            DrawLine(spriteBatch, aColor, topRight, bottomRight, layer);
            DrawLine(spriteBatch, aColor, bottomRight, bottomLeft, layer);
            DrawLine(spriteBatch, aColor, bottomLeft, topLeft, layer);
        }

        public static void DrawPolygon(SpriteBatch spriteBatch, Color lineColor, Color vertexColor, float lineLayer, float vertexLayer, List<Vector2> aVertices)
        {
            for (int i = 0; i < aVertices.Count; i++)
            {
                int nextItem = (i + 1) % aVertices.Count;
                DrawLine(spriteBatch, lineColor, aVertices[i], aVertices[nextItem], lineLayer);
                DrawPoint(spriteBatch, vertexColor, aVertices[i], vertexLayer);
            }
        }

        public static void DrawPoint(SpriteBatch spriteBatch, Color aColor, Vector2 point, float layer)
        {
            Vector2 origin = new Vector2(DebugContent.PathNodeTexture.Width / 2, DebugContent.PathNodeTexture.Height / 2);

            spriteBatch.Draw(DebugContent.PathNodeTexture, point, null, aColor, 0f, origin, Vector2.One * 0.5f, SpriteEffects.None, layer);
        }
    }
}
