using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using ComponentFramework;

namespace ComponentFramework
{
    public static class Helper
    {
        public static float Epsilon = 0.0000001f;

        public static float ToDegrees(Vector2 aVector)
        {
            float radians = (float)Math.Atan2(aVector.Y, aVector.X);
            float degrees = MathHelper.ToDegrees(radians);

            if (degrees < 0)
                degrees += 360;

            return degrees;
        }

        public static double ToRadians(Vector2 aVector)
        {
            return Math.Atan2(aVector.Y, aVector.X);
        }

        public static Vector2 ZeroRotationVector
        {
            get { return new Vector2(1, 0); }
        }

        public static float DifferenceBetween(float a, float b)
        {
            return Math.Abs(a - b);
        }

        public static float AngleFromVector(Vector2 vector)
        {
            return (float)Math.Atan2(vector.Y, vector.X);
        }

        public static float AngleFromVectorInDegrees(Vector2 vector)
        {
            float angle = AngleFromVector(vector);

            if (angle < 0)
            {
                angle += 360;
            }

            return angle;
        }

        public static float SignedAngleBetweenVectors(Vector2 v1, Vector2 v2)
        {
            float perpDot = v1.X * v2.Y - v1.Y * v2.X;
            return (float)Math.Atan2(perpDot, Vector2.Dot(v1, v2));
        }

        public static Vector2 AngleToVector(float angle)
        {
            return new Vector2((float)Math.Cos(angle), (float)Math.Sin(angle));
        }

        public static Vector2 UnitVectorBetweenPoints(Vector2 start, Vector2 end)
        {
            Vector2 direction = end - start;

            if (direction != Vector2.Zero)
            {
                direction.Normalize();
            }

            return direction;
        }

        public static Vector2 GetWorldPosition(Vector2 position, Camera2D camera)
        {
            return Vector2.Transform(position, camera.InverseTransform);
        }

        /// <summary>
        /// Transforms a Vector2 by it's rotation around the origin
        /// </summary>
        /// <param name="aPoint">The point to be translated</param>
        /// <param name="aOrigin">the center point of the rotation</param>
        /// <param name="aRotation">the rotation</param>
        /// <returns></returns>
        public static Vector2 TransformVector2ByRotation(Vector2 aPoint, Vector2 aOrigin, float aRotation)
        {
            Vector2 result = new Vector2();
            result.X = (float)(aOrigin.X + (aPoint.X - aOrigin.X) * Math.Cos(aRotation)
                - (aPoint.Y - aOrigin.Y) * Math.Sin(aRotation));
            result.Y = (float)(aOrigin.Y + (aPoint.Y - aOrigin.Y) * Math.Cos(aRotation)
                + (aPoint.X - aOrigin.X) * Math.Sin(aRotation));
            return result;
        }

        public static Vector2 GetCenterOfPoints(List<Vector2> points)
        {
            double xPosition = 0.0;
            double yPosition = 0.0;

            for (int i = 0; i < points.Count; i++)
            {
                xPosition += points[i].X;
                yPosition += points[i].Y;
            }
            return new Vector2(((float)xPosition) / ((float)points.Count), 
                               ((float)yPosition) / ((float)points.Count));
        }

        public static Vector2 GetCenterOfPoints(Vector2[] points)
        {
            double xPosition = 0.0;
            double yPosition = 0.0;

            for (int i = 0; i < points.Length; i++)
            {
                xPosition += points[i].X;
                yPosition += points[i].Y;
            }
            return new Vector2(((float)xPosition) / ((float)points.Length),
                               ((float)yPosition) / ((float)points.Length));
        }

        public static Vector2 RotateVector2(Vector2 point, float radians, Vector2 pivot)
        {
            float cosRadians = (float)Math.Cos(radians);
            float sinRadians = (float)Math.Sin(radians);

            Vector2 translatedPoint = new Vector2();
            translatedPoint.X = point.X - pivot.X;
            translatedPoint.Y = point.Y - pivot.Y;

            Vector2 rotatedPoint = new Vector2();
            rotatedPoint.X = translatedPoint.X * cosRadians - translatedPoint.Y * sinRadians + pivot.X;
            rotatedPoint.Y = translatedPoint.X * sinRadians + translatedPoint.Y * cosRadians + pivot.Y;

            return rotatedPoint;
        }

        /// <summary>
        /// Rotates a Vector2 by some rotation
        /// </summary>
        /// <param name="point">The value of the unrotated point</param>
        /// <param name="radians">the rotation to be applied o the vector</param>
        /// <returns></returns>
        public static Vector2 RotateVector2(Vector2 point, float radians)
        {
            float cosRadians = (float)Math.Cos(radians);
            float sinRadians = (float)Math.Sin(radians);

            return new Vector2(
                point.X * cosRadians - point.Y * sinRadians,
                point.X * sinRadians + point.Y * cosRadians);
        }

        public static float RandomBetween(float min, float max)
        {
            return min + (float)FrameworkServices.Random.NextDouble() * (max - min);
        }

        public static Vector2 GetRandomDirection(float minRotation, float maxRotation)
        {
            float angle = RandomBetween(minRotation, maxRotation);
            return AngleToVector(angle);
        }
    }
}
