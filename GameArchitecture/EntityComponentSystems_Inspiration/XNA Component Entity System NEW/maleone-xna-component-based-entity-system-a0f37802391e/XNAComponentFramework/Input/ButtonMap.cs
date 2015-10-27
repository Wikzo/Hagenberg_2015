using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework.Input;
using ComponentFramework;

namespace ComponentFramework.Input
{
    /// <summary>
    /// A pairing of a button press, Key and Mouse Button 
    /// </summary>
    public class ButtonMap
    {
        public Buttons GamePadButton = Buttons.Back;
        public Keys Key = Keys.None;
        public MouseButtons MouseButton = MouseButtons.None;

        public ButtonMap()
        {

        }

        public ButtonMap(Buttons Button, Keys key, MouseButtons mouseButton)
        {
            GamePadButton = Button;
            Key = key;
            MouseButton = mouseButton;
        }
    }
}
