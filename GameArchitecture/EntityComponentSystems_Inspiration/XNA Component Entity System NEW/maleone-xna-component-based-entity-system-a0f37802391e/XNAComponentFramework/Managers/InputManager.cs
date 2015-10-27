using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework;
using ComponentFramework.Input;

namespace ComponentFramework
{
    public enum MouseButtons
    {
        None,
        Left,
        Middle,
        Right,
        XButton1,
        XButton2
    }

    public static class InputManager
    {
        #region Fields

        private const int MaxPlayers = 4;

        static GamePadState[] _previousGamepadState;
        static GamePadState[] _currentGamepadState;

        static KeyboardState _previousKeyboardState, _currentKeyboardState;

        static MouseState _previousMouseState, _currentMouseState;

        public static float StickDeadZone = 0.1f;

        //array of input buffers, used to determine the last few buttons pressed by any of the 4 players
        public static List<Buttons>[] Buffer = new List<Buttons>[MaxPlayers];
        public static int BufferSize = 10;


        #region Button press time fields
        //time values for controller button presses
        private static float[] _pressTimeA = new float[MaxPlayers];
        private static float[] _previousPressTimeA = new float[MaxPlayers];

        private static float[] _pressTimeB = new float[MaxPlayers];
        private static float[] _previousPressTimeB = new float[MaxPlayers];

        private static float[] _pressTimeBack = new float[MaxPlayers];
        private static float[] _previousPressTimeBack = new float[MaxPlayers];

        private static float[] _pressTimeBigButton = new float[MaxPlayers];
        private static float[] _previousPressTimeBigButton = new float[MaxPlayers];

        private static float[] _pressTimeDPadDown = new float[MaxPlayers];
        private static float[] _previousPressTimeDPadDown = new float[MaxPlayers];

        private static float[] _pressTimeDPadLeft = new float[MaxPlayers];
        private static float[] _previousPressTimeDPadLeft = new float[MaxPlayers];

        private static float[] _pressTimeDPadRight = new float[MaxPlayers];
        private static float[] _previousPressTimeDPadRight = new float[MaxPlayers];

        private static float[] _pressTimeDPadUp = new float[MaxPlayers];
        private static float[] _previousPressTimeDPadUp = new float[MaxPlayers];

        private static float[] _pressTimeLeftShoulder = new float[MaxPlayers];
        private static float[] _previousPressTimeLeftShoulder = new float[MaxPlayers];

        private static float[] _pressTimeLeftStick = new float[MaxPlayers];
        private static float[] _previousPressTimeLeftStick = new float[MaxPlayers];

        private static float[] _pressTimeLeftThumbstickDown = new float[MaxPlayers];
        private static float[] _previousPressTimeLeftThumbstickDown = new float[MaxPlayers];

        private static float[] _pressTimeLeftThumbstickRight = new float[MaxPlayers];
        private static float[] _previousPressTimeLeftThumbstickRight = new float[MaxPlayers];

        private static float[] _pressTimeLeftThumbstickLeft = new float[MaxPlayers];
        private static float[] _previousPressTimeLeftThumbstickLeft = new float[MaxPlayers];

        private static float[] _pressTimeLeftThumbstickUp = new float[MaxPlayers];
        private static float[] _previousPressTimeLeftThumbstickUp = new float[MaxPlayers];

        private static float[] _pressTimeLeftTrigger = new float[MaxPlayers];
        private static float[] _previousPressTimeLeftTrigger = new float[MaxPlayers];

        private static float[] _pressTimeRightShoulder = new float[MaxPlayers];
        private static float[] _previousPressTimeRightShoulder = new float[MaxPlayers];

        private static float[] _pressTimeRightStick = new float[MaxPlayers];
        private static float[] _previousPressTimeRightStick = new float[MaxPlayers];

        private static float[] _pressTimeRightThumbstickDown = new float[MaxPlayers];
        private static float[] _previousPressTimeRightThumbstickDown = new float[MaxPlayers];

        private static float[] _pressTimeRightThumbstickLeft = new float[MaxPlayers];
        private static float[] _previousPressTimeRightThumbstickLeft = new float[MaxPlayers];

        private static float[] _pressTimeRightThumbstickRight = new float[MaxPlayers];
        private static float[] _previousPressTimeRightThumbstickRight = new float[MaxPlayers];

        private static float[] _pressTimeRightThumbstickUp = new float[MaxPlayers];
        private static float[] _previousPressTimeRightThumbstickUp = new float[MaxPlayers];

        private static float[] _pressTimeRightTrigger = new float[MaxPlayers];
        private static float[] _previousPressTimeRightTrigger = new float[MaxPlayers];

        private static float[] _pressTimeStart = new float[MaxPlayers];
        private static float[] _previousPressTimeStart = new float[MaxPlayers];

        private static float[] _pressTimeX = new float[MaxPlayers];
        private static float[] _previousPressTimeX = new float[MaxPlayers];

        private static float[] _pressTimeY = new float[MaxPlayers];
        private static float[] _previousPressTimeY = new float[MaxPlayers];

        #endregion

        //hold the amount of time the button has been down for
        private static float[] _timeSinceButtonDownA = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownB = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownBack = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownBigButton = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownDPadDown = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownDPadLeft = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownDPadRight = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownDPadUp = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownLeftShoulder = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownLeftStick = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownLeftThumbstickDown = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownLeftThumbstickRight = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownLeftThumbstickLeft = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownLeftThumbstickUp = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownLeftTrigger = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownRightShoulder = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownRightStick = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownRightThumbstickDown = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownRightThumbstickLeft = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownRightThumbstickRight = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownRightThumbstickUp = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownRightTrigger = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownStart = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownX = new float[MaxPlayers];
        private static float[] _timeSinceButtonDownY = new float[MaxPlayers];


        //all the key press times for relevant keys
        private static List<Keys> _trackedKeys = new List<Keys>();
        private static Dictionary<Keys, float> _keyPressTimes = new Dictionary<Keys, float>();
        private static Dictionary<Keys, float> _previousKeyPressTimes = new Dictionary<Keys, float>();
        private static Keys[] _pressedKeys;
        private static Dictionary<Keys, float> _timeSinceKeyDown = new Dictionary<Keys, float>();

        //mouse timing variables
        private static float _pressTimeMouseLeft;
        private static float _previousPressTimeMouseLeft;
        private static float _timeSinceButtonDownMouseLeft;

        private static float _pressTimeMouseMiddle;
        private static float _previousPressTimeMouseMiddle;
        private static float _timeSinceButtonDownMouseMiddle;

        private static float _pressTimeMouseRight;
        private static float _previousPressTimeMouseRight;
        private static float _timeSinceButtonDownMouseRight;

        private static float _pressTimeXButton1;
        private static float _previousPressTimeXButton1;
        private static float _timeSinceButtonDownXButton1;

        private static float _pressTimeXButton2;
        private static float _previousPressTimeXButton2;
        private static float _timeSinceButtonDownXButton2;

        #endregion

        #region Properties

        /// <summary>
        /// Get the position of the mouse in the world
        /// </summary>
        /// <param name="camera">The user's camera.  Mouse world position is calculated based on it's values</param>
        /// <returns>the position of the mouse in the world</returns>
        public static Vector2 GetMouseWorldPosition(Camera2D camera)
        {
            return Vector2.Transform(RawMousePosition, camera.InverseTransform);
        }

        public static Vector2 RawMousePosition
        {
            get { return new Vector2(_currentMouseState.X, _currentMouseState.Y); }
        }

        public static int MouseScrollWheelValue
        {
            get { return _currentMouseState.ScrollWheelValue; }
        }

        public static bool MouseScrollWheelUp
        {
            get { return _previousMouseState.ScrollWheelValue < _currentMouseState.ScrollWheelValue; }
        }

        public static bool MouseScrollWheelDown
        {
            get { return _previousMouseState.ScrollWheelValue > _currentMouseState.ScrollWheelValue; }
        }

        public static int MouseScrollWheelChange
        {
            //divide by 120 here because on my mouse, going from notch to notch changes
            //the scrol wheel value by 120 units which I want to be about 1 unit in usable terms.
            get { return (_currentMouseState.ScrollWheelValue - _previousMouseState.ScrollWheelValue) / 120; }
        }

        public static Vector2 MouseMovement
        {
            get { return new Vector2(_currentMouseState.X - _previousMouseState.X, _currentMouseState.Y - _previousMouseState.Y); }
        }

        public static int MaximumPlayers
        {
            get { return MaxPlayers; }
        }

        public static GamePadState[] CurrentGamePadState
        {
            get { return _currentGamepadState; }
        }

        public static GamePadState GetCurrentGamePadState(int aIndex)
        {
            return _currentGamepadState[aIndex];
        }

        public static KeyboardState CurrentKeyboardState
        {
            get { return _currentKeyboardState; }
        }

        public static KeyboardState GetCurrentKeyboardState()
        {
            return _currentKeyboardState;
        }

        public static MouseState CurrentMouseState
        {
            get { return _currentMouseState; }
        }

        public static MouseState GetCurrentMouseState()
        {
            return _currentMouseState;
        }

        public static double GetRightStickAngle(int aPlayerIndex)
        {
            return Helper.ToRadians(_currentGamepadState[aPlayerIndex].ThumbSticks.Right);
        }

        public static float GetRightStickAngleDegrees(int aPlayerIndex)
        {
            return Helper.ToDegrees(_currentGamepadState[aPlayerIndex].ThumbSticks.Right);
        }

        public static Vector2 GetRightStickValue(int aPlayerIndex)
        {
            return _currentGamepadState[aPlayerIndex].ThumbSticks.Right;
        }

        public static float GetLeftTriggerValue(int aPlayerIndex)
        {
            return _currentGamepadState[aPlayerIndex].Triggers.Left;
        }

        public static double GetLeftStickAngle(int aPlayerIndex)
        {
            return Helper.ToRadians(_currentGamepadState[aPlayerIndex].ThumbSticks.Left);
        }

        public static float GetLeftStickAngleDegrees(int aPlayerIndex)
        {
            return Helper.ToDegrees(_currentGamepadState[aPlayerIndex].ThumbSticks.Left);
        }

        public static Vector2 GetLeftStickValue(int aPlayerIndex)
        {
            return _currentGamepadState[aPlayerIndex].ThumbSticks.Left;
        }

        public static float GetRightTriggerValue(int aPlayerIndex)
        {
            return _currentGamepadState[aPlayerIndex].Triggers.Right;
        }

        public static float TrackedKeys
        {
            get { return _trackedKeys.Count; }
        }

        public static Keys[] PressedKeys
        {
            get { return _pressedKeys; }
        }

        #endregion

        #region Initialization

        public static void Initialize()
        {
            //create the input buffers
            for (int i = 0; i < MaxPlayers; i++)
            {
                Buffer[i] = new List<Buttons>(BufferSize);
            }

            InitializeGamepadValues();

            InitializeKeyboardValues();

            InitializeMouseValues();
        }

        private static void InitializeGamepadValues()
        {
            _previousGamepadState = new GamePadState[MaxPlayers];
            _currentGamepadState = new GamePadState[MaxPlayers];

            //set up game pad press time values
            for (int i = 0; i < MaxPlayers; i++)
            {
                _pressTimeA[i] = float.NaN;
                _previousPressTimeA[i] = float.NaN;

                _pressTimeB[i] = float.NaN;
                _previousPressTimeB[i] = float.NaN;

                _pressTimeBack[i] = float.NaN;
                _previousPressTimeBack[i] = float.NaN;

                _pressTimeBigButton[i] = float.NaN;
                _previousPressTimeBigButton[i] = float.NaN;

                _pressTimeDPadDown[i] = float.NaN;
                _previousPressTimeDPadDown[i] = float.NaN;

                _pressTimeDPadLeft[i] = float.NaN;
                _previousPressTimeDPadLeft[i] = float.NaN;

                _pressTimeDPadRight[i] = float.NaN;
                _previousPressTimeDPadRight[i] = float.NaN;

                _pressTimeDPadUp[i] = float.NaN;
                _previousPressTimeDPadUp[i] = float.NaN;

                _pressTimeLeftShoulder[i] = float.NaN;
                _previousPressTimeLeftShoulder[i] = float.NaN;

                _pressTimeLeftStick[i] = float.NaN;
                _previousPressTimeLeftStick[i] = float.NaN;

                _pressTimeLeftThumbstickDown[i] = float.NaN;
                _previousPressTimeLeftThumbstickDown[i] = float.NaN;

                _pressTimeLeftThumbstickRight[i] = float.NaN;
                _previousPressTimeLeftThumbstickRight[i] = float.NaN;

                _pressTimeLeftThumbstickLeft[i] = float.NaN;
                _previousPressTimeLeftThumbstickLeft[i] = float.NaN;

                _pressTimeLeftThumbstickUp[i] = float.NaN;
                _previousPressTimeLeftThumbstickUp[i] = float.NaN;

                _pressTimeLeftTrigger[i] = float.NaN;
                _previousPressTimeLeftTrigger[i] = float.NaN;

                _pressTimeRightShoulder[i] = float.NaN;
                _previousPressTimeRightShoulder[i] = float.NaN;

                _pressTimeRightStick[i] = float.NaN;
                _previousPressTimeRightStick[i] = float.NaN;

                _pressTimeRightThumbstickDown[i] = float.NaN;
                _previousPressTimeRightThumbstickDown[i] = float.NaN;

                _pressTimeRightThumbstickLeft[i] = float.NaN;
                _previousPressTimeRightThumbstickLeft[i] = float.NaN;

                _pressTimeRightThumbstickRight[i] = float.NaN;
                _previousPressTimeRightThumbstickRight[i] = float.NaN;

                _pressTimeRightThumbstickUp[i] = float.NaN;
                _previousPressTimeRightThumbstickUp[i] = float.NaN;

                _pressTimeRightTrigger[i] = float.NaN;
                _previousPressTimeRightTrigger[i] = float.NaN;

                _pressTimeStart[i] = float.NaN;
                _previousPressTimeStart[i] = float.NaN;

                _pressTimeX[i] = float.NaN;
                _previousPressTimeX[i] = float.NaN;

                _pressTimeY[i] = float.NaN;
                _previousPressTimeY[i] = float.NaN;

                _timeSinceButtonDownA[i] = 0f;
                _timeSinceButtonDownB[i] = 0f;
                _timeSinceButtonDownBack[i] = 0f;
                _timeSinceButtonDownBigButton[i] = 0f;
                _timeSinceButtonDownDPadDown[i] = 0f;
                _timeSinceButtonDownDPadLeft[i] = 0f;
                _timeSinceButtonDownDPadRight[i] = 0f;
                _timeSinceButtonDownDPadUp[i] = 0f;
                _timeSinceButtonDownLeftShoulder[i] = 0f;
                _timeSinceButtonDownLeftStick[i] = 0f;
                _timeSinceButtonDownLeftThumbstickDown[i] = 0f;
                _timeSinceButtonDownLeftThumbstickRight[i] = 0f;
                _timeSinceButtonDownLeftThumbstickLeft[i] = 0f;
                _timeSinceButtonDownLeftThumbstickUp[i] = 0f;
                _timeSinceButtonDownLeftTrigger[i] = 0f;
                _timeSinceButtonDownRightShoulder[i] = 0f;
                _timeSinceButtonDownRightStick[i] = 0f;
                _timeSinceButtonDownRightThumbstickDown[i] = 0f;
                _timeSinceButtonDownRightThumbstickLeft[i] = 0f;
                _timeSinceButtonDownRightThumbstickRight[i] = 0f;
                _timeSinceButtonDownRightThumbstickUp[i] = 0f;
                _timeSinceButtonDownRightTrigger[i] = 0f;
                _timeSinceButtonDownStart[i] = 0f;
                _timeSinceButtonDownX[i] = 0f;
                _timeSinceButtonDownY[i] = 0f;
            }
        }

        private static void InitializeKeyboardValues()
        {
            _trackedKeys.Clear();
            _previousKeyPressTimes.Clear();
            _keyPressTimes.Clear();
            _timeSinceKeyDown.Clear();
        }

        private static void InitializeMouseValues()
        {
            //mouse button press times
            _pressTimeMouseLeft = float.NaN;
            _previousPressTimeMouseLeft = float.NaN;
            _timeSinceButtonDownMouseLeft = 0f;

            _pressTimeMouseMiddle = float.NaN;
            _previousPressTimeMouseMiddle = float.NaN;
            _timeSinceButtonDownMouseMiddle = 0f;

            _pressTimeMouseRight = float.NaN;
            _previousPressTimeMouseRight = float.NaN;
            _timeSinceButtonDownMouseRight = 0f;

            _pressTimeXButton1 = float.NaN;
            _previousPressTimeXButton1 = float.NaN;
            _timeSinceButtonDownXButton1 = 0f;

            _pressTimeXButton2 = float.NaN;
            _previousPressTimeXButton2 = float.NaN;
            _timeSinceButtonDownXButton2 = 0f;
            
        }

        #endregion

        #region Update

        public static void Update()
        {
            UpdateGamepadVariables();

            UpdateKeyboardVariables();

            UpdateMouseVariables();
        }

        private static void UpdateGamepadVariables()
        {
            //gamepad stuff
            for (int i = 0; i < MaxPlayers; i++)
            {
                _previousGamepadState[i] = _currentGamepadState[i];
                _currentGamepadState[i] = GamePad.GetState((PlayerIndex)i);

                //update button down time variables
                UpdateButtonDownTimes(i);
            }
        }

        private static void UpdateKeyboardVariables()
        {
            //keyboard stuff
            _previousKeyboardState = _currentKeyboardState;
            _currentKeyboardState = Keyboard.GetState();

            _pressedKeys = _currentKeyboardState.GetPressedKeys();

            //check to see if each key was currently being tracked, if not track it
            for (int i = 0; i < _pressedKeys.Length; i++)
            {
                Keys key = _pressedKeys[i];

                if (!_trackedKeys.Contains(key) && key != Keys.None)
                {
                    TrackKey(key);
                }
            }

            for (int i = 0; i < _trackedKeys.Count; i++)
            {
                Keys key = _trackedKeys[i];

                //only keep track of key press times for 10 seconds
                if (Helper.DifferenceBetween(_keyPressTimes[key], (float)TimeManager.CurrentTime) >= 10f &&
                    _timeSinceKeyDown[key] <= 0f)
                {
                    UnTrackKey(key);
                }

                //update the amount of time this key has been held down
                if (_currentKeyboardState.IsKeyDown(key))
                {
                    _timeSinceKeyDown[key] += (float)TimeManager.SecondDifference;
                }
                else
                {
                    _timeSinceKeyDown[key] = 0f;
                }
            }
        }

        private static void UpdateMouseVariables()
        {
            //mouse stuff
            _previousMouseState = _currentMouseState;
            _currentMouseState = Mouse.GetState();

            UpdateMouseButtonDownTimes();
        }

        #endregion

        #region Button Press Methods

        public static bool WasButtonPressed(PlayerIndex aPlayerIndex, Buttons aButton)
        {
            bool wasButtonPressed = _previousGamepadState[(int)aPlayerIndex].IsButtonUp(aButton) &&
                                    _currentGamepadState[(int)aPlayerIndex].IsButtonDown(aButton);

            //update button press time variables
            if (wasButtonPressed)
            {
                switch (aButton)
                {
                    case Buttons.A:
                        _previousPressTimeA[(int)aPlayerIndex] = _pressTimeA[(int)aPlayerIndex];
                        _pressTimeA[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.B:
                        _previousPressTimeB[(int)aPlayerIndex] = _pressTimeB[(int)aPlayerIndex];
                        _pressTimeB[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.Back:
                        _previousPressTimeBack[(int)aPlayerIndex] = _pressTimeB[(int)aPlayerIndex];
                        _pressTimeBack[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.BigButton:
                        _previousPressTimeBigButton[(int)aPlayerIndex] = _pressTimeBigButton[(int)aPlayerIndex];
                        _pressTimeBigButton[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.DPadDown:
                        _previousPressTimeDPadDown[(int)aPlayerIndex] = _pressTimeDPadDown[(int)aPlayerIndex];
                        _pressTimeDPadDown[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.DPadLeft:
                        _previousPressTimeDPadLeft[(int)aPlayerIndex] = _pressTimeDPadLeft[(int)aPlayerIndex];
                        _pressTimeDPadLeft[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.DPadRight:
                        _previousPressTimeDPadRight[(int)aPlayerIndex] = _pressTimeDPadRight[(int)aPlayerIndex];
                        _pressTimeDPadRight[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.DPadUp:
                        _previousPressTimeDPadUp[(int)aPlayerIndex] = _pressTimeDPadUp[(int)aPlayerIndex];
                        _pressTimeDPadUp[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.LeftShoulder: 
                        _previousPressTimeLeftShoulder[(int)aPlayerIndex] = _pressTimeLeftShoulder[(int)aPlayerIndex];
                        _pressTimeLeftShoulder[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.LeftStick:
                        _previousPressTimeLeftStick[(int)aPlayerIndex] = _pressTimeLeftStick[(int)aPlayerIndex];
                        _pressTimeLeftStick[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.LeftThumbstickDown:
                        _previousPressTimeLeftThumbstickDown[(int)aPlayerIndex] = _pressTimeLeftThumbstickDown[(int)aPlayerIndex];
                        _pressTimeLeftThumbstickDown[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.LeftThumbstickLeft:
                        _previousPressTimeLeftThumbstickLeft[(int)aPlayerIndex] = _pressTimeLeftThumbstickLeft[(int)aPlayerIndex];
                        _pressTimeLeftThumbstickLeft[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.LeftThumbstickRight:
                        _previousPressTimeLeftThumbstickRight[(int)aPlayerIndex] = _pressTimeLeftThumbstickRight[(int)aPlayerIndex];
                        _pressTimeLeftThumbstickRight[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.LeftThumbstickUp: 
                        _previousPressTimeLeftThumbstickUp[(int)aPlayerIndex] = _pressTimeLeftThumbstickUp[(int)aPlayerIndex];
                        _pressTimeLeftThumbstickUp[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.LeftTrigger:
                        _previousPressTimeLeftTrigger[(int)aPlayerIndex] = _pressTimeLeftTrigger[(int)aPlayerIndex];
                        _pressTimeLeftTrigger[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.RightShoulder:
                        _previousPressTimeRightShoulder[(int)aPlayerIndex] = _pressTimeRightShoulder[(int)aPlayerIndex];
                        _pressTimeRightShoulder[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.RightStick:
                        _previousPressTimeRightStick[(int)aPlayerIndex] = _pressTimeRightStick[(int)aPlayerIndex];
                        _pressTimeRightStick[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.RightThumbstickDown:
                        _previousPressTimeRightThumbstickDown[(int)aPlayerIndex] = _pressTimeRightThumbstickDown[(int)aPlayerIndex];
                        _pressTimeRightThumbstickDown[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.RightThumbstickLeft:
                        _previousPressTimeRightThumbstickLeft[(int)aPlayerIndex] = _pressTimeRightThumbstickLeft[(int)aPlayerIndex];
                        _pressTimeRightThumbstickLeft[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.RightThumbstickRight:
                        _previousPressTimeRightThumbstickRight[(int)aPlayerIndex] = _pressTimeRightThumbstickRight[(int)aPlayerIndex];
                        _pressTimeRightThumbstickRight[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.RightThumbstickUp:
                        _previousPressTimeRightThumbstickUp[(int)aPlayerIndex] = _pressTimeRightThumbstickUp[(int)aPlayerIndex];
                        _pressTimeRightThumbstickUp[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.RightTrigger:
                        _previousPressTimeRightTrigger[(int)aPlayerIndex] = _pressTimeRightTrigger[(int)aPlayerIndex];
                        _pressTimeRightTrigger[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.Start:
                        _previousPressTimeStart[(int)aPlayerIndex] = _pressTimeStart[(int)aPlayerIndex];
                        _pressTimeStart[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.X:
                        _previousPressTimeX[(int)aPlayerIndex] = _pressTimeX[(int)aPlayerIndex];
                        _pressTimeX[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    case Buttons.Y:
                        _previousPressTimeY[(int)aPlayerIndex] = _pressTimeY[(int)aPlayerIndex];
                        _pressTimeY[(int)aPlayerIndex] = (float)TimeManager.CurrentTime;
                        break;
                    default:
                        break;
                }
            }

            return wasButtonPressed;
        }

        public static bool WasButtonReleased(PlayerIndex aPlayerIndex, Buttons aButton)
        {
            return _previousGamepadState[(int)aPlayerIndex].IsButtonDown(aButton) &&
                   _currentGamepadState[(int)aPlayerIndex].IsButtonUp(aButton);
        }

        public static bool IsButtonDown(PlayerIndex aPlayerIndex, Buttons aButton)
        {
            return _currentGamepadState[(int)aPlayerIndex].IsButtonDown(aButton);
        }

        public static bool WasButtonDoubleTapped(PlayerIndex aPlayerIndex, Buttons aButton, float aMaxTimeBetweenPresses)
        {
            return WasButtonPressed(aPlayerIndex, aButton) && 
                TimeBetweenButtonPresses(aPlayerIndex, aButton) < aMaxTimeBetweenPresses;
        }

        public static bool AnyButtonPressed(PlayerIndex aPlayerIndex)
        {
            return _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.A) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.A) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.B) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.B) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.Back) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.Back) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.DPadDown) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.DPadDown) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.DPadLeft) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.DPadLeft) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.DPadRight) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.DPadRight) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.DPadUp) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.DPadUp) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.LeftShoulder) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.LeftShoulder) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.LeftStick) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.LeftStick) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.LeftTrigger) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.LeftTrigger) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.RightShoulder) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.RightShoulder) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.RightStick) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.RightStick) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.RightTrigger) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.RightTrigger) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.Start) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.Start) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.X) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.X) ||
                   _previousGamepadState[(int)aPlayerIndex].IsButtonUp(Buttons.Y) && _currentGamepadState[(int)aPlayerIndex].IsButtonDown(Buttons.Y);
        }

        public static float TimeBetweenButtonPresses(PlayerIndex aPlayerIndex, Buttons aButton)
        {
            int index = (int)aPlayerIndex;

            switch (aButton)
            {
                case Buttons.A:
                    return Helper.DifferenceBetween(_pressTimeA[index], _previousPressTimeA[index]);
                case Buttons.B:
                    return Helper.DifferenceBetween(_pressTimeB[index], _previousPressTimeB[index]);
                case Buttons.Back:
                    return Helper.DifferenceBetween(_pressTimeBack[index], _previousPressTimeBack[index]);
                case Buttons.BigButton:
                    return Helper.DifferenceBetween(_pressTimeBigButton[index], _previousPressTimeBigButton[index]);
                case Buttons.DPadDown:
                    return Helper.DifferenceBetween(_pressTimeDPadDown[index], _previousPressTimeDPadDown[index]);
                case Buttons.DPadLeft:
                    return Helper.DifferenceBetween(_pressTimeDPadLeft[index], _previousPressTimeDPadLeft[index]);
                case Buttons.DPadRight:
                    return Helper.DifferenceBetween(_pressTimeDPadRight[index], _previousPressTimeDPadRight[index]);
                case Buttons.DPadUp:
                    return Helper.DifferenceBetween(_pressTimeDPadUp[index], _previousPressTimeDPadUp[index]);
                case Buttons.LeftShoulder:
                    return Helper.DifferenceBetween(_pressTimeLeftShoulder[index], _previousPressTimeLeftShoulder[index]);
                case Buttons.LeftStick:
                    return Helper.DifferenceBetween(_pressTimeLeftStick[index], _previousPressTimeLeftStick[index]);
                case Buttons.LeftThumbstickDown:
                    return Helper.DifferenceBetween(_pressTimeLeftThumbstickDown[index], _previousPressTimeLeftThumbstickDown[index]);
                case Buttons.LeftThumbstickLeft:
                    return Helper.DifferenceBetween(_pressTimeLeftThumbstickLeft[index], _previousPressTimeLeftThumbstickLeft[index]);
                case Buttons.LeftThumbstickRight:
                    return Helper.DifferenceBetween(_pressTimeLeftThumbstickRight[index], _previousPressTimeLeftThumbstickRight[index]);
                case Buttons.LeftThumbstickUp:
                    return Helper.DifferenceBetween(_pressTimeLeftThumbstickUp[index], _previousPressTimeLeftThumbstickUp[index]);
                case Buttons.LeftTrigger:
                    return Helper.DifferenceBetween(_pressTimeLeftTrigger[index], _previousPressTimeLeftTrigger[index]);
                case Buttons.RightShoulder:
                    return Helper.DifferenceBetween(_pressTimeRightShoulder[index], _previousPressTimeRightShoulder[index]);
                case Buttons.RightStick:
                    return Helper.DifferenceBetween(_pressTimeRightStick[index], _previousPressTimeRightStick[index]);
                case Buttons.RightThumbstickDown:
                    return Helper.DifferenceBetween(_pressTimeRightThumbstickDown[index], _previousPressTimeRightThumbstickDown[index]);
                case Buttons.RightThumbstickLeft:
                    return Helper.DifferenceBetween(_pressTimeRightThumbstickLeft[index], _previousPressTimeRightThumbstickLeft[index]);
                case Buttons.RightThumbstickRight:
                    return Helper.DifferenceBetween(_pressTimeRightThumbstickRight[index], _previousPressTimeRightThumbstickRight[index]);
                case Buttons.RightThumbstickUp:
                    return Helper.DifferenceBetween(_pressTimeRightThumbstickUp[index], _previousPressTimeRightThumbstickUp[index]);
                case Buttons.RightTrigger:
                    return Helper.DifferenceBetween(_pressTimeRightTrigger[index], _previousPressTimeRightTrigger[index]);
                case Buttons.Start:
                    return Helper.DifferenceBetween(_pressTimeStart[index], _previousPressTimeStart[index]);
                case Buttons.X:
                    return Helper.DifferenceBetween(_pressTimeX[index], _previousPressTimeX[index]);
                case Buttons.Y:
                    return Helper.DifferenceBetween(_pressTimeY[index], _previousPressTimeY[index]);
                default:
                    break;
            }

            return 10f;
        }

        public static float TimeSinceButtonDown(PlayerIndex aPlayerIndex, Buttons aButton)
        {
            switch (aButton)
            {
                case Buttons.A:
                    {
                        return _timeSinceButtonDownA[(int)aPlayerIndex];
                    }
                case Buttons.B:
                    {
                        return _timeSinceButtonDownB[(int)aPlayerIndex];
                    }
                case Buttons.Back:
                    {
                        return _timeSinceButtonDownBack[(int)aPlayerIndex];
                    }
                case Buttons.BigButton:
                    {
                        return _timeSinceButtonDownBigButton[(int)aPlayerIndex];
                    }
                case Buttons.DPadDown:
                    {
                        return _timeSinceButtonDownDPadDown[(int)aPlayerIndex];
                    }
                case Buttons.DPadLeft:
                    {
                        return _timeSinceButtonDownDPadLeft[(int)aPlayerIndex];
                    }
                case Buttons.DPadRight:
                    {
                        return _timeSinceButtonDownDPadRight[(int)aPlayerIndex];
                    }
                case Buttons.DPadUp:
                    {
                        return _timeSinceButtonDownDPadUp[(int)aPlayerIndex];
                    }
                case Buttons.LeftShoulder:
                    {
                        return _timeSinceButtonDownLeftShoulder[(int)aPlayerIndex];
                    }
                case Buttons.LeftStick:
                    {
                        return _timeSinceButtonDownLeftStick[(int)aPlayerIndex];
                    }
                case Buttons.LeftThumbstickDown:
                    {
                        return _timeSinceButtonDownLeftThumbstickDown[(int)aPlayerIndex];
                    }
                case Buttons.LeftThumbstickLeft:
                    {
                        return _timeSinceButtonDownLeftThumbstickLeft[(int)aPlayerIndex];
                    }
                case Buttons.LeftThumbstickRight:
                    {
                        return _timeSinceButtonDownLeftThumbstickRight[(int)aPlayerIndex];
                    }
                case Buttons.LeftThumbstickUp:
                    {
                        return _timeSinceButtonDownLeftThumbstickUp[(int)aPlayerIndex];
                    }
                case Buttons.LeftTrigger:
                    {
                        return _timeSinceButtonDownLeftTrigger[(int)aPlayerIndex];
                    }
                case Buttons.RightShoulder:
                    {
                        return _timeSinceButtonDownRightShoulder[(int)aPlayerIndex];
                    }
                case Buttons.RightStick:
                    {
                        return _timeSinceButtonDownRightStick[(int)aPlayerIndex];
                    }
                case Buttons.RightThumbstickDown:
                    {
                        return _timeSinceButtonDownRightThumbstickDown[(int)aPlayerIndex];
                    }
                case Buttons.RightThumbstickLeft:
                    {
                        return _timeSinceButtonDownRightThumbstickLeft[(int)aPlayerIndex];
                    }
                case Buttons.RightThumbstickRight:
                    {
                        return _timeSinceButtonDownRightThumbstickRight[(int)aPlayerIndex];
                    }
                case Buttons.RightThumbstickUp:
                    {
                        return _timeSinceButtonDownRightThumbstickUp[(int)aPlayerIndex];
                    }
                case Buttons.RightTrigger:
                    {
                        return _timeSinceButtonDownRightTrigger[(int)aPlayerIndex];
                    }
                case Buttons.Start:
                    {
                        return _timeSinceButtonDownStart[(int)aPlayerIndex];
                    }
                case Buttons.X:
                    {
                        return _timeSinceButtonDownX[(int)aPlayerIndex];
                    }
                case Buttons.Y:
                    {
                        return _timeSinceButtonDownY[(int)aPlayerIndex];
                    }
                }
            
            return 0f;
        }

        #endregion

        #region Key Press Methods

        public static bool WasKeyPressed(Keys aKey)
        {
            bool wasKeyPressed = _previousKeyboardState.IsKeyUp(aKey) &&
                                 _currentKeyboardState.IsKeyDown(aKey);

            if (wasKeyPressed)
            {
                _previousKeyPressTimes[aKey] = _keyPressTimes[aKey];
                _keyPressTimes[aKey] = (float)TimeManager.CurrentTime;
            }

            return wasKeyPressed;
        }

        public static bool WasKeyReleased(Keys aKey)
        {
            return _previousKeyboardState.IsKeyDown(aKey) &&
                   _currentKeyboardState.IsKeyUp(aKey);
        }

        public static bool IsKeyDown(Keys aKey)
        {
            return _currentKeyboardState.IsKeyDown(aKey);
        }

        public static bool WasKeyDoubleTapped(Keys aKey, float aMaxTimeBetweenPresses)
        {
            return WasKeyPressed(aKey) &&
                TimeBetweenKeyPresses(aKey) < aMaxTimeBetweenPresses;
        }

        public static float TimeBetweenKeyPresses(Keys aKey)
        {
            return Helper.DifferenceBetween(_previousKeyPressTimes[aKey], _keyPressTimes[aKey]);
        }

        private static void TrackKey(Keys aKey)
        {
            _trackedKeys.Add(aKey);
            _previousKeyPressTimes.Add(aKey, (float)TimeManager.CurrentTime);
            _keyPressTimes.Add(aKey, (float)TimeManager.CurrentTime);
            if (!_timeSinceKeyDown.ContainsKey(aKey))
            {
                _timeSinceKeyDown.Add(aKey, 0f);
            }
        }

        private static void UnTrackKey(Keys aKey)
        {
            _trackedKeys.Remove(aKey);
            _previousKeyPressTimes.Remove(aKey);
            _keyPressTimes.Remove(aKey);
            _timeSinceKeyDown.Remove(aKey);
        }

        public static float TimeSinceKeyDown(Keys aKey)
        {
            if (_timeSinceKeyDown.ContainsKey(aKey))
            {
                return _timeSinceKeyDown[aKey];
            }

            return 0f;
        }

        #endregion

        #region Mouse Methods

        public static bool WasMouseButtonPressed(MouseButtons aMouseButton)
        {
            bool wasMouseButtonPressed;

            switch (aMouseButton)
            {
                case MouseButtons.None:
                    {
                        return false;
                    }
                case MouseButtons.Left:
                    {
                        wasMouseButtonPressed = _previousMouseState.LeftButton == ButtonState.Released &&
                            _currentMouseState.LeftButton == ButtonState.Pressed;

                        if (wasMouseButtonPressed)
                        {
                            _previousPressTimeMouseLeft = _pressTimeMouseLeft;
                            _pressTimeMouseLeft = (float)TimeManager.CurrentTime;
                        }

                        return wasMouseButtonPressed;
                    }
                case MouseButtons.Middle:
                    {
                        wasMouseButtonPressed = _previousMouseState.MiddleButton == ButtonState.Released &&
                            _currentMouseState.MiddleButton == ButtonState.Pressed;

                        if (wasMouseButtonPressed)
                        {
                            _previousPressTimeMouseMiddle = _pressTimeMouseMiddle;
                            _pressTimeMouseMiddle = (float)TimeManager.CurrentTime;
                        }

                        return wasMouseButtonPressed;
                    }
                case MouseButtons.Right:
                    {
                        wasMouseButtonPressed = _previousMouseState.RightButton == ButtonState.Released &&
                            _currentMouseState.RightButton == ButtonState.Pressed;

                        if (wasMouseButtonPressed)
                        {
                            _previousPressTimeMouseRight = _pressTimeMouseRight;
                            _pressTimeMouseRight = (float)TimeManager.CurrentTime;
                        }

                        return wasMouseButtonPressed;
                    }
                case MouseButtons.XButton1:
                    {
                        wasMouseButtonPressed = _previousMouseState.XButton1 == ButtonState.Released &&
                            _currentMouseState.XButton1 == ButtonState.Pressed;

                        if (wasMouseButtonPressed)
                        {
                            _previousPressTimeXButton1 = _pressTimeXButton1;
                            _pressTimeXButton1 = (float)TimeManager.CurrentTime;
                        }

                        return wasMouseButtonPressed;
                    }
                case MouseButtons.XButton2:
                    {
                        wasMouseButtonPressed = _previousMouseState.XButton2 == ButtonState.Released &&
                            _currentMouseState.XButton2 == ButtonState.Pressed;

                        if (wasMouseButtonPressed)
                        {
                            _previousPressTimeXButton2 = _pressTimeXButton2;
                            _pressTimeXButton2 = (float)TimeManager.CurrentTime;
                        }

                        return wasMouseButtonPressed;
                    }
                default:
                    break;
            }

            return false;
        }

        public static bool WasMouseButtonReleased(MouseButtons aMouseButton)
        {
            switch (aMouseButton)
            {
                case MouseButtons.None:
                    {
                        return false;
                    }
                case MouseButtons.Left:
                    {
                        return _previousMouseState.LeftButton == ButtonState.Pressed && _currentMouseState.LeftButton == ButtonState.Released;
                    }
                case MouseButtons.Middle:
                    {
                        return _previousMouseState.MiddleButton == ButtonState.Pressed && _currentMouseState.MiddleButton == ButtonState.Released;
                    }
                case MouseButtons.Right:
                    {
                        return _previousMouseState.RightButton == ButtonState.Pressed && _currentMouseState.RightButton == ButtonState.Released;
                    }
                case MouseButtons.XButton1:
                    {
                        return _previousMouseState.XButton1 == ButtonState.Pressed && _currentMouseState.XButton1 == ButtonState.Released;
                    }
                case MouseButtons.XButton2:
                    {
                        return _previousMouseState.XButton2 == ButtonState.Pressed && _currentMouseState.XButton2 == ButtonState.Released;
                    }
                default:
                    break;
            }

            return false;
        }

        public static bool IsMouseButtonDown(MouseButtons aMouseButton)
        {
            switch (aMouseButton)
            {
                case MouseButtons.None:
                    {
                        return false;
                    }
                case MouseButtons.Left:
                    {
                        return _currentMouseState.LeftButton == ButtonState.Pressed;
                    }
                case MouseButtons.Middle:
                    {
                        return _currentMouseState.MiddleButton == ButtonState.Pressed;
                    }
                case MouseButtons.Right:
                    {
                        return _currentMouseState.RightButton == ButtonState.Pressed;
                    }
                case MouseButtons.XButton1:
                    {
                        return _currentMouseState.XButton1 == ButtonState.Pressed;
                    }
                case MouseButtons.XButton2:
                    {
                        return _currentMouseState.XButton2 == ButtonState.Pressed;
                    }
                default:
                    break;
            }
            return false;
        }

        public static bool WasMouseButtonDoubleClicked(MouseButtons aMouseButton, float aMaxTimeBetweenPresses)
        {
            return WasMouseButtonPressed(aMouseButton) && TimeBetweenClicks(aMouseButton) < aMaxTimeBetweenPresses;
        }

        public static float TimeBetweenClicks(MouseButtons aMouseButton)
        {
            switch (aMouseButton)
            {
                case MouseButtons.None:
                    return 10000f;
                case MouseButtons.Left:
                    return Helper.DifferenceBetween(_pressTimeMouseLeft, _previousPressTimeMouseLeft);
                case MouseButtons.Middle:
                    return Helper.DifferenceBetween(_pressTimeMouseMiddle, _previousPressTimeMouseMiddle);
                case MouseButtons.Right:
                    return Helper.DifferenceBetween(_pressTimeMouseRight, _previousPressTimeMouseRight);
                case MouseButtons.XButton1:
                    return Helper.DifferenceBetween(_pressTimeXButton1, _previousPressTimeXButton1);
                case MouseButtons.XButton2:
                    return Helper.DifferenceBetween(_pressTimeXButton2, _previousPressTimeXButton2);
                default:
                    break;
            }

            return 10f;
        }

        public static float TimeSinceMouseButtonDown(MouseButtons aMouseButton)
        {
            switch (aMouseButton)
            {
                case MouseButtons.None:
                    {
                        return 100f;
                    }
                case MouseButtons.Left:
                    {
                        return _timeSinceButtonDownMouseLeft;
                    }
                case MouseButtons.Middle:
                    {
                        return _timeSinceButtonDownMouseMiddle;
                    }
                case MouseButtons.Right:
                    {
                        return _timeSinceButtonDownMouseRight;
                    }
                case MouseButtons.XButton1:
                    {
                        return _timeSinceButtonDownXButton1;
                    }
                case MouseButtons.XButton2:
                    {
                        return _timeSinceButtonDownXButton2;
                    }
                default:
                    {
                        return 0f;
                    }
            }
        }

        #endregion

        #region Button Map Methods

        public static bool WasButtonPressed(PlayerIndex aPlayerIndex, ButtonMap map)
        {
            return WasButtonPressed(aPlayerIndex, map.GamePadButton) || WasKeyPressed(map.Key) || WasMouseButtonPressed(map.MouseButton); 
        }

        public static bool WasButtonReleased(PlayerIndex aPlayerIndex, ButtonMap map)
        {
            return WasButtonReleased(aPlayerIndex, map.GamePadButton) || WasKeyReleased(map.Key) || WasMouseButtonReleased(map.MouseButton);
        }

        public static bool IsButtonDown(PlayerIndex aPlayerIndex, ButtonMap map)
        {
            return IsButtonDown(aPlayerIndex, map.GamePadButton) || IsKeyDown(map.Key) || IsMouseButtonDown(map.MouseButton);
        }

        public static bool WasButtonDoubleTapped(PlayerIndex aPlayerIndex, ButtonMap map, float aMaxTimeBetweenPresses)
        {
            return WasButtonDoubleTapped(aPlayerIndex, map.GamePadButton, aMaxTimeBetweenPresses) || 
                WasKeyDoubleTapped(map.Key, aMaxTimeBetweenPresses) || 
                WasMouseButtonDoubleClicked(map.MouseButton, aMaxTimeBetweenPresses);
        }

        public static float TimeSinceButtonDown(PlayerIndex aPlayerIndex, ButtonMap map)
        {
            float[] times = new float[3];

            times[0] = TimeSinceButtonDown(aPlayerIndex, map.GamePadButton);
            times[1] = TimeSinceKeyDown(map.Key);
            times[2] = TimeSinceMouseButtonDown(map.MouseButton);

            return times.Min();
        }

        #endregion

        #region Menu Selection and Navigation Methods

        public static bool IsMenuUp()
        {
            return WasKeyPressed(Keys.W) ||
                WasKeyPressed(Keys.Up) ||
                WasButtonPressed(0, Buttons.LeftThumbstickUp) ||
                WasButtonPressed(0, Buttons.DPadUp);
        }

        public static bool IsMenuDown()
        {
            return WasKeyPressed(Keys.S) ||
                WasKeyPressed(Keys.Down) ||
                WasButtonPressed(0, Buttons.LeftThumbstickDown) ||
                WasButtonPressed(0, Buttons.DPadDown);
        }

        public static bool IsMenuRight()
        {
            return WasKeyPressed(Keys.D) ||
                WasKeyPressed(Keys.Right) ||
                WasButtonPressed(0, Buttons.LeftThumbstickRight) ||
                WasButtonPressed(0, Buttons.DPadRight);
        }

        public static bool IsMenuLeft()
        {
            return WasKeyPressed(Keys.A) ||
                WasKeyPressed(Keys.Left) ||
                WasButtonPressed(0, Buttons.LeftThumbstickLeft) ||
                WasButtonPressed(0, Buttons.DPadLeft);
        }

        public static bool IsMenuSelect()
        {
            return WasKeyPressed(Keys.Enter) ||
                WasKeyPressed(Keys.Space) ||
                WasButtonPressed(0, Buttons.A) ||
                WasButtonPressed(0, Buttons.Start) ||
                WasMouseButtonPressed(MouseButtons.Left);
        }

        public static bool IsMenuCancel()
        {
            return WasKeyPressed(Keys.Escape) ||
                WasButtonPressed(0, Buttons.B);
        }

        #endregion

        #region Helper Methods

        private static void UpdateButtonDownTimes(int index)
        {
            #region A Button
            if (_currentGamepadState[index].IsButtonDown(Buttons.A))
            {
                _timeSinceButtonDownA[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownA[index] = 0f;
            }
            #endregion

            #region B Button
            if (_currentGamepadState[index].IsButtonDown(Buttons.B))
            {
                _timeSinceButtonDownB[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownB[index] = 0f;
            }
            #endregion

            #region Back Button
            if (_currentGamepadState[index].IsButtonDown(Buttons.Back))
            {
                _timeSinceButtonDownBack[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownBack[index] = 0f;
            }
            #endregion

            #region Big Button
            if (_currentGamepadState[index].IsButtonDown(Buttons.BigButton))
            {
                _timeSinceButtonDownBigButton[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownBigButton[index] = 0f;
            }
            #endregion

            #region DPad Down
            if (_currentGamepadState[index].IsButtonDown(Buttons.DPadDown))
            {
                _timeSinceButtonDownDPadDown[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownDPadDown[index] = 0f;
            }
            #endregion

            #region DPad Left
            if (_currentGamepadState[index].IsButtonDown(Buttons.DPadLeft))
            {
                _timeSinceButtonDownDPadLeft[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownDPadLeft[index] = 0f;
            }
            #endregion

            #region DPad Right
            if (_currentGamepadState[index].IsButtonDown(Buttons.DPadRight))
            {
                _timeSinceButtonDownDPadRight[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownDPadRight[index] = 0f;
            }
            #endregion

            #region DPad Up
            if (_currentGamepadState[index].IsButtonDown(Buttons.DPadUp))
            {
                _timeSinceButtonDownDPadUp[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownDPadUp[index] = 0f;
            }
            #endregion

            #region LeftShoulder
            if (_currentGamepadState[index].IsButtonDown(Buttons.LeftShoulder))
            {
                _timeSinceButtonDownLeftShoulder[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownLeftShoulder[index] = 0f;
            }
            #endregion

            #region LeftStick
            if (_currentGamepadState[index].IsButtonDown(Buttons.LeftStick))
            {
                _timeSinceButtonDownLeftStick[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownLeftStick[index] = 0f;
            }
            #endregion

            #region Left Thumbstick Down
            if (_currentGamepadState[index].IsButtonDown(Buttons.LeftThumbstickDown))
            {
                _timeSinceButtonDownLeftThumbstickDown[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownLeftThumbstickDown[index] = 0f;
            }
            #endregion

            #region Left Thumbstick Left
            if (_currentGamepadState[index].IsButtonDown(Buttons.LeftThumbstickLeft))
            {
                _timeSinceButtonDownLeftThumbstickLeft[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownLeftThumbstickLeft[index] = 0f;
            }
            #endregion

            #region Left Thumbstick Right
            if (_currentGamepadState[index].IsButtonDown(Buttons.LeftThumbstickRight))
            {
                _timeSinceButtonDownLeftThumbstickRight[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownLeftThumbstickRight[index] = 0f;
            }
            #endregion

            #region Left Thumbstick Up
            if (_currentGamepadState[index].IsButtonDown(Buttons.LeftThumbstickUp))
            {
                _timeSinceButtonDownLeftThumbstickUp[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownLeftThumbstickUp[index] = 0f;
            }
            #endregion

            #region Left Trigger
            if (_currentGamepadState[index].IsButtonDown(Buttons.LeftTrigger))
            {
                _timeSinceButtonDownLeftTrigger[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownLeftTrigger[index] = 0f;
            }
            #endregion

            #region Right Shoulder
            if (_currentGamepadState[index].IsButtonDown(Buttons.RightShoulder))
            {
                _timeSinceButtonDownRightShoulder[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownRightShoulder[index] = 0f;
            }
            #endregion

            #region Right Stick
            if (_currentGamepadState[index].IsButtonDown(Buttons.RightStick))
            {
                _timeSinceButtonDownRightStick[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownRightStick[index] = 0f;
            }
            #endregion

            #region Right Thumbstick Down
            if (_currentGamepadState[index].IsButtonDown(Buttons.RightThumbstickDown))
            {
                _timeSinceButtonDownRightThumbstickDown[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownRightThumbstickDown[index] = 0f;
            }
            #endregion

            #region Right Thumbstick Left
            if (_currentGamepadState[index].IsButtonDown(Buttons.RightThumbstickLeft))
            {
                _timeSinceButtonDownRightThumbstickLeft[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownRightThumbstickLeft[index] = 0f;
            }
            #endregion

            #region Right Thumbstick Right
            if (_currentGamepadState[index].IsButtonDown(Buttons.RightThumbstickRight))
            {
                _timeSinceButtonDownRightThumbstickRight[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownRightThumbstickRight[index] = 0f;
            }
            #endregion

            #region Right Thumbstick Up
            if (_currentGamepadState[index].IsButtonDown(Buttons.RightThumbstickUp))
            {
                _timeSinceButtonDownRightThumbstickUp[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownRightThumbstickUp[index] = 0f;
            }
            #endregion

            #region Right Trigger
            if (_currentGamepadState[index].IsButtonDown(Buttons.RightTrigger))
            {
                _timeSinceButtonDownRightTrigger[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownRightTrigger[index] = 0f;
            }
            #endregion

            #region Start
            if (_currentGamepadState[index].IsButtonDown(Buttons.Start))
            {
                _timeSinceButtonDownStart[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownStart[index] = 0f;
            }
            #endregion

            #region X
            if (_currentGamepadState[index].IsButtonDown(Buttons.X))
            {
                _timeSinceButtonDownX[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownX[index] = 0f;
            }
            #endregion

            #region Y
            if (_currentGamepadState[index].IsButtonDown(Buttons.Y))
            {
                _timeSinceButtonDownY[index] += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownY[index] = 0f;
            }
            #endregion
        }

        private static void UpdateMouseButtonDownTimes()
        {
            #region Left Button
            if (_currentMouseState.LeftButton == ButtonState.Pressed)
            {
                _timeSinceButtonDownMouseLeft += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownMouseLeft = 0f;
            }
            #endregion

            #region Middle Button
            if (_currentMouseState.MiddleButton == ButtonState.Pressed)
            {
                _timeSinceButtonDownMouseMiddle += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownMouseMiddle = 0f;
            }
            #endregion

            #region Right Button
            if (_currentMouseState.RightButton == ButtonState.Pressed)
            {
                _timeSinceButtonDownMouseRight += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownMouseRight = 0f;
            }
            #endregion

            #region XButton1
            if (_currentMouseState.XButton1 == ButtonState.Pressed)
            {
                _timeSinceButtonDownXButton1 += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownXButton1 = 0f;
            }
            #endregion

            #region XButton2
            if (_currentMouseState.XButton2 == ButtonState.Pressed)
            {
                _timeSinceButtonDownXButton2 += (float)TimeManager.SecondDifference;
            }
            else
            {
                _timeSinceButtonDownXButton2 = 0f;
            }
            #endregion
        }

        #endregion
    }
}
