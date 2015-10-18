package net.gustavdahl.bucketdrop.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;


import net.gustavdahl.bucketdrop.GameScreen;


public class HtmlLauncher extends GwtApplication {
   @Override
   public GwtApplicationConfiguration getConfig () {
      return new GwtApplicationConfiguration(800, 480);
   }

   @Override
   public ApplicationListener getApplicationListener () {
      return new GameScreen();
   }
}