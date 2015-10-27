using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ComponentFramework
{
    public static class GlobalData
    {
        //drawing layers from highest to lowest
        public static float PathNodeTextLayer = 1f;
        public static float PathNodeMarkerLayer = 0.98f;
        public static float PathNodeNodeLayer = 0.96f;
        public static float PathLineLayer = 0.95f;
        public static float PathNodeParentLayer = 0.94f;
        public static float PathNodeLineLayer = 0.92f;

        //General Debug layer
        public static float DebugLayer = 0.9f; 

        //shape debug drawing layers
        public static float PolygonVertexLayer = 0.88f;
        public static float PolygonLineLayer = 0.86f;
        public static float LineShapeLayer = 0.86f;
        public static float CircleShapeLayer = 0.84f;
        public static float TriangleLineLayer = 0.82f;
    }
}