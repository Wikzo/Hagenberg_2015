using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using ComponentFramework;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Graphics;

namespace ComponentFramework.ScreenSystem
{
    public abstract class MenuScreen : GameScreen
    {
        List<MenuItem> _menuItems = new List<MenuItem>();

        int selectedEntry = 0;

        protected IList<MenuItem> MenuItems
        {
            get { return _menuItems; }
        }

        public MenuScreen()
        {
            TransitionOnTime = TimeSpan.FromSeconds(0.5f);
            TransitionOffTime = TimeSpan.FromSeconds(0.5f);
        }

        #region Update and Draw

        public override void Update(bool otherScreenHasFocus, bool coveredByOtherScreen)
        {
            base.Update(otherScreenHasFocus, coveredByOtherScreen);

            for (int i = 0; i < _menuItems.Count; i++)
            {
                bool isSelected = isActive && (i == selectedEntry);

                _menuItems[i].Update(this, isSelected);
            }

            // make sure our entries are in the right place before we draw them
            UpdateMenuItemLocations();
        }

        public override void Draw(SpriteBatch spriteBatch) { }

        public override void DrawHUD(SpriteBatch spriteBatch)
        {
            GraphicsDevice graphics = FrameworkServices.GraphicsDevice;
            SpriteFont font = ScreenManager.MenuFont;

            // Draw each menu entry in turn.
            for (int i = 0; i < _menuItems.Count; i++)
            {
                MenuItem item = _menuItems[i];

                bool isSelected = FrameworkServices.Game.IsActive && (i == selectedEntry);

                item.Draw(spriteBatch, this, isSelected, TransitionAlpha);
            }

            // Make the menu slide into place during transitions, using a
            // power curve to make things look more interesting (this makes
            // the movement slow down as it nears the end).
            float transitionOffset = (float)Math.Pow(TransitionPosition, 2);
        }

        private void UpdateMenuItemLocations()
        {
            float transitionOffset = (float)Math.Pow(TransitionPosition, 2);

            for (int i = 0; i < _menuItems.Count; i++)
            {
                MenuItem item = _menuItems[i];

                if (ScreenState == ScreenState.TransitionOn)
                {
                    item.Position.X -= transitionOffset;
                }
                if (ScreenState == ScreenState.TransitionOff)
                {
                    item.Position.X += transitionOffset;
                }
            }
        }

        #endregion

        #region Helper Methods

        public override void  HandleInput()
        {
            if (InputManager.IsMenuCancel())
            {
                OnCancel();
            }

            if (InputManager.IsMenuSelect())
            {
                _menuItems[selectedEntry].OnEntrySelected();
            }

            if (InputManager.IsMenuDown())
            {
                //call the Deselected event on the previously selected item
                _menuItems[selectedEntry].OnEntryUnHighlighted();

                selectedEntry++;
                if (selectedEntry > _menuItems.Count - 1)
                {
                    selectedEntry = 0;
                }

                _menuItems[selectedEntry].OnEntryHighLighted();
            }

            if (InputManager.IsMenuUp())
            {
                //call the deselected event on the previously selected item
                _menuItems[selectedEntry].OnEntryUnHighlighted();

                selectedEntry--;
                if (selectedEntry < 0)
                {
                    selectedEntry = _menuItems.Count - 1;
                }

                _menuItems[selectedEntry].OnEntryHighLighted();
            }

            //mouse interaction
            for (int i = 0; i < _menuItems.Count; i++)
            {
                MenuItem item = _menuItems[i];

                if (item.EntryHitBounds.Contains((int)InputManager.RawMousePosition.X, (int)InputManager.RawMousePosition.Y))
                {
                    _menuItems[selectedEntry].OnEntryUnHighlighted();

                    selectedEntry = i;
                    _menuItems[selectedEntry].OnEntryHighLighted();

                    if (InputManager.WasMouseButtonPressed(MouseButtons.Left))
                    {
                        _menuItems[selectedEntry].OnEntrySelected();
                    }
                }
            }
        }

        protected virtual void OnCancel()
        {
            ExitScreen();
        }

        #endregion
    }
}
