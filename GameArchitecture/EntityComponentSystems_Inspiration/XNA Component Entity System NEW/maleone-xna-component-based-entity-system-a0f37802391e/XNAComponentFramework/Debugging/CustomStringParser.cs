using System;
using System.Reflection;
using Microsoft.Xna.Framework;
using System.Globalization;

namespace ComponentFramework.Debugging
{
    // Subclass the default string parser so we can add more types we want to support
    public class CustomStringParser : XConsole.DefaultStringParser
    {
        public override object Parse(Type type, string value)
        {
            if (type == typeof(Color))
            {
                PropertyInfo p = typeof(Color).GetProperty(value, BindingFlags.Static | BindingFlags.Public);
                return p.GetValue(null, null);
            }

            return base.Parse(type, value);
        }
    }
}
