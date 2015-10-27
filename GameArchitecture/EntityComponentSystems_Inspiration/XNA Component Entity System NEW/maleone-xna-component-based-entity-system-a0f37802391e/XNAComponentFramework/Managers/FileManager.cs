using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ComponentFramework
{
    /// <summary>
    /// Used to load assets
    /// </summary>
    public static class FileManager
    {
        public static T Load<T>(string assetName)
        {
            return FrameworkServices.Content.Load<T>(assetName);
        }
    }
}
