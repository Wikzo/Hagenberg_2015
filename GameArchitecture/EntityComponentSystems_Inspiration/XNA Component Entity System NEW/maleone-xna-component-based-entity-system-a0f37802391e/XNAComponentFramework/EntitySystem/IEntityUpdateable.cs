using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace ComponentFramework.EntitySystem
{
    public interface IEntityUpdateable
    {
        /// <summary>
        /// Gets whether the component should be updated.
        /// </summary>
        bool Enabled { get; }

        /// <summary>
        /// Gets the order in which the component should be updated. 
        /// Components with smaller values are updated first.
        /// </summary>
        int UpdateOrder { get; }

        /// <summary>
        /// Invoked when the Enabled property changes.
        /// </summary>
        event EventHandler<EventArgs> EnabledChanged;

        /// <summary>
        /// Invoked when the UpdateOrder property changes.
        /// </summary>
        event EventHandler<EventArgs> UpdateOrderChanged;

        /// <summary>
        /// Updates the component.
        /// </summary>
        void Update();
    }
}
