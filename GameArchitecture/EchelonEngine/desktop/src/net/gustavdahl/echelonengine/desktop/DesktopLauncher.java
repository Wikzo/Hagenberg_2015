package net.gustavdahl.echelonengine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.gustavdahl.echelonengine.MyGameEngine_old;
import net.gustavdahl.echelonengine.scenes.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Echelon Engine";
		config.width = 1366;
		config.height = 768;
		
		new LwjglApplication(new MyGame(), config);
	}
}
