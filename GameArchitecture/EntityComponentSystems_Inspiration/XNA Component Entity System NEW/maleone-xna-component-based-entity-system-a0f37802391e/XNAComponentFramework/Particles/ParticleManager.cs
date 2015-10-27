using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework.Particles;

namespace ComponentFramework
{
    public static class ParticleManager
    {
        private static GameObjectList _masterList = new GameObjectList();
        private static GameObjectList _particleSystems = new GameObjectList();

        public static void Initialize()
        {
            _masterList.Clear();
            _particleSystems.Clear();
        }

        public static void Update()
        {
            _particleSystems.Clear();

            for (int i = 0; i < _masterList.Count; i++)
            {
                _particleSystems.Add(_masterList[i]);
            }

            //perform the update on the particle system
            for (int i = 0; i < _particleSystems.Count; i++)
            {
                _particleSystems[i].PerformUpdate();
            }
        }

        public static void Draw(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _particleSystems.Count; i++)
            {
                GameObject system = _particleSystems[i];

                spriteBatch.Begin(SpriteSortMode.FrontToBack, system.BlendState, 
                    null, DepthStencilState.Default, null, null, FrameworkServices.MainCamera.GetTransform());

                system.PerformDraw(spriteBatch);

                spriteBatch.End();
            }
        }

        public static void AddParticleSystem(ParticleSystem system)
        {
            //GameObject.AddToList(_masterList, system);
            _masterList.Add(system);
        }
    }
}
