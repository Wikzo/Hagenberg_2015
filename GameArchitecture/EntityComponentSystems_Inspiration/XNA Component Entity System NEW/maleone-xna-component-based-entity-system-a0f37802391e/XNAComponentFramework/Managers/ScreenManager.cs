using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using ComponentFramework.ScreenSystem;
using System.Diagnostics;
using Microsoft.Xna.Framework.Graphics;
using System.IO.IsolatedStorage;
using System.IO;

namespace ComponentFramework
{
    public static class ScreenManager
    {
        #region Fields

        private static List<GameScreen> _screens = new List<GameScreen>();
        private static List<GameScreen> _screensToUpdate = new List<GameScreen>();

        private static bool _isInitialized;

        private static bool _traceEnabled;
        private static SpriteFont _font;

        #endregion

        #region Properties

        public static bool TraceEnabled
        {
            get { return _traceEnabled; }
            set { _traceEnabled = value; }
        }

        public static SpriteFont MenuFont
        {
            get { return _font; }
            set { _font = value; }
        }

        #endregion

        #region Initialization

        public static void Initialize()
        {
            _font = FileManager.Load<SpriteFont>("Fonts/menu_font");
            
            _isInitialized = true;
        }

        public static void LoadContent()
        {
            for (int i = 0; i < _screens.Count; i++)
            {
                _screens[i].LoadContent();
            }
        }

        public static void UnloadContent()
        {
            for (int i = 0; i < _screens.Count; i++)
            {
                _screens[i].UnloadContent();
            }
        }

        #endregion

        #region  Update and Draw

        public static void Update()
        {
            _screensToUpdate.Clear();

            for (int i = 0; i < _screens.Count; i++)
            {
                _screensToUpdate.Add(_screens[i]);
            }

            bool otherScreenHasFocus = !FrameworkServices.Game.IsActive;
            bool coveredByOtherScreen = false;

            while (_screensToUpdate.Count > 0)
            {
                GameScreen screen = _screensToUpdate[_screensToUpdate.Count - 1];

                _screensToUpdate.RemoveAt(_screensToUpdate.Count - 1);

                screen.Update(otherScreenHasFocus, coveredByOtherScreen);

                if (screen.ScreenState == ScreenState.TransitionOn || screen.ScreenState == ScreenState.Active)
                {
                    if (!otherScreenHasFocus)
                    {
                        screen.HandleInput();

                        otherScreenHasFocus = true;
                    }

                    if (!screen.isPopup)
                    {
                        coveredByOtherScreen = true;
                    }
                }
            }

            if (_traceEnabled)
            {
                TraceScreens();
            }
        }

        public static void Draw(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _screens.Count; i++)
            {
                if (_screens[i].ScreenState == ScreenState.Hidden)
                {
                    continue;
                }

                _screens[i].Draw(spriteBatch);
            }
        }

        public static void DrawHUD(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _screens.Count; i++)
            {
                if (_screens[i].ScreenState == ScreenState.Hidden)
                {
                    continue;
                }

                _screens[i].DrawHUD(spriteBatch);
            }
        }

        public static void DebugDraw(SpriteBatch spriteBatch)
        {
            for (int i = 0; i < _screens.Count; i++)
            {
                if (_screens[i].ScreenState == ScreenState.Hidden)
                {
                    continue;
                }

                _screens[i].DebugDraw(spriteBatch);
            }
        }

        #endregion

        #region Helper Methods

        private static void TraceScreens()
        {
            List<string> screenNames = new List<string>();

            for (int i = 0; i < _screens.Count; i++)
            {
                screenNames.Add(_screens[i].GetType().Name);
            }

            Debug.WriteLine(string.Join(", ", screenNames.ToArray()));
        }

        #endregion

        #region Public Methods

        public static void AddScreen(GameScreen aScreenToAdd)
        {
            aScreenToAdd.IsExiting = false;

            if (_isInitialized)
            {
                aScreenToAdd.LoadContent();
            }

            _screens.Add(aScreenToAdd);
        }

        public static void RemoveScreen(GameScreen aScreenToRemove)
        {
            if (_isInitialized)
            {
                aScreenToRemove.UnloadContent();
            }

            _screens.Remove(aScreenToRemove);
            _screensToUpdate.Remove(aScreenToRemove);
        }

        public static GameScreen[] GetScreens()
        {
            return _screens.ToArray();
        }

        public static void FadeBackBufferToBlack(float aAlpha)
        {
            Viewport viewport = FrameworkServices.GraphicsDevice.Viewport;

            FrameworkServices.SpriteBatch.Begin();

            //implement later if necessary
            //ComponentFrameworkServices.SpriteBatch.Draw(blankTexture,
            //                  new Rectangle(0, 0, viewport.Width, viewport.Height),
            //                  Color.Black * aAlpha);

            FrameworkServices.SpriteBatch.End();
        }

        /// <summary>
        /// Informs the screen manager to serialize its state to disk.
        /// </summary>
        public static void SerializeState()
        {
            // open up isolated storage
            using (IsolatedStorageFile storage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                // if our screen manager directory already exists, delete the contents
                if (storage.DirectoryExists("ScreenManager"))
                {
                    DeleteState(storage);
                }

                // otherwise just create the directory
                else
                {
                    storage.CreateDirectory("ScreenManager");
                }

                // create a file we'll use to store the list of screens in the stack
                using (IsolatedStorageFileStream stream = storage.CreateFile("ScreenManager\\ScreenList.dat"))
                {
                    using (BinaryWriter writer = new BinaryWriter(stream))
                    {
                        // write out the full name of all the types in our stack so we can
                        // recreate them if needed.
                        foreach (GameScreen screen in _screens)
                        {
                            if (screen.IsSerializable)
                            {
                                writer.Write(screen.GetType().AssemblyQualifiedName);
                            }
                        }
                    }
                }

                // now we create a new file stream for each screen so it can save its state
                // if it needs to. we name each file "ScreenX.dat" where X is the index of
                // the screen in the stack, to ensure the files are uniquely named
                int screenIndex = 0;
                foreach (GameScreen screen in _screens)
                {
                    if (screen.IsSerializable)
                    {
                        string fileName = string.Format("ScreenManager\\Screen{0}.dat", screenIndex);

                        // open up the stream and let the screen serialize whatever state it wants
                        using (IsolatedStorageFileStream stream = storage.CreateFile(fileName))
                        {
                            screen.Serialize(stream);
                        }

                        screenIndex++;
                    }
                }
            }
        }

        public static bool DeserializeState()
        {
            // open up isolated storage
            using (IsolatedStorageFile storage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                // see if our saved state directory exists
                if (storage.DirectoryExists("ScreenManager"))
                {
                    try
                    {
                        // see if we have a screen list
                        if (storage.FileExists("ScreenManager\\ScreenList.dat"))
                        {
                            // load the list of screen types
                            using (IsolatedStorageFileStream stream = storage.OpenFile("ScreenManager\\ScreenList.dat", FileMode.Open, FileAccess.Read))
                            {
                                using (BinaryReader reader = new BinaryReader(stream))
                                {
                                    while (reader.BaseStream.Position < reader.BaseStream.Length)
                                    {
                                        // read a line from our file
                                        string line = reader.ReadString();

                                        // if it isn't blank, we can create a screen from it
                                        if (!string.IsNullOrEmpty(line))
                                        {
                                            Type screenType = Type.GetType(line);
                                            GameScreen screen = Activator.CreateInstance(screenType) as GameScreen;
                                            AddScreen(screen);
                                        }
                                    }
                                }
                            }
                        }

                        // next we give each screen a chance to deserialize from the disk
                        for (int i = 0; i < _screens.Count; i++)
                        {
                            string filename = string.Format("ScreenManager\\Screen{0}.dat", i);
                            using (IsolatedStorageFileStream stream = storage.OpenFile(filename, FileMode.Open, FileAccess.Read))
                            {
                                _screens[i].Deserialize(stream);
                            }
                        }

                        return true;
                    }
                    catch (Exception)
                    {
                        // if an exception was thrown while reading, odds are we cannot recover
                        // from the saved state, so we will delete it so the game can correctly
                        // launch.
                        DeleteState(storage);
                    }
                }
            }

            return false;
        }

        /// <summary>
        /// Deletes the saved state files from isolated storage.
        /// </summary>
        private static void DeleteState(IsolatedStorageFile storage)
        {
            // get all of the files in the directory and delete them
            string[] files = storage.GetFileNames("ScreenManager\\*");
            foreach (string file in files)
            {
                storage.DeleteFile(Path.Combine("ScreenManager", file));
            }
        }

        #endregion
    }
}
