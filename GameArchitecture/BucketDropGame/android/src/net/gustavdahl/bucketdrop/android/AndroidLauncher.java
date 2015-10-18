package net.gustavdahl.bucketdrop.android;


import android.os.Bundle;
import net.gustavdahl.bucketdrop.GameScreen;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      AndroidApplicationConfiguration config= new AndroidApplicationConfiguration();
      config.useAccelerometer = false;
      config.useCompass = false;

      initialize(new GameScreen(), config);
   }
}