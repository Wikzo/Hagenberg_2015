using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using XNAEntityComponents.EntitySystem;

namespace XNAEntityComponents.Components
{
    public class TransformComponent : EntityComponent
    {
        public Vector2 Position;
        public float Scale;
        public float Rotation;

        public TransformComponent(Entity aParent) : base(aParent) 
        {
            this.Name = "TransformComponent";
        }
    }
}
