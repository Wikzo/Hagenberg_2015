using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using StopWatch = System.Diagnostics.Stopwatch;

namespace ComponentFramework
{
    public static class TimeManager
    {
        private static StopWatch _stopWatch;
        private static double _lastSecondDifference;
        private static double _secondDifference;
        private static double _lastCurrentTime;
        private static TimeSpan _elapsedGameTime;

        public static float TimeScale = 1f;

        public static double CurrentTime;

        public static double SystemCurrentTime
        {
            get { return _stopWatch.Elapsed.TotalSeconds; }
        }

        static TimeManager()
        {
            TimeScale = 1.0f;
        }

        public static void Initialize()
        {
            InitializeStopWatch();
        }

        public static void InitializeStopWatch()
        {
            _stopWatch = new StopWatch();
            _stopWatch.Start();
        }

        public static void Update(GameTime gameTime)
        {
            _lastSecondDifference = _secondDifference;
            _lastCurrentTime = CurrentTime;

            _secondDifference = gameTime.ElapsedGameTime.TotalSeconds * TimeScale;
            CurrentTime += _secondDifference;

            _elapsedGameTime = gameTime.ElapsedGameTime;
        }

        public static double SecondDifference
        {
            get { return _secondDifference; }
        }

        public static TimeSpan ElapsedGameTime
        {
            get { return _elapsedGameTime; }
        }

        public static double SecondsSince(double aTime)
        {
            return CurrentTime - aTime;
        }
    }
}
