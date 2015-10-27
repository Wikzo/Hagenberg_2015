using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using XNAEntityComponents.EntitySystem;
using Microsoft.Xna.Framework;
using XNAEntityComponents.Components;

namespace XNAEntityComponents.Scene
{
    public class Level
    {
        /// <summary>
        /// The Entities present when the level starts
        /// </summary>
        public List<Entity> StartingEntities = new List<Entity>();

        public Level()
        {
            GetStartingEntities();
        }

        /// <summary>
        /// Add the starting entities here, and then add them to the entity manager
        /// </summary>
        protected virtual void GetStartingEntities() 
        {
            //create the entity
            Entity Background = EntityManager.CreateNew(EntityLayer.Background);
            //add the components to the entity, passing it in as the parent
            Background.AddComponent(new StageBackground(Background));
            //add the entity to the starting Entities List
            StartingEntities.Add(Background);
        }

        /// <summary>
        /// Clears the entity Manager, and adds the entities in the starting entities list to the entity Manager
        /// </summary>
        public virtual void Initialize()
        {
            EntityManager.Clear();

            for (int i = 0; i < StartingEntities.Count; i++)
            {
                EntityManager.AddEntity(StartingEntities[i]);
            }
        }

        public virtual void Update(GameTime gameTime)
        {

        }

        public virtual void Draw(GameTime gameTime)
        {

        }
    }
}
