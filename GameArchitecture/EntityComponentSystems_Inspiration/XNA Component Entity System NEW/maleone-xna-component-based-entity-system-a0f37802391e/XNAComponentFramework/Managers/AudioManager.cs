using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework.Audio;

namespace ComponentFramework
{
    /// <summary>
    /// Used to play sounds effects and songs
    /// </summary>
    public static class AudioManager
    {
        private static AudioEngine _audioEngine;
        private static WaveBank _waveBank;
        private static SoundBank _soundBank;

        public static void Initialize()
        {
            //_audioEngine = new AudioEngine("Audio/ComponentFrameworkAudio.xgs");
            //_waveBank = new WaveBank(_audioEngine, "Audio/ComponentFrameworkAudio.xwb");
            //_soundBank = new SoundBank(_audioEngine, "Audio/ComponentFrameworkAudio.xsb");
        }

        //public static void PlaySound(string aSoundName)
        //{
        //    _soundBank.PlayCue(aSoundName);
        //}

        public static void Update()
        {
            //_audioEngine.Update();
        }
    }
}
