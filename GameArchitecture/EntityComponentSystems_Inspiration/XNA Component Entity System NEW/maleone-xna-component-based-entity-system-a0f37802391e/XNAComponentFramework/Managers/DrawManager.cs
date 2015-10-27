using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework.Debugging;

namespace ComponentFramework
{
    public static class DrawManager
    {
        public static GameObjectList NormalRenderList = new GameObjectList();
        public static GameObjectList UniqueRenderList = new GameObjectList();

        private static Comparison<GameObject> renderSort = new Comparison<GameObject>(CompareByDrawLayer);

        private static int CompareByDrawLayer(GameObject a, GameObject b)
        {
            return a.DrawLayer.CompareTo(b.DrawLayer);
        }

        public static void Draw(SpriteBatch spriteBatch)
        {
            //sort all game objects by their draw layer
            GameObject.MasterList.Sort(renderSort);

            spriteBatch.Begin(SpriteSortMode.FrontToBack, BlendState.AlphaBlend, null, null, null, null, FrameworkServices.MainCamera.Transform);

            //draw the screens in the screen Manager
            ScreenManager.Draw(spriteBatch);

            DrawWorld(spriteBatch);

            spriteBatch.End();

            //draw all items that require their own spritebatch call
            DrawUnique(spriteBatch);

            spriteBatch.Begin();

            DrawHUD(spriteBatch);

            ScreenManager.DrawHUD(spriteBatch);

            //put debug info here that should be drawn to the screen
            if (FrameworkServices.IsDebug)
            {
                ScreenManager.DebugDraw(spriteBatch);
                FrameworkServices.DrawDebugInfo(spriteBatch);
            }

            spriteBatch.End();

            //put debug info here that should be drawn in the game world.  This section can be chopped out of non development builds.
            if (FrameworkServices.IsDebug)
            {
                spriteBatch.Begin(SpriteSortMode.FrontToBack, BlendState.AlphaBlend, null, null, null, null, FrameworkServices.MainCamera.Transform);

                DebugDraw(FrameworkServices.SpriteBatch);

                spriteBatch.End();
            }

            XConsole.Instance.Draw(spriteBatch);
        }
        
        /// <summary>
        /// the main draw pump
        /// </summary>
        /// <param name="spriteBatch">the spritebatch used for drawing</param>
        private static void DrawWorld(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < NormalRenderList.Count; i++)
            {
                NormalRenderList[i].PerformDraw(spriteBatch);
            }
        }

        /// <summary>
        /// Used to draw items that require their own draw call.
        /// </summary>
        /// <param name="spriteBatch">the spritebatch used for drawing</param>
        private static void DrawUnique(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < UniqueRenderList.Count; i++)
            {
                GameObject current = UniqueRenderList[i];

                    spriteBatch.Begin(SpriteSortMode.FrontToBack, 
                        current.BlendState, 
                        current.SamplerState, 
                        current.DepthStencilState, 
                        current.RasterizerState, 
                        null, 
                        FrameworkServices.MainCamera.Transform);

                    current.PerformDraw(spriteBatch);

                    spriteBatch.End();
            }
        }

        private static void DrawHUD(SpriteBatch spriteBatch)
        {
            //draw all the game objects in existence
            for (int i = 0; i < GameObject.MasterList.Count; i++)
            {
                GameObject.MasterList[i].DrawHUD(spriteBatch);
            }
        }

        public static void DebugDraw(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < GameObject.MasterList.Count; i++)
            {
                GameObject.MasterList[i].DebugDraw(spriteBatch);
            }
        }
    }
}
