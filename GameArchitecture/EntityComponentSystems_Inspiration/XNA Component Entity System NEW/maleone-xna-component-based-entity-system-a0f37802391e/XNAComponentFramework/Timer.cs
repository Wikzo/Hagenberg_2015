using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using ComponentFramework;

namespace ComponentFramework
{
    public delegate void TimerFinished();

    public class Timer
    {
        static List<Timer> Timers = new List<Timer>();

        private double _timeRemaining;
        private double _length;

        public event TimerFinished Fire;

        public void Start(double length)
        {
            Timers.Remove(this);
            Timers.Add(this);
            this._length = length;
            this._timeRemaining = length;
        }

        public void Stop()
        {
            Timers.Remove(this);
        }

        public static void Update()
        {
            for (int i = Timers.Count - 1; i >= 0; i--)
            {
                Timer timer = Timers[i];

                timer._timeRemaining -= TimeManager.SecondDifference;
                if (timer._timeRemaining <= 0)
                {
                    timer.Fire();
                    timer._timeRemaining = timer._length;
                }
            }
        }
    }
}
