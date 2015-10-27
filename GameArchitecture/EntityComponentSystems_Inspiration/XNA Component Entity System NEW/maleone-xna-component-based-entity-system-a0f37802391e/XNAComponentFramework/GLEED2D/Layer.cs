﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace GLEED2D
{
    /// <summary>
    /// Layer is a component of a level that stores primitives or texture items
    /// </summary>
    public partial class Layer
    {
        /// <summary>
        /// The name of the layer.
        /// </summary>
        [XmlAttribute()]
        public String Name;

        /// <summary>
        /// Should this layer be visible?
        /// </summary>
        [XmlAttribute()]
        public bool Visible;

        /// <summary>
        /// The list of the items in this layer.
        /// </summary>
        public List<Item> Items;

        /// <summary>
        /// The Scroll Speed relative to the main camera. The X and Y components are 
        /// interpreted as factors, so (1;1) means the same scrolling speed as the main camera.
        /// Enables parallax scrolling.
        /// </summary>
        public Vector2 ScrollSpeed;

        public Layer()
        {
            Items = new List<Item>();
            ScrollSpeed = Vector2.One;
        }

        public void Draw(SpriteBatch spriteBatch)
        {
            if (!Visible) return;
            for (int i = 0; i < Items.Count; i++)
            {
                Items[i].Draw(spriteBatch);
            }
        }
    }
}
